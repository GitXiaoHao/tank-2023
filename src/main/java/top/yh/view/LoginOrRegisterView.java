package top.yh.view;

import top.yh.PropertiesName;
import top.yh.resources.ViewCommonData;
import top.yh.resources.AbstractViewData;

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
        String filePath;
        if (ViewCommonData.nowState) {
            filePath = PropertiesName.LOGIN_VIEW_PATH;
        }else {
            filePath = PropertiesName.REGISTER_VIEW_PATH;
        }
        final AbstractViewData viewData = new LoginDataAbstract(filePath);
        /*
         *登陆窗格 初始化
         */
        //窗体
        viewData.initFrame(this);
        //添加面板
        this.getContentPane().add(viewData.addBackImage(viewData.initLoginOrRegisterJpanel(viewData.getJpanel())));
        this.setVisible(true);
    }

    private static class LoginDataAbstract extends AbstractViewData {


        public LoginDataAbstract(String filePath) {
            super(filePath);
        }

        @Override
        protected void otherInitData() {

        }
    }
}