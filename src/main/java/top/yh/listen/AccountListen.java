package top.yh.listen;

import lombok.AllArgsConstructor;
import top.yh.database.service.UserService;
import top.yh.resources.ViewCommonData;
import top.yh.view.GameView;
import top.yh.view.LoginOrRegisterView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

/**
 * @author yuhao
 * @date 2023/2/3
 * 监听器
 **/
public class AccountListen {
    private LoginOrRegisterByInformationErrorListen loginErrorListen;
    private LoginOrRegisterByInformationErrorListen registerErrorListen;
    private LoginListenByLogin loginListenByLogin;
    private LoginListenByRegisterOrRegisterListenByBack registerListenByBack;
    private LoginListenByRegisterOrRegisterListenByBack loginListenByRegister;
    private RegisterListenByRegister registerListenByRegister;
    private UserService userService;

    public void loginAndRegisterError(Map<JTextField, JLabel> map, JTextField userField, JTextField passwordField) {
        loginErrorListen = new LoginOrRegisterByInformationErrorListen(map, userField, passwordField);
        registerErrorListen = new LoginOrRegisterByInformationErrorListen(map, userField, passwordField);
    }

    /**
     * 校验
     *
     * @return
     */
    private boolean visitedDatabase() {
        String user;
        String password;
        //测试
        if (1 / 1 == 1) {
            return true;
        }
        if (ViewCommonData.nowState) {
            //判断数据库
            user = loginErrorListen.userField.getText().trim();
            password = loginErrorListen.passwordField.getText().trim();
            if (userService == null) {
                userService = new UserService();
            }
            boolean ifUser = userService.ifUser(user, password);
            if (!ifUser) {
                JOptionPane.showMessageDialog(null, "用户名或密码错误", "Title", JOptionPane.ERROR_MESSAGE);
            }
            return ifUser;
        } else {
            //是注册
            user = registerErrorListen.userField.getText().trim();
            password = registerErrorListen.passwordField.getText().trim();
            if (userService.ifUser(user)) {
                //如果有这个用户名
                JOptionPane.showMessageDialog(null, "已有该用户名，请重新输入", "提示", JOptionPane.ERROR_MESSAGE);
            } else {
                return userService.addUser(user, password);
            }
        }
        return false;
    }

    public LoginListenByRegisterOrRegisterListenByBack getRegisterListenByBack(JLabel back) {
        if (registerListenByBack == null) {
            registerListenByBack = new LoginListenByRegisterOrRegisterListenByBack(back);
        }
        return registerListenByBack;
    }

    public LoginListenByRegisterOrRegisterListenByBack getLoginListenByRegister(JLabel register) {
        if (loginListenByRegister == null) {
            loginListenByRegister = new LoginListenByRegisterOrRegisterListenByBack(register);
        }
        return loginListenByRegister;
    }

    public LoginListenByLogin getLoginListenByLogin() {
        if (loginListenByLogin == null) {
            loginListenByLogin = new LoginListenByLogin();
        }
        return loginListenByLogin;
    }

    public RegisterListenByRegister getRegisterListenByRegister() {
        if (registerListenByRegister == null) {
            registerListenByRegister = new RegisterListenByRegister();
        }
        return registerListenByRegister;
    }

    /**
     * 登录页面的注册按钮
     * 注册页面的返回按钮
     */
    private static class LoginListenByRegisterOrRegisterListenByBack extends MouseAdapter {
        private final JLabel label;

        public LoginListenByRegisterOrRegisterListenByBack(JLabel label) {
            this.label = label;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //登录页面换为注册 注册页面换为登录
            if (ViewCommonData.nowState) {
                //当前页面是登录
                //将当前窗体设为隐藏
                ViewCommonData.loginWindow.setVisible(false);
                ViewCommonData.loginSuccess = false;
                //展示注册窗体
                if (ViewCommonData.registerWindow == null) {
                    ViewCommonData.registerWindow = new LoginOrRegisterView(ViewCommonData.register);
                } else {
                    ViewCommonData.registerWindow.setVisible(true);
                }
            } else {
                //当前是注册页面
                //注册界面隐藏
                ViewCommonData.registerWindow.setVisible(false);
                //登录界面出现
                ViewCommonData.loginWindow.setVisible(true);
                ViewCommonData.loginSuccess = true;
            }
            ViewCommonData.nowState = !ViewCommonData.nowState;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //鼠标移入改变颜色
            this.label.setForeground(Color.cyan);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //鼠标移出改变颜色
            this.label.setForeground(Color.black);
        }
    }

    /**
     * 登录和注册界面的提示信息
     */
    private record LoginOrRegisterByInformationErrorListen(Map<JTextField, JLabel> map, JTextField userField,
                                                           JTextField passwordField) {
        private LoginOrRegisterByInformationErrorListen(Map<JTextField, JLabel> map, JTextField userField, JTextField passwordField) {
            this.map = map;
            this.userField = userField;
            this.passwordField = passwordField;
            this.userField.addMouseListener(new ErrorListen(userField, true));
            this.passwordField.addMouseListener(new ErrorListen(passwordField, false));
        }

        @AllArgsConstructor
        private class ErrorListen extends MouseAdapter {
            private JTextField errorField;
            private boolean userOrPasswordField;

            //点击
            @Override
            public void mouseClicked(MouseEvent e) {
                //取消文字
                map.get(errorField).setText("");
            }

            /**
             * 按下鼠标按钮时
             */
            @Override
            public void mousePressed(MouseEvent e) {
                //取消文字
                map.get(errorField).setText("");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //进入组件时调用。
                //取消文字
                map.get(errorField).setText("");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //鼠标退出时
                //校验用户输入
                String word = errorField.getText().trim();
                String regExp = userOrPasswordField ? "^[^0-9][\\w_]{5,9}$" : "^[\\w_]{6,20}$";
                String information = userOrPasswordField ? "用户名不合法" : "密码不合法";
                if (word.matches(regExp)) {
                    //如果通过校验
                    //判断是登录还是注册
                    if (ViewCommonData.nowState) {
                        //是登录
                        ViewCommonData.loginSuccess = true;
                    } else {
                        //是注册
                        ViewCommonData.registerSuccess = true;
                    }
                    map.get(errorField).setText("");
                } else {
                    //没有通过
                    map.get(errorField).setText(information);
                }
            }
        }
    }

    /**
     * 登录界面的登录按钮
     */
    private class LoginListenByLogin extends KeyAdapter implements ActionListener {
        private synchronized void login() {
            if (ViewCommonData.loginSuccess) {
                //如果通过校验
                if (visitedDatabase()) {
                    if (ViewCommonData.gameView == null) {
                        ViewCommonData.gameView = new GameView();
                    } else {
                        ViewCommonData.gameView.setVisible(true);
                    }
                    ViewCommonData.loginSuccess = false;
                    ViewCommonData.loginWindow.setVisible(false);
                    ViewCommonData.loginWindow = null;
                    if (ViewCommonData.registerWindow != null) {
                        ViewCommonData.registerWindow.setVisible(false);
                        ViewCommonData.registerWindow = null;
                    }
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                //实现与登陆按钮同样的方法
                login();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //登录按钮
            login();
        }
    }

    private class RegisterListenByRegister extends KeyAdapter implements ActionListener {
        private synchronized void register() {
            //判断是不是注册
            if (!ViewCommonData.nowState) {
                if (ViewCommonData.registerSuccess) {
                    //如果通过校验
                    if (visitedDatabase()) {
                        //如果通过
                        JOptionPane.showMessageDialog(null, "注册成功");
                        //切换为登录页面
                        ViewCommonData.registerWindow.setVisible(false);
                        ViewCommonData.loginWindow.setVisible(true);
                    }
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            register();
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                //实现与登陆按钮同样的方法
                register();
            }
        }
    }
}