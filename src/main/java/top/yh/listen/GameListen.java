package top.yh.listen;

import top.yh.utils.Condition;
import top.yh.utils.GameWindowData;

import java.awt.event.*;

/**
 * @author yuhao
 * @date 2023/2/3
 **/
public class GameListen {
    /**
     * 游戏窗体的监听
     * 点击开始或暂停
     * 按空格键暂停
     * 按任意键开始
     */
    public static class GameFrameListen extends MouseAdapter{
        private static void beginOrStop(InputEvent e,boolean keyOrMouse){
            switch (GameWindowData.state){
                case Begin:
                    if(keyOrMouse || ((KeyEvent)e).getKeyCode() == KeyEvent.VK_SPACE) {
                        //暂停
                        GameWindowData.state = Condition.Pause;
                    }
                    break;
                case NotStarted:
                    GameWindowData.state = Condition.Begin;
                    break;
                case Win:
                case Fail:
                default:
                    GameWindowData.state = Condition.NotStarted;
            }
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            beginOrStop(e,true);
        }

        public static class KeyListen extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                beginOrStop(e,false);
            }
        }
    }
}
