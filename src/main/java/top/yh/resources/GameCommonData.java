package top.yh.resources;

import top.yh.listen.SomeListenValue;
import top.yh.utils.Condition;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuhao
 * @date 2023/2/3
 **/
public class GameCommonData {
    static {
        state = Condition.NotStarted;
        someListenValue = new SomeListenValue(0);
        numberOfCycles = new AtomicInteger(0);
        beginNumberOfCycles = new AtomicInteger(0);
        surplusHeroBulletNumber = new AtomicInteger(0);
        superList = Collections.synchronizedList(new ArrayList<>());
        enemyTankList = Collections.synchronizedList(new ArrayList<>());
        uselessList = Collections.synchronizedList(new ArrayList<>());
        heroBulletList = Collections.synchronizedList(new ArrayList<>());
        enemyTankBulletList = Collections.synchronizedList(new ArrayList<>());
        isBeginBullet = Collections.synchronizedList(new ArrayList<>());
        tankDataTimerMap = Collections.synchronizedMap(new HashMap<>());
        killNumberName = "killNumber";
    }

    /**
     * 0未开始
     * 1开始
     * 2失败
     * 3胜利
     * 4暂停
     */
    public static Condition state;
    public static SomeListenValue someListenValue;
    /**
     * 是否已经开启的子弹定时器
     */
    public static List<TankAbstract> isBeginBullet;
    /**
     * 循环次数
     */
    public static AtomicInteger numberOfCycles;
    /**
     * 开始时的循环次数
     */
    public static AtomicInteger beginNumberOfCycles;
    /**
     * 所有物体
     */
    public static List<TankAbstract> superList;
    /**
     * 出界或者没用的物体
     */
    public static List<TankAbstract> uselessList;
    /**
     * 子弹
     */
    public static List<TankAbstract> heroBulletList;
    /**
     * 敌方子弹
     */
    public static List<TankAbstract> enemyTankBulletList;
    /**
     * 敌方坦克
     */
    public static List<TankAbstract> enemyTankList;
    /**
     * 还有多少子弹
     */
    public static AtomicInteger surplusHeroBulletNumber;

    /**
     * 存放敌方坦克和定时器的集合
     */
    public static Map<TankAbstract, List<javax.swing.Timer>> tankDataTimerMap;
    public static String killNumberName;
    private GameCommonData() {

    }
}
