package top.yh;

import org.junit.Test;
import top.yh.utils.Direction;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yuhao
 * @date 2023/2/2
 **/
public class PropertyTest {
    @Test
    public void test(){
        Map<String,String> list = new HashMap<>();
//        list.add("dsa");
        String  da = list.get("da");
        System.out.println(da);
        //Direction direction = Direction.valueOf("up");
        //System.out.println(direction.name());
        System.out.println("yu".toUpperCase());
        int[] ints = new int[Direction.values().length];
        System.out.println(ints[Direction.UP.ordinal()]);
        System.out.println(Direction.UP.ordinal());
        //设置退出键
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.out.println(KeyEvent.VK_W);
        System.out.println(KeyEvent.VK_S);
        System.out.println(KeyEvent.VK_A);
        System.out.println(KeyEvent.VK_D);
        System.out.println(KeyEvent.VK_UP);
        System.out.println(KeyEvent.VK_DOWN);
        System.out.println(KeyEvent.VK_RIGHT);
        System.out.println(KeyEvent.VK_LEFT);
        jFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());

            }
        });
        jFrame.setSize(200,300);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);

    }
}
