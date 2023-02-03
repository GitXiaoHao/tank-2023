package top.yh.view;

import top.yh.listen.GameListen;
import top.yh.resourse.ViewCommonData;
import top.yh.resourse.ViewData;
import top.yh.utils.GameWindowData;
import top.yh.utils.GetImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author yuhao
 * @date 2023/2/3
 * 游戏界面
 **/
public class GameView extends JFrame {
    static {
        //菜单面板
        if (ViewCommonData.menuView == null) {
            ViewCommonData.menuView = new MenuView();
        }
    }

    private final GameData viewData;

    public GameView() throws HeadlessException {
        String filePath = "../view/game-view-data.properties";
        viewData = new GameData(filePath);
        viewData.otherInitData();
        viewData.initFrame(this);
        //取消边框
        this.setUndecorated(true);
        this.getContentPane().add(viewData.addBackImage(viewData.getJpanel()));
        //鼠标监听
        this.addMouseListener(new GameListen.GameFrameListen());
        //键盘监听
        this.addKeyListener(new GameListen.GameFrameListen.KeyListen());
        this.setVisible(true);
        ViewCommonData.menuView.setVisible(true);
        Timer timer = new Timer(viewData.flushMinus, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewCommonData.gameView.repaint();
                int value = GameWindowData.numberOfCycles.incrementAndGet();
                if (value == Integer.MAX_VALUE) {
                    GameWindowData.numberOfCycles.set(1);
                }
            }
        });
        timer.start();
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
        switch (GameWindowData.state) {
            case NotStarted:
                //未开始
                graphics.drawImage(viewData.getBackGroundImage().getImage(), 0, 0, viewData.getWindowsWidth(), viewData.getWindowsHeight(), null);
                break;
            case Begin:
                //菜单面板上的

                //重绘

                //开始
                //改变属性
                //敌方子弹
                GameWindowData.beginNumberOfCycles.incrementAndGet();
                break;
            case Fail:
                //失败
                graphics.drawImage(viewData.failBackGroundImage, 0, 0,
                        viewData.getWindowsWidth(), viewData.getWindowsHeight(), null);
                break;
            case Win:
                //胜利
                graphics.drawImage(viewData.winBackGroundImage, 0, 0,
                        viewData.getWindowsWidth(), viewData.getWindowsHeight(), null);
                break;
            case Pause:
                //暂停
                break;
            default:
                break;
        }
        g.drawImage(viewData.image, 0, 0, null);
    }


    private static class GameData extends ViewData {
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

        public GameData(String filePath) {
            super(filePath);
        }

        @Override
        protected void otherInitData() {
            this.failBackGroundImage = GetImage.getImage(this.getViewDataMap().get("failBackGroundImage"));
            this.winBackGroundImage = GetImage.getImage(this.getViewDataMap().get("winBackGroundImage"));
            this.flushMinus = Integer.parseInt(this.getViewDataMap().get("flushMinus"));
        }
    }

    public static class MenuView extends JFrame {
        private MenuView() {
            String filePath = "../view/menu-view-data.properties";
            final MenuData menuData = new MenuData(filePath);
            menuData.initFrame(this);
            menuData.otherInitData();
            //取消边框
            this.setUndecorated(true);
            //设置位置
            this.setBounds(menuData.menuWindowX, menuData.menuWindowY, menuData.getWindowsWidth(), menuData.getWindowsHeight());
            this.getContentPane().add(menuData.addBackImage(menuData.addButton(menuData.getJpanel())));
        }

        private static class MenuData extends ViewData {
            private static int size = 0;
            private int menuWindowX;
            private int menuWindowY;
            private int buttonX;
            private int buttonY;
            private int buttonWidth;
            private int buttonHeight;
            private int buttonDistance;

            public MenuData(String filePath) {
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
            }

            public JPanel addButton(JPanel panel) {

                JButton exitButton = new JButton("退出");
                JButton stopButton = new JButton("暂停");
                initButton(exitButton);
                initButton(stopButton);
                exitButton.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.gc();
                        System.exit(0);
                    }
                });
                panel.add(exitButton);
                panel.add(stopButton);
                return panel;
            }

            private void initButton(JButton button) {
                Font font = new java.awt.Font("华文行楷", Font.BOLD, 30);
                button.setBounds(this.buttonX + (this.buttonDistance * (size++)),
                        this.buttonY,
                        this.buttonWidth,
                        this.buttonHeight);
                button.setFont(font);
//                .让按钮随按钮上的图案变化
                button.setMargin(new Insets(0, 0, 0, 0));
                //设置凸起来的按钮，很多其他的swing也可用此方法
                button.setBorder(BorderFactory.createRaisedBevelBorder());
                //设置凹起来的按钮，很多其他的swing也可用此方法
                // button.setBorder(BorderFactory.createLoweredBevelBorder());
                //.对JButton去掉按钮的边框的设置
                button.setBorderPainted(false);
//按钮设置为透明，这样就不会挡着后面的背景
                button.setContentAreaFilled(false);
            }
        }
    }
}
