package top.yh.resources;

import java.awt.*;

/**
 * @author yuhao
 * @date 2023/9/25
 *  登录和注册的尺寸
 **/
public class RegisterOrLoginMeasure {
    /**
     * 登录界面 用户名位置
     */
    public final int loginUserX = 100;
    public final int loginUserY = 100;
    /**
     * 登录界面 密码位置
     */
    public final int loginPasswordX = loginUserX;
    public final int loginPasswordY = loginUserY + 43;
    /**
     * 用户名输入框位置
     */
    public final int loginUserFieldX = 150;
    public final int loginUserFieldY = 115;
    /**
     * 密码输入框位置
     */
    public final int loginPasswordFieldX = loginUserFieldX;
    public final int loginPasswordFieldY = loginUserFieldY + 40;
    /**
     * 登录按钮位置
     */
    public final int loginButtonX = 200;
    public final int loginButtonY = 250;
    /**
     * 注册标签位置
     */
    public final int registerX = 165;
    public final int registerY = 300;
    /**
     * 返回标签位置
     */
    public final int BACK_X = 210;
    public final int BACK_Y = registerY;
    /**
     * 登录界面 用户名大小
     */
    public final int loginUserWidth = 100;
    public final int loginUserHeight = 50;
    /**
     * 登录界面 密码大小
     */
    public final int loginPasswordWidth = loginUserWidth;
    public final int loginPasswordHeight = loginUserHeight;
    /**
     * 登录界面输入框大小
     */
    public final int loginUserFieldWidth = 200;
    public final int loginUserFieldHeight = 25;
    public final int loginPasswordFieldWidth = loginUserFieldWidth;
    public final int loginPasswordFieldHeight = 25;
    /**
     * 登录按钮大小
     */
    public final int loginButtonWidth = 60;
    public final int loginButtonHeight = 20;
    /**
     * 注册标签大小
     */
    public final int registerWidth = 200;
    public final int registerHeight = 20;
    /**
     * 返回标签大小
     */
    public final int backWidth = 200;
    public final int backHeight = 40;
    /**
     * 输入框指定列数
     */
    public final int inputFieldSize = 20;
    /**
     * 用户名
     */
    public final String USER = "用户名";
    /**
     * 密码
     */
    public final String PASSWORD = "密码";
    /**
     * 登录按钮
     */
    public final String LOGIN = "登录";
    /**
     * 注册标签
     */
    public final String NOT_REGISTER = "没有账号，立即注册";
    public final Font FONT = new Font("微软雅黑", Font.BOLD, 15);
    /**
     * 注册标签字体
     */
    public final Font REGISTER_FONT = new Font("楷体", Font.BOLD, 15);
    /**
     * 返回标签字体
     */
    public final Font BACK_FONT = new Font("楷体", Font.BOLD, 20);
    /**
     * 字体颜色
     */
    public final Color COLOR = Color.BLACK;
    /**
     * 返回标签
     */
    public final String RETURN = "返回";
    public final String REGISTER = "注册";
}