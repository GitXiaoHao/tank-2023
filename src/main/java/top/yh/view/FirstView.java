package top.yh.view;

import top.yh.utils.PropertiesName;
import top.yh.music.MusicPlay;
import top.yh.resources.ViewAbstract;
import top.yh.resources.ViewCommonData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author yuhao
 * @date 2023/2/2
 * 第一个视图 程序主入口
 **/
public class FirstView extends JFrame {
    public FirstView() throws HeadlessException {
        /*
         * 读取的数据文件
         */
        String filePath = PropertiesName.FIRST_VIEW_PATH;
        final ViewAbstract firstData = new FirstDataAbstract(filePath);
        //初始化面板
        firstData.initFrame(this);
        //点击监听器
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ViewCommonData.loginWindow = new LoginOrRegisterView(ViewCommonData.login);
                //设为可视化
                ViewCommonData.loginWindow.setVisible(true);
                //将第一个窗体隐藏
                ViewCommonData.firstView.setVisible(false);
                ViewCommonData.firstView = null;
            }
        });
        //开始音乐
        ViewCommonData.musicPlay = new MusicPlay("解压.wav");
        ViewCommonData.musicPlay.start();
        this.setVisible(true);
    }

    /**
     * 第一个视图的数据
     */
    private static class FirstDataAbstract extends ViewAbstract {

        public FirstDataAbstract(String filePath) {
            super(filePath);
        }

    }
}
