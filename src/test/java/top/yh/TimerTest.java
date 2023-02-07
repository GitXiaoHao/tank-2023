package top.yh;

import org.junit.Test;
import top.yh.utils.Direction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author yuhao
 * @date 2023/2/4
 **/
public class TimerTest {
    @Test
    public void test() throws InterruptedException {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("123");
            }
        });
        timer.start();
        Thread.sleep(5000);
        timer.stop();
        Thread.sleep(5000);
        timer.start();
        JOptionPane.showMessageDialog(null, "退出程序？");
        System.exit(0);

    }
    @Test
    public void test02(){
        System.out.println(Direction.values().length);
    }
}
