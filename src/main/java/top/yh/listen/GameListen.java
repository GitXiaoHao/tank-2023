package top.yh.listen;

import lombok.Getter;
import top.yh.resources.AbstractTankData;
import top.yh.resources.GameCommonData;
import top.yh.resources.ViewCommonData;
import top.yh.utils.Condition;

import javax.swing.*;
import java.awt.event.*;

/**
 * @author yuhao
 * @date 2023/2/3
 **/
public class GameListen {
    /**
     * 退出按钮的监听器
     */
    private static ExitButtonListen exitButtonListen;
    /**
     * 暂停和开始按钮的监听器
     */
    private static StopOrBeginButtonListen stopOrBeginButtonListen;
    /**
     * 物体的监听器
     */
    private static SuperListen superListen;
    /**
     * 是否开启了刷怪定时器
     */
    private static boolean whetherToStartTheTimer;
    /**
     * 是否初始化
     */
    private static boolean initializeOrNot;

    /**
     * 游戏窗体的鼠标监听对象
     */
    private GameFrameListen gameFrameMouseListen;
    /**
     * 游戏窗口按钮的键盘监听对象
     */
    private GameFrameListen.KeyListen gameFrameKeyListen;
    /**
     * 游戏窗体是否已经开始刷新
     */
    private boolean isGameViewRepaint;

    public GameListen() {
        //初始化判断对象
        whetherToStartTheTimer = initializeOrNot = isGameViewRepaint = false;
        //游戏物体的监听
        superListen = new SuperListen();
    }

    /**
     * 开启定时器
     */
    public void addTimerForFlushEnemyTank() {
        //如果没有打开刷怪的定时器
        if (!whetherToStartTheTimer) {
            //将判断刷怪的定时器bool 对象设为true
            whetherToStartTheTimer = true;
            //开启刷怪
            superListen.timerForFlushEnemyTank();
        }
        for (int i = 0; i < GameCommonData.enemyTankList.size(); i++) {
            AbstractTankData tank = GameCommonData.enemyTankList.get(i);
            //如果有就true
            boolean aBoolean = GameCommonData.isBeginBullet.contains(tank);
            if (!aBoolean) {
                GameCommonData.isBeginBullet.add(tank);
                superListen.timerForEnemyTankBullet(tank);
                superListen.timerForEnemyTankIsInspect(tank);
            }
        }
    }

    /**
     * 关闭刷怪定时器
     *
     * @param value 已经杀了多少
     */
    public void stopFlushEnemyTank(int value) {
        if (whetherToStartTheTimer) {
            //关闭定时器
            superListen.stopForFlushEnemyTank(value);
        }

    }

    /**
     * @return 返回游戏窗体监听对象
     */
    public GameFrameListen addGameFrameMouseListen() {
        if (gameFrameMouseListen == null) {
            gameFrameMouseListen = new GameFrameListen();
        }
        return gameFrameMouseListen;
    }

    /**
     * 返回游戏窗口按钮的键盘监听对象
     *
     * @return 返回监听器对象
     */
    public GameFrameListen.KeyListen addGameFrameKeyListen() {
        if (gameFrameKeyListen == null) {
            gameFrameKeyListen = new GameFrameListen.KeyListen();
        }
        return gameFrameKeyListen;
    }

    /**
     * 添加退出按钮监听器
     *
     * @param button 退出按钮
     * @return 返回监听器对象
     */
    public ExitButtonListen addExitButtonListen(JButton button) {
        if (exitButtonListen == null) {
            exitButtonListen = new ExitButtonListen(button);
        }
        return exitButtonListen;
    }

    /**
     * 添加菜单栏的暂停和开始按钮
     *
     * @param button 按钮
     * @return 返回监听器对象
     */
    public StopOrBeginButtonListen addStopOrBeginButtonListen(JButton button) {
        if (stopOrBeginButtonListen == null) {
            stopOrBeginButtonListen = new StopOrBeginButtonListen(button);
        }
        return stopOrBeginButtonListen;
    }

    /**
     * 还没有开始时预加载
     */
    public void notStartInitData() {
        if (!initializeOrNot) {
            initializeOrNot = true;
            superListen.notStartInitData();
        }
    }

    /**
     * 重绘
     *
     * @param minus 重绘时间
     */
    public void addTimerForGameViewRepaint(int minus) {
        if (!isGameViewRepaint) {
            superListen.timerForGameRepaint(minus);
            isGameViewRepaint = true;
        }
    }

    public void stopTimerForGameViewRepaint(Condition condition) {
        isGameViewRepaint = false;
        superListen.stopForGameRepaint();
        switch (condition) {
            case NotStarted:
            case Fail:
            case Win:
                exitButtonListen.button.setEnabled(true);
                break;
            case Pause:
            case Begin:
            default:
                break;
        }
    }


    /**
     * 游戏窗体的监听
     * 点击开始或暂停
     * 按空格键暂停
     * 按任意键开始
     */
    private static class GameFrameListen extends MouseAdapter {
        public GameFrameListen() {

        }

        /**
         * @param e          Event对象
         * @param keyOrMouse 如果是键盘就为false 如果是鼠标就为true
         */
        private static void beginOrStop(InputEvent e, boolean keyOrMouse) {
            switch (GameCommonData.state) {
                case Begin:
                    if (keyOrMouse || ((KeyEvent) e).getKeyCode() == KeyEvent.VK_SPACE) {
                        //暂停
                        GameCommonData.state = Condition.Pause;
                        //可点击
                        exitButtonListen.button.setEnabled(true);
                        stopOrBeginButtonListen.button.setText("开始");
                    }
                    if (!keyOrMouse) {
                        //如果是键盘
                        superListen.heroAndImageListen(e);
                    }
                    break;
                case NotStarted:
                case Pause:
                    //暂停 变 开始
                    GameCommonData.state = Condition.Begin;
                    //不可点击
                    exitButtonListen.button.setEnabled(false);
                    stopOrBeginButtonListen.button.setText("暂停");
                    ViewCommonData.gameView.repaint();
                    break;
                case Win:
                case Fail:
                    break;
                default:
                    GameCommonData.state = Condition.NotStarted;
                    stopOrBeginButtonListen.button.setText("开始");
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            beginOrStop(e, true);
        }

        public static class KeyListen extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                beginOrStop(e, false);
            }
        }
    }

    /**
     * 退出键
     */
    @Getter
    private static class ExitButtonListen implements ActionListener {
        private final JButton button;

        public ExitButtonListen(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ViewCommonData.musicPlay.stopMusic();
            System.gc();
            System.exit(0);
        }
    }

    /**
     * 暂停按钮 和 开始按钮监听
     */

    private static class StopOrBeginButtonListen implements ActionListener {
        private final JButton button;

        public StopOrBeginButtonListen(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (GameCommonData.state) {
                case Begin:
                    //当前是开始状态
                    //改为暂停
                    GameCommonData.state = Condition.Pause;
                    this.button.setText("开始");
                    //退出键可以按下
                    exitButtonListen.button.setEnabled(true);
                    break;
                case Pause:
                case NotStarted:
                    //当前是暂停状态
                    //改为开始
                    GameCommonData.state = Condition.Begin;
                    this.button.setText("暂停");
                    //退出键不能按下
                    exitButtonListen.button.setEnabled(false);
                    //要重绘
                    ViewCommonData.gameView.repaint();
                    break;
                default:
            }
        }
    }
}
