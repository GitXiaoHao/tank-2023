package top.yh.view;

import lombok.Getter;
import top.yh.PropertiesName;
import top.yh.database.utils.JdbcByDruid;
import top.yh.listen.GameListen;
import top.yh.resources.AbstractTankData;
import top.yh.resources.AbstractViewData;
import top.yh.resources.GameCommonData;
import top.yh.utils.Condition;
import top.yh.utils.GetImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author yuhao
 * @date 2023/2/3
 * 游戏界面
 **/
public class GameView extends JFrame implements PropertyChangeListener {
    /**
     * 菜单界面
     */
    private static final GameView.MenuView MENU_VIEW;
    /**
     * 游戏的监听对象
     */
    private static final GameListen GAME_LISTEN;

    static {
        // TODO 必须这样放
        //游戏监听对象
        GAME_LISTEN = new GameListen();
        //菜单面板
        MENU_VIEW = new MenuView();
    }

    /**
     * 游戏界面的数据
     */
    private final GameDataAbstract viewData;

    public GameView() throws HeadlessException {
        //关闭连接
        JdbcByDruid.close();
        //游戏窗体的文件路径
        final String filePath = PropertiesName.GAME_VIEW_PATH;
        //初始化界面数据对象
        viewData = new GameDataAbstract(filePath);
        //加入其他数据
        viewData.otherInitData();
        //初始化窗体
        viewData.initFrame(this);
        //取消边框
        this.setUndecorated(true);
        //加入游戏面板
        this.getContentPane().add(viewData.addBackImage(viewData.getJpanel()));
        //鼠标监听
        this.addMouseListener(GAME_LISTEN.addGameFrameMouseListen());
        //键盘监听
        this.addKeyListener(GAME_LISTEN.addGameFrameKeyListen());
        //初始化
        GAME_LISTEN.notStartInitData();
        //加入 PropertyChangeSupport
        GameCommonData.someListenValue.addPropertyChangeListener(this);
        //将菜单窗体可视化
        MENU_VIEW.setVisible(true);
        //游戏窗体可视化
        this.setVisible(true);
        //加入游戏窗体重绘的定时器
        GAME_LISTEN.addTimerForGameViewRepaint(viewData.getFlushMinus());
    }


    @Override
    public void paint(Graphics g) {
        //初始化双缓冲
        if (viewData.image == null) {
            viewData.image = createImage(viewData.getWindowsWidth(), viewData.getWindowsHeight());
        }
        //获取画笔
        Graphics graphics = viewData.image.getGraphics();
        //绘制填充
        graphics.fillRect(0, 0, viewData.getWindowsWidth(), viewData.getWindowsHeight());
        //判断
        switch (GameCommonData.state) {
            case NotStarted:
                //未开始
                graphics.drawImage(viewData.getBackGroundImage().getImage(), 0, 0, viewData.getWindowsWidth(), viewData.getWindowsHeight(), null);
                //停止重绘
                GAME_LISTEN.stopTimerForGameViewRepaint(Condition.NotStarted);
                break;
            case Begin:
                //开始重绘
                GAME_LISTEN.addTimerForGameViewRepaint(viewData.getFlushMinus());
                //计时器一直刷
                GAME_LISTEN.addTimerForFlushEnemyTank();
                //丢掉
                GameCommonData.superList.removeAll(GameCommonData.uselessList);
                GameCommonData.uselessList.clear();
                //重绘
                for (AbstractTankData aSuper : GameCommonData.superList) {
                    //画
                    aSuper.byImage(graphics);
                }
                //记录开始的刷新数
                GameCommonData.beginNumberOfCycles.incrementAndGet();
                break;
            case Fail:
                //失败
                graphics.drawImage(viewData.failBackGroundImage, 0, 0,
                        viewData.getWindowsWidth(), viewData.getWindowsHeight(), null);
                GAME_LISTEN.stopTimerForGameViewRepaint(Condition.Fail);

                break;
            case Win:
                //胜利
                graphics.drawImage(viewData.winBackGroundImage, 0, 0,
                        viewData.getWindowsWidth(), viewData.getWindowsHeight(), null);
                GAME_LISTEN.stopTimerForGameViewRepaint(Condition.Win);
                break;
            case Pause:
                //暂停
                GAME_LISTEN.stopTimerForGameViewRepaint(Condition.Pause);
                break;
            default:
                break;
        }
        g.drawImage(viewData.image, 0, 0, null);
    }

    /**
     * PropertyChangeSupport 当里面的数据变化时自动调用
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //改变数据的名字
        String evtPropertyName = evt.getPropertyName();
        //改变后的数据
        Object newValue = evt.getNewValue();
        //改变前的数据
        Object oldValue = evt.getOldValue();
        //如果改变的时 killNumber 击败了多少坦克
        if (GameCommonData.killNumberName.equalsIgnoreCase(evtPropertyName)) {
            //先打印
            System.out.println(evtPropertyName + " 新数据 " + newValue + " 旧数据 " + oldValue);
            //看看是否已经到达要求停止刷新敌方坦克
            GAME_LISTEN.stopFlushEnemyTank((int) newValue);
            //改变菜单面板的数据
            MENU_VIEW.changeLabel(newValue);
        }

    }

    /**
     * 游戏窗体的数据
     */
    @Getter
    private static class GameDataAbstract extends AbstractViewData {
        /**
         * 双缓冲图片
         */
        private Image image = null;
        /**
         * 失败背景图
         */
        private Image failBackGroundImage;
        private int flushMinus;
        /**
         * 胜利背景图
         */
        private Image winBackGroundImage;

        public GameDataAbstract(String filePath) {
            super(filePath);
        }

        @Override
        protected void otherInitData() {
            //初始化游戏窗体信息
            this.failBackGroundImage = GetImage.getImage(this.getViewDataMap().get("failBackGroundImage"));
            this.winBackGroundImage = GetImage.getImage(this.getViewDataMap().get("winBackGroundImage"));
            this.flushMinus = Integer.parseInt(this.getViewDataMap().get("flushMinus"));
        }
    }

    /**
     * 菜单面板
     */
    private static class MenuView extends JFrame {
        private static JLabel killLabel;

        private MenuView() {
            String filePath = PropertiesName.MENU_VIEW_PATH;
            final MenuDataAbstract menuData = new MenuDataAbstract(filePath);
            menuData.initFrame(this);
            menuData.otherInitData();
            //取消边框
            this.setUndecorated(true);
            //设置位置
            this.setBounds(menuData.menuWindowX, menuData.menuWindowY, menuData.getWindowsWidth(), menuData.getWindowsHeight());
            this.getContentPane().add(menuData.addBackImage(menuData.addButton(menuData.getJpanel())));
        }

        public void changeLabel(Object newValue) {
            killLabel.setText("当前已击杀" + newValue + "个坦克");
        }

        private static class MenuDataAbstract extends AbstractViewData {
            private int size = 0;
            private int menuWindowX;
            private int menuWindowY;
            private int buttonX;
            private int buttonY;
            private int buttonWidth;
            private int buttonHeight;
            private int buttonDistance;
            private int labelWidth;
            private int labelHeight;

            public MenuDataAbstract(String filePath) {
                super(filePath);
            }

            @Override
            protected void otherInitData() {
                this.menuWindowX = Integer.parseInt(this.getViewDataMap().get("menuWindowX"));
                this.menuWindowY = Integer.parseInt(this.getViewDataMap().get("menuWindowY"));
                this.buttonX = Integer.parseInt(this.getViewDataMap().get("buttonX"));
                this.buttonY = Integer.parseInt(this.getViewDataMap().get("buttonY"));
                this.buttonWidth = Integer.parseInt(this.getViewDataMap().get("buttonWidth"));
                this.buttonHeight = Integer.parseInt(this.getViewDataMap().get("buttonHeight"));
                this.buttonDistance = Integer.parseInt(this.getViewDataMap().get("buttonDistance"));
                this.labelWidth = Integer.parseInt(this.getViewDataMap().get("labelWidth"));
                this.labelHeight = Integer.parseInt(this.getViewDataMap().get("labelHeight"));
            }

            public JPanel addButton(JPanel panel) {
                JButton exitButton = new JButton("退出");
                JButton stopOrBeginButton = new JButton("开始");
                JButton againButton = new JButton("重玩");
                JButton backButton = new JButton("返回");
                initButton(backButton);
                initButton(stopOrBeginButton);
                initButton(againButton);
                initButton(exitButton);
                //添加监听器
                exitButton.addActionListener(GAME_LISTEN.addExitButtonListen(exitButton));
                stopOrBeginButton.addActionListener(GAME_LISTEN.addStopOrBeginButtonListen(stopOrBeginButton));
                panel.add(backButton);
                panel.add(stopOrBeginButton);
                addLabel(panel);
                panel.add(againButton);
                panel.add(exitButton);
                return panel;
            }

            public void addLabel(JPanel panel) {
                killLabel = new JLabel();
                killLabel.setBounds(this.buttonX + (this.buttonDistance * (size++)),
                        this.buttonY,
                        this.labelWidth,
                        this.labelHeight);
                Font font = new java.awt.Font("宋体", Font.PLAIN, 30);
                killLabel.setFont(font);
                killLabel.setForeground(Color.darkGray);
                panel.add(killLabel);
            }

            private void initButton(JButton button) {
                Font font = new java.awt.Font("华文行楷", Font.PLAIN, 30);
                button.setBounds(this.buttonX + (this.buttonDistance * (size++)),
                        this.buttonY,
                        this.buttonWidth,
                        this.buttonHeight);
                button.setFont(font);
                //.让按钮随按钮上的图案变化
                button.setMargin(new Insets(0, 0, 0, 0));
                //设置凸起来的按钮，很多其他的swing也可用此方法
                button.setBorder(BorderFactory.createRaisedBevelBorder());
                //设置凹起来的按钮，很多其他的swing也可用此方法
                // button.setBorder(BorderFactory.createLoweredBevelBorder());
                //.对JButton去掉按钮的边框的设置
                button.setBorderPainted(false);
                //按钮设置为透明，这样就不会挡着后面的背景
                button.setContentAreaFilled(false);
                //设置移入移出
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setForeground(Color.red);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setForeground(Color.black);
                    }
                });
            }
        }
    }
}
