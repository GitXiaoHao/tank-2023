package top.yh;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuhao
 * @date 2023/2/3
 **/
public class GameTest {
    @Test
    public void test(){
        AtomicInteger beginNumberOfCycles = new AtomicInteger(0);
        System.out.println(beginNumberOfCycles);
        int i = beginNumberOfCycles.addAndGet(1);
        System.out.println(i);
        i = beginNumberOfCycles.getAndAdd(2);
        System.out.println(beginNumberOfCycles);
        System.out.println(i);
        i = Integer.MAX_VALUE;
        System.out.println(i);
    }
}
