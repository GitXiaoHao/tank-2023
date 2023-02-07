package top.yh.resources;

import top.yh.music.MusicPlay;
import top.yh.view.FirstView;
import top.yh.view.GameView;
import top.yh.view.LoginOrRegisterView;

/**
 * @author yuhao
 * @date 2023/2/3
 * 窗体的公共资源
 **/
public class ViewCommonData {
    private ViewCommonData(){

    }
    /**
     * 音乐
     */
    public static MusicPlay musicPlay;
    /**
     * 当前是登录 和 注册
     * 如果是登录就 true
     * 如果是注册就 false
     */
    public static boolean nowState = true;
    public static String register = "register";
    public static String login = "login";

    /**
     * 判断用户名或者密码是否通过校验
     * 登录界面
     */
    public static boolean loginSuccess = true;
    public static boolean registerSuccess = false;
    /**
     * 第一个窗体对象
     */
    public static FirstView firstView;
    /**
     * 登录窗体
     */
    public static LoginOrRegisterView loginWindow;
    /**
     * 注册窗体
     */
    public static LoginOrRegisterView registerWindow;
    /**
     * 游戏界面
     */
    public static GameView gameView;

}
