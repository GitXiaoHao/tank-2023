package top.yh.view;

import top.yh.listen.GameListen;
import top.yh.resources.ViewAbstract;
import top.yh.utils.PropertiesName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author yuhao
 * @date 2023/9/25
 **/
public class MenuView extends JFrame {
    public JLabel killLabel;
    private final GameListen gameListen;

    public MenuView(GameListen gameListen) throws HeadlessException {
        this.gameListen = gameListen;
        String filePath = PropertiesName.MENU_VIEW_PATH;
        final MenuDataAbstract menuData = new MenuDataAbstract(filePath);
        menuData.initFrame(this);
        menuData.otherInitData();
        //取消边框
        this.setUndecorated(true);
        //设置位置
        this.setBounds(menuData.getMenuWindowX(), menuData.getMenuWindowY(), menuData.getWindowsWidth(), menuData.getWindowsHeight());
    }

    public void changeLabel(Object newValue) {
        killLabel.setText("当前已击杀" + newValue + "个坦克");
    }

    private class MenuDataAbstract extends ViewAbstract {
        public MenuDataAbstract(String filePath) {
            super(filePath);
        }

        @Override
        public void otherInitData() {
            this.menuWindowX = Integer.parseInt(this.getViewDataMap().get("menuWindowX"));
            this.menuWindowY = Integer.parseInt(this.getViewDataMap().get("menuWindowY"));
            this.buttonX = Integer.parseInt(this.getViewDataMap().get("buttonX"));
            this.buttonY = Integer.parseInt(this.getViewDataMap().get("buttonY"));
            this.buttonWidth = Integer.parseInt(this.getViewDataMap().get("buttonWidth"));
            this.buttonHeight = Integer.parseInt(this.getViewDataMap().get("buttonHeight"));
            this.buttonDistance = Integer.parseInt(this.getViewDataMap().get("buttonDistance"));
            this.labelWidth = Integer.parseInt(this.getViewDataMap().get("labelWidth"));
            this.labelHeight = Integer.parseInt(this.getViewDataMap().get("labelHeight"));
        }
        @Override
        protected void initButton(JButton button) {
            Font font = new java.awt.Font("华文行楷", Font.PLAIN, 30);
            button.setBounds(this.buttonX + (this.buttonDistance * (size++)),
                    this.buttonY,
                    this.buttonWidth,
                    this.buttonHeight);
            button.setFont(font);
            //.让按钮随按钮上的图案变化
            button.setMargin(new Insets(0, 0, 0, 0));
            //设置凸起来的按钮，很多其他的swing也可用此方法
            button.setBorder(BorderFactory.createRaisedBevelBorder());
            //设置凹起来的按钮，很多其他的swing也可用此方法
            // button.setBorder(BorderFactory.createLoweredBevelBorder());
            //.对JButton去掉按钮的边框的设置
            button.setBorderPainted(false);
            //按钮设置为透明，这样就不会挡着后面的背景
            button.setContentAreaFilled(false);
            //设置移入移出
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setForeground(Color.red);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setForeground(Color.black);
                }
            });
        }
        @Override
        public JPanel initOtherPanel(JPanel panel) {
            JButton exitButton = new JButton("退出");
            JButton stopOrBeginButton = new JButton("开始");
            JButton againButton = new JButton("重玩");
            JButton backButton = new JButton("返回");
            initButton(backButton);
            initButton(stopOrBeginButton);
            initButton(againButton);
            initButton(exitButton);
            //添加监听器
            exitButton.addActionListener(gameListen.addExitButtonListen(exitButton));
            stopOrBeginButton.addActionListener(gameListen.addStopOrBeginButtonListen(stopOrBeginButton));
            panel.add(backButton);
            panel.add(stopOrBeginButton);
            panel.add(againButton);
            panel.add(exitButton);
            return panel;
        }
    }
}