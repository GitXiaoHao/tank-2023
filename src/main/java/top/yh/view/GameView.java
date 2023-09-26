package top.yh.view;

import lombok.Getter;
import top.yh.database.utils.JdbcByDruid;
import top.yh.listen.GameListen;
import top.yh.resources.GameCommonData;
import top.yh.resources.TankAbstract;
import top.yh.resources.ViewAbstract;
import top.yh.utils.Condition;
import top.yh.utils.GetImage;
import top.yh.utils.PropertiesName;

import javax.swing.*;
import java.awt.*;

/**
 * @author yuhao
 * @date 2023/2/3
 * 游戏界面
 **/
public class GameView extends JFrame {
    /**
     * 菜单界面
     */
    private final MenuView menuView;
    /**
     * 游戏的监听对象
     */
    private final GameListen gameListen;

    /**
     * 游戏界面的数据
     */
    private final GameDataAbstract viewData;

    public GameView() throws HeadlessException {
        //关闭连接
        JdbcByDruid.close();
        //游戏窗体的文件路径
        final String filePath = PropertiesName.GAME_VIEW_PATH;
        // TODO 必须这样放
        //游戏监听对象
        gameListen = new GameListen();
        //菜单面板
        menuView = new MenuView(gameListen);
        //初始化界面数据对象
        viewData = new GameDataAbstract(filePath);
        //初始化窗体
        viewData.initFrame(this);
        //取消边框
        this.setUndecorated(true);
        //鼠标监听
        this.addMouseListener(gameListen.addGameFrameMouseListen());
        //键盘监听
        this.addKeyListener(gameListen.addGameFrameKeyListen());
        //加入 PropertyChangeSupport
        GameCommonData.someListenValue.addPropertyChangeListener(menuView);
        //游戏窗体可视化
        this.setVisible(true);
        //将菜单窗体可视化
        menuView.setVisible(true);
        //初始化
        gameListen.notStartInitData();
        //加入游戏窗体重绘的定时器
        gameListen.addTimerForGameViewRepaint(viewData.getFlushMinus());
    }


    @Override
    public void paint(Graphics g) {
        //初始化双缓冲
        if (viewData.getImage() == null) {
            viewData.setImage(createImage(viewData.getWindowsWidth(), viewData.getWindowsHeight()));
        }
        //获取画笔
        Graphics graphics = viewData.getImage().getGraphics();
        //绘制填充
        graphics.fillRect(0, 0, viewData.getWindowsWidth(), viewData.getWindowsHeight());
        //判断
        switch (GameCommonData.state) {
            case NotStarted:
                //未开始
                graphics.drawImage(viewData.getBackGroundImage().getImage(), 0, 0, viewData.getWindowsWidth(), viewData.getWindowsHeight(), null);
                //停止重绘
                gameListen.stopTimerForGameViewRepaint(Condition.NotStarted);
                break;
            case Begin:
                //开始重绘
                gameListen.addTimerForGameViewRepaint(viewData.getFlushMinus());
                //计时器一直刷
                gameListen.addTimerForFlushEnemyTank();
                //丢掉
                GameCommonData.superList.removeAll(GameCommonData.uselessList);
                GameCommonData.uselessList.clear();
                //重绘
                for (TankAbstract aSuper : GameCommonData.superList) {
                    //画
                    aSuper.byImage(graphics);
                }
                //记录开始的刷新数
                GameCommonData.beginNumberOfCycles.incrementAndGet();
                break;
            case Fail:
                //失败
                graphics.drawImage(viewData.getFailBackGroundImage(), 0, 0,
                        viewData.getWindowsWidth(), viewData.getWindowsHeight(), null);
                gameListen.stopTimerForGameViewRepaint(Condition.Fail);

                break;
            case Win:
                //胜利
                graphics.drawImage(viewData.getWinBackGroundImage(), 0, 0,
                        viewData.getWindowsWidth(), viewData.getWindowsHeight(), null);
                gameListen.stopTimerForGameViewRepaint(Condition.Win);
                break;
            case Pause:
                //暂停
                gameListen.stopTimerForGameViewRepaint(Condition.Pause);
                break;
            default:
                break;
        }
        g.drawImage(viewData.getImage(), 0, 0, null);
    }

    /**
     * 游戏窗体的数据
     */
    @Getter
    private class GameDataAbstract extends ViewAbstract {


        public GameDataAbstract(String filePath) {
            super(filePath);
        }

        @Override
        public void otherInitData() {
            //初始化游戏窗体信息
            this.failBackGroundImage = GetImage.getImage(this.getViewDataMap().get("failBackGroundImage"));
            this.winBackGroundImage = GetImage.getImage(this.getViewDataMap().get("winBackGroundImage"));
            this.flushMinus = Integer.parseInt(this.getViewDataMap().get("flushMinus"));
        }

        @Override
        public JPanel initOtherPanel(JPanel panel) {

            return panel;
        }
    }
}
