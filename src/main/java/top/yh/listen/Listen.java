package top.yh.listen;

import lombok.AllArgsConstructor;
import top.yh.resourse.ViewCommonData;
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
public class Listen {
    public static LoginOrRegisterByInformationErrorListen loginErrorListen;
    public static LoginOrRegisterByInformationErrorListen registerErrorListen;

    private static boolean visitedDatabase() {
        String user;
        String password;
        if (ViewCommonData.nowState) {
            //判断数据库
            user = loginErrorListen.userField.getText().trim();
            password = loginErrorListen.passwordField.getText().trim();
        } else {
            //是注册
            user = registerErrorListen.userField.getText().trim();
            password = registerErrorListen.passwordField.getText().trim();
        }
        return true;
    }

    /**
     * 登录页面的注册按钮
     * 注册页面的返回按钮
     */
    @AllArgsConstructor
    public static class LoginListenByRegisterOrRegisterListenByBack extends MouseAdapter {
        private JLabel label;
        /**
         * 判断是注册还是返回
         * 如果是注册标签就为true
         * 如果是返回标签就为false
         */
        private boolean registerOrBack;

        @Override
        public void mouseClicked(MouseEvent e) {
            if(registerOrBack) {
                //将当前窗体设为隐藏
                ViewCommonData.loginWindow.setVisible(false);
                ViewCommonData.loginSuccess = false;
                //展示注册窗体
                if(ViewCommonData.registerWindow == null) {
                    ViewCommonData.registerWindow = new LoginOrRegisterView(ViewCommonData.register);
                }else{
                    ViewCommonData.registerWindow.setVisible(true);
                }
            }else {
                //注册界面隐藏
                ViewCommonData.registerWindow.setVisible(false);
                //登录界面出现
                ViewCommonData.loginWindow.setVisible(true);
            }
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
     * 登录界面的登录按钮
     */
    public static class LoginListenByLogin extends KeyAdapter implements ActionListener {
        private synchronized void login() {
            if (ViewCommonData.loginSuccess) {
                //如果通过校验
                if (visitedDatabase()) {
                    ViewCommonData.loginWindow.setVisible(false);
                    ViewCommonData.loginSuccess = false;
                    if(ViewCommonData.gameView == null) {
                        ViewCommonData.gameView = new GameView();
                    }else{
                        ViewCommonData.gameView.setVisible(true);
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

    /**
     * 登录和注册界面的提示信息
     */
    public static class LoginOrRegisterByInformationErrorListen {
        private final JTextField userField;
        private final JTextField passwordField;
        private final Map<JTextField, JLabel> map;

        public LoginOrRegisterByInformationErrorListen(Map<JTextField, JLabel> map, JTextField userField, JTextField passwordField) {
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

    public static class RegisterListenByRegister extends KeyAdapter implements ActionListener {
        private synchronized void register() {
            //判断是不是注册
            if (!ViewCommonData.nowState) {
                if (ViewCommonData.registerSuccess) {
                    //如果通过校验
                    if (visitedDatabase()) {
                        //如果通过
                        //提示

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
