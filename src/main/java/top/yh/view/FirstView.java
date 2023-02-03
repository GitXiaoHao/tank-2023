package top.yh.view;

import top.yh.music.MusicPlay;
import top.yh.resourse.ViewCommonData;
import top.yh.resourse.ViewData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.*;

/**
 * @author yuhao
 * @date 2023/2/2
 * 第一个视图 程序主入口
 **/
public class FirstView extends JFrame {

    /**
     * 线程池
     */
    public static ExecutorService threadPool = new ThreadPoolExecutor(2, 5,
            1L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(3),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    public FirstView() throws HeadlessException {
        /*
         * 读取的数据文件
         */
        String filePath = "../view/first-view-data.properties";
        final ViewData firstData = new FirstData(filePath);
        //初始化面板
        firstData.initFrame(this);
        //添加窗体
        this.getContentPane().add(firstData.addBackImage(firstData.getJpanel()));
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
            }
        });
        //开始音乐
//        threadPool.execute(musicPlay);
        ViewCommonData.musicPlay = new MusicPlay("解压.wav");
        ViewCommonData.musicPlay.start();
        this.setVisible(true);
    }

    /**
     * 第一个视图的数据
     */
    private static class FirstData extends ViewData{

        public FirstData(String filePath) {
            super(filePath);
        }

        @Override
        protected void otherInitData() {

        }
    }

    public static void main(String[] args) {
        ViewCommonData.firstView = new FirstView();
    }

}
