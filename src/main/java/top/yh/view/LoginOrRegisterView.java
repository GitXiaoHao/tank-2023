package top.yh.view;

import top.yh.resourse.ViewCommonData;
import top.yh.resourse.ViewData;

import javax.swing.*;
import java.awt.*;

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
        String filePath = "../view/" + mode + "-view-data.properties";
        final ViewData viewData = new LoginData(filePath);
        /*
         *登陆窗格 初始化
         */
        //窗体
        viewData.initFrame(this);
        //添加面板
        this.getContentPane().add(viewData.addBackImage(viewData.initLoginOrRegisterJpanel(viewData.getJpanel(), mode)));
        this.setVisible(true);
    }

    private static class LoginData extends ViewData {


        public LoginData(String filePath) {
            super(filePath);
        }

        @Override
        protected void otherInitData() {

        }
    }
}