package top.yh.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuhao
 * @date 2023/2/3
 **/
public class GameWindowData {
    /**
     * 0未开始
     * 1开始
     * 2失败
     * 3胜利
     * 4暂停
     */
    public static Condition state = Condition.NotStarted;
    /**
     * 循环次数
     */
    public static AtomicInteger numberOfCycles = new AtomicInteger(0);
    /**
     * 开始时的循环次数
     */
    public static AtomicInteger beginNumberOfCycles = new AtomicInteger(0);
    private GameWindowData() {

    }

}
