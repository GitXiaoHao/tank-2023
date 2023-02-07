package top.yh.resources;

import lombok.Getter;
import top.yh.listen.ViewListen;
import top.yh.utils.GetImage;
import top.yh.utils.GetProperties;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuhao
 * @date 2023/2/2
 **/
@Getter
public abstract class AbstractViewData {

    private final String filePath;
    /**
     * 窗口大小
     */

    private int windowsWidth;
    private int windowsHeight;
    /**
     * 背景图
     */

    private ImageIcon backGroundImage;
    /**
     * 登录窗口图标
     */

    private Image loginWindowsImage;
    /**
     * 窗口标题
     */
    private String windowsTitle;
    /**
     * 存放视图数据的map
     * key是数据名
     * value是数据
     */
    private Map<String, String> viewDataMap;

    /**
     * 构造方法
     * @param filePath 读取的文件
     */
    public AbstractViewData(String filePath) {
        this.filePath = filePath;
        //先初始化共有数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //map数据
        viewDataMap = GetProperties.getAllProperties(filePath);
        windowsWidth = Integer.parseInt(viewDataMap.get("windowsWidth"));
        windowsHeight = Integer.parseInt(viewDataMap.get("windowsHeight"));
        backGroundImage = GetImage.getImageIcon(viewDataMap.get("backGroundImage"));
        loginWindowsImage = GetImage.getImage(viewDataMap.get("loginWindowsImage"));
        windowsTitle = viewDataMap.get("windowsTitle");
    }

    /**
     * 初始化其他的数据 由继承者进行编写
     */
    protected abstract void otherInitData();

    /**
     * 初始化面板
     */
    public JPanel getJpanel() {
        //面板
        JPanel panel = new JPanel();
        //设置大小
        panel.setBounds(0, 0, getWindowsWidth(), getWindowsHeight());
        //布局管理器
        panel.setLayout(null);
        //设置面板背景
        panel.setBackground(Color.WHITE);
        return panel;
    }

    /**
     * 初始化JFrame
     *
     * @param jFrame 窗体
     */
    public void initFrame(JFrame jFrame) {
        //设置标题
        jFrame.setTitle(getWindowsTitle());
        //设置大小
        jFrame.setSize(getWindowsWidth(), getWindowsHeight());
        //不可改变大小
        jFrame.setResizable(false);
        //窗口在屏幕中间
        jFrame.setLocationRelativeTo(null);
        //设置退出键
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //窗口图标
        jFrame.setIconImage(getLoginWindowsImage());
    }


    public JPanel initLoginOrRegisterJpanel(JPanel panel) {
        ViewListen viewListen = new ViewListen();
        RegisterOrLoginMeasure measure = new RegisterOrLoginMeasure();
        //用户名
        JLabel user = new JLabel(measure.USER);
        //密码
        JLabel password = new JLabel(measure.PASSWORD);
        //用户名输入框
        JTextField userField = new JTextField(measure.inputFieldSize);
        //用户名错误提示
        JLabel userErrorField = new JLabel();
        userErrorField.setForeground(Color.red);
        //密码输入框
        JPasswordField passwordField = new JPasswordField(measure.inputFieldSize);
        //密码错误提示
        JLabel passwordErrorField = new JLabel();
        passwordErrorField.setForeground(Color.red);
        //出错的提示
        Map<JTextField, JLabel> map = new HashMap<>(2);
        //将用户名和用户名提示错误进行捆绑
        map.put(userField, userErrorField);
        //将密码和密码提示信息进行捆绑
        map.put(passwordField, passwordErrorField);
        /*
        设置位置 大小
         */
        //用户名
        user.setBounds(measure.loginUserX,
                measure.loginUserY,
                measure.loginUserWidth,
                measure.loginUserHeight);
        //密码
        password.setBounds(measure.loginPasswordX,
                measure.loginPasswordY,
                measure.loginPasswordWidth,
                measure.loginPasswordHeight);
        //用户名输入框
        userField.setBounds(measure.loginUserFieldX,
                measure.loginUserFieldY,
                measure.loginUserFieldWidth,
                measure.loginUserFieldHeight);
        //用户名错误提示框
        userErrorField.setBounds(measure.loginUserFieldX + measure.loginUserFieldWidth + 5,
                measure.loginUserFieldY,
                measure.loginUserFieldWidth,
                measure.loginUserFieldHeight);
        //密码输入框
        passwordField.setBounds(measure.loginPasswordFieldX,
                measure.loginPasswordFieldY, measure.loginPasswordFieldWidth,
                measure.loginPasswordFieldHeight);
        passwordErrorField.setBounds(measure.loginPasswordFieldX + measure.loginPasswordFieldWidth + 5,
                measure.loginPasswordFieldY,
                measure.loginPasswordFieldWidth,
                measure.loginPasswordFieldHeight);


        //面板
        panel.setBounds(0, 0, windowsWidth, windowsHeight);
        /*
        字体
         */
        //用户名
        user.setFont(measure.FONT);
        //密码
        password.setFont(measure.FONT);
        /*
        颜色
         */
        //用户名
        user.setForeground(measure.COLOR);
        //密码
        password.setForeground(measure.COLOR);

        /*
        添加至面板
         */
        //用户名
        panel.add(user);
        //密码
        panel.add(password);
        //用户名输入框
        panel.add(userField);
        panel.add(userErrorField);
        //密码输入框
        panel.add(passwordField);
        panel.add(passwordErrorField);


        //如果时登录界面
        if (ViewCommonData.nowState) {
            //注册标签
            JLabel register = new JLabel(measure.NOT_REGISTER);
            //注册标签
            register.setBounds(measure.registerX,
                    measure.registerY,
                    measure.registerWidth,
                    measure.registerHeight);
            //注册标签
            register.setFont(measure.REGISTER_FONT);
            //注册标签
            register.setForeground(measure.COLOR);
            //登录按钮
            JButton login = new JButton(measure.LOGIN);
            //登陆
            login.setBounds(measure.loginButtonX,
                    measure.loginButtonY,
                    measure.loginButtonWidth,
                    measure.loginButtonHeight);
            //监听器
            //注册标签增加鼠标监听器
            register.addMouseListener(viewListen.getLoginListenByRegister(register));
            //登录按钮
            //按钮监听
            login.addActionListener(viewListen.getLoginListenByLogin());
            //键盘监听
            login.addKeyListener(viewListen.getLoginListenByLogin());
            //注册标签
            panel.add(register);
            //登录按钮
            panel.add(login);
        } else {
            //如果是注册界面
            //注册按钮
            JButton register = new JButton(measure.REGISTER);
            //注册标签
            JLabel back = new JLabel(measure.RETURN);
            //注册按钮
            register.setBounds(measure.loginButtonX,
                    measure.loginButtonY,
                    measure.loginButtonWidth,
                    measure.loginButtonHeight);
            //返回标签
            back.setBounds(measure.BACK_X, measure.BACK_Y, measure.backWidth, measure.backHeight);
            //返回标签
            back.setFont(measure.BACK_FONT);
            //注册标签
            register.setForeground(measure.COLOR);
            //监听器
            back.addMouseListener(viewListen.getRegisterListenByBack(back));
            //给注册按钮添加监听器
            register.addActionListener(viewListen.getRegisterListenByRegister());
            register.addKeyListener(viewListen.getRegisterListenByRegister());
            //注册标签
            panel.add(register);
            panel.add(back);
        }
        viewListen.loginAndRegisterError(map,userField,passwordField);
        return panel;
    }

    /**
     * 一定得放最后 不然会出问题！
     * 添加背景
     */
    public JPanel addBackImage(JPanel panel) {
        JLabel label = new JLabel();
        label.setBounds(0, 0, getWindowsWidth(), getWindowsHeight());
        //图片
        label.setIcon(getBackGroundImage());
        //加入面板
        panel.add(label);
        return panel;
    }
    /**
     * 登录和注册的尺寸
     */
    private static class RegisterOrLoginMeasure {
        /**
         * 登录界面 用户名位置
         */
        private final int loginUserX = 100;
        private final int loginUserY = 100;
        /**
         * 登录界面 密码位置
         */
        private final int loginPasswordX = loginUserX;
        private final int loginPasswordY = loginUserY + 43;
        /**
         * 用户名输入框位置
         */
        private final int loginUserFieldX = 150;
        private final int loginUserFieldY = 115;
        /**
         * 密码输入框位置
         */
        private final int loginPasswordFieldX = loginUserFieldX;
        private final int loginPasswordFieldY = loginUserFieldY + 40;
        /**
         * 登录按钮位置
         */
        private final int loginButtonX = 200;
        private final int loginButtonY = 250;
        /**
         * 注册标签位置
         */
        private final int registerX = 165;
        private final int registerY = 300;
        /**
         * 返回标签位置
         */
        private final int BACK_X = 210;
        private final int BACK_Y = registerY;
        /**
         * 登录界面 用户名大小
         */
        private final int loginUserWidth = 100;
        private final int loginUserHeight = 50;
        /**
         * 登录界面 密码大小
         */
        private final int loginPasswordWidth = loginUserWidth;
        private final int loginPasswordHeight = loginUserHeight;
        /**
         * 登录界面输入框大小
         */
        private final int loginUserFieldWidth = 200;
        private final int loginUserFieldHeight = 25;
        private final int loginPasswordFieldWidth = loginUserFieldWidth;
        private final int loginPasswordFieldHeight = 25;
        /**
         * 登录按钮大小
         */
        private final int loginButtonWidth = 60;
        private final int loginButtonHeight = 20;
        /**
         * 注册标签大小
         */
        private final int registerWidth = 200;
        private final int registerHeight = 20;
        /**
         * 返回标签大小
         */
        private final int backWidth = 200;
        private final int backHeight = 40;
        /**
         * 输入框指定列数
         */
        private final int inputFieldSize = 20;
        /**
         * 用户名
         */
        private final String USER = "用户名";
        /**
         * 密码
         */
        private final String PASSWORD = "密码";
        /**
         * 登录按钮
         */
        private final String LOGIN = "登录";
        /**
         * 注册标签
         */
        private final String NOT_REGISTER = "没有账号，立即注册";
        private final Font FONT = new Font("微软雅黑", Font.BOLD, 15);
        /**
         * 注册标签字体
         */
        private final Font REGISTER_FONT = new Font("楷体", Font.BOLD, 15);
        /**
         * 返回标签字体
         */
        private final Font BACK_FONT = new Font("楷体", Font.BOLD, 20);
        /**
         * 字体颜色
         */
        private final Color COLOR = Color.BLACK;
        /**
         * 返回标签
         */
        private final String RETURN = "返回";
        private final String REGISTER = "注册";
    }
}
