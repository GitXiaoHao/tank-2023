package top.yh.view;

import top.yh.listen.AccountListen;
import top.yh.resources.RegisterOrLoginMeasure;
import top.yh.resources.ViewAbstract;
import top.yh.resources.ViewCommonData;
import top.yh.utils.PropertiesName;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuhao
 * @date 2023/2/3
 **/
public class LoginOrRegisterView extends JFrame {


    public LoginOrRegisterView(String mode) throws HeadlessException {
        /*
          读取的数据文件
         */
        ViewCommonData.nowState = ViewCommonData.login.equalsIgnoreCase(mode);
        String filePath;
        if (ViewCommonData.nowState) {
            filePath = PropertiesName.LOGIN_VIEW_PATH;
        } else {
            filePath = PropertiesName.REGISTER_VIEW_PATH;
        }
        final ViewAbstract viewData = new LoginDataAbstract(filePath);
        /*
         *登陆窗格 初始化
         */
        //窗体
        viewData.initFrame(this);
        this.setVisible(true);
    }

    private static class LoginDataAbstract extends ViewAbstract {


        public LoginDataAbstract(String filePath) {
            super(filePath);
        }

        @Override
        public JPanel initOtherPanel(JPanel panel) {
            AccountListen accountListen = new AccountListen();
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
            //用户名
            user.setBounds(measure.loginUserX, measure.loginUserY, measure.loginUserWidth, measure.loginUserHeight);
            //密码
            password.setBounds(measure.loginPasswordX, measure.loginPasswordY, measure.loginPasswordWidth, measure.loginPasswordHeight);
            //用户名输入框
            userField.setBounds(measure.loginUserFieldX, measure.loginUserFieldY, measure.loginUserFieldWidth, measure.loginUserFieldHeight);
            //用户名错误提示框
            userErrorField.setBounds(measure.loginUserFieldX + measure.loginUserFieldWidth + 5, measure.loginUserFieldY, measure.loginUserFieldWidth, measure.loginUserFieldHeight);
            //密码输入框
            passwordField.setBounds(measure.loginPasswordFieldX, measure.loginPasswordFieldY, measure.loginPasswordFieldWidth, measure.loginPasswordFieldHeight);
            passwordErrorField.setBounds(measure.loginPasswordFieldX + measure.loginPasswordFieldWidth + 5, measure.loginPasswordFieldY, measure.loginPasswordFieldWidth, measure.loginPasswordFieldHeight);
            //面板
            panel.setBounds(0, 0, windowsWidth, windowsHeight);
            //用户名
            user.setFont(measure.FONT);
            //密码
            password.setFont(measure.FONT);
            //用户名
            user.setForeground(measure.COLOR);
            //密码
            password.setForeground(measure.COLOR);
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
                register.setBounds(measure.registerX, measure.registerY, measure.registerWidth, measure.registerHeight);
                //注册标签
                register.setFont(measure.REGISTER_FONT);
                //注册标签
                register.setForeground(measure.COLOR);
                //登录按钮
                JButton login = new JButton(measure.LOGIN);
                //登陆
                login.setBounds(measure.loginButtonX, measure.loginButtonY, measure.loginButtonWidth, measure.loginButtonHeight);
                //监听器
                //注册标签增加鼠标监听器
                register.addMouseListener(accountListen.getLoginListenByRegister(register));
                //登录按钮
                //按钮监听
                login.addActionListener(accountListen.getLoginListenByLogin());
                //键盘监听
                login.addKeyListener(accountListen.getLoginListenByLogin());
                //注册标签
                panel.add(register);
                //登录按钮
                panel.add(login);
            }
            else {
                //如果是注册界面
                //注册按钮
                JButton register = new JButton(measure.REGISTER);
                //注册标签
                JLabel back = new JLabel(measure.RETURN);
                //注册按钮
                register.setBounds(measure.loginButtonX, measure.loginButtonY, measure.loginButtonWidth, measure.loginButtonHeight);
                //返回标签
                back.setBounds(measure.BACK_X, measure.BACK_Y, measure.backWidth, measure.backHeight);
                //返回标签
                back.setFont(measure.BACK_FONT);
                //注册标签
                register.setForeground(measure.COLOR);
                //监听器
                back.addMouseListener(accountListen.getRegisterListenByBack(back));
                //给注册按钮添加监听器
                register.addActionListener(accountListen.getRegisterListenByRegister());
                register.addKeyListener(accountListen.getRegisterListenByRegister());
                //注册标签
                panel.add(register);
                panel.add(back);
            }
            accountListen.loginAndRegisterError(map, userField, passwordField);
            return panel;
        }
    }
}