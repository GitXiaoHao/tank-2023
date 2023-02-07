package top.yh.timer;

import top.yh.PropertiesName;
import top.yh.obj.Super;
import top.yh.resources.AbstractTankData;
import top.yh.resources.GameCommonData;
import top.yh.resources.ViewCommonData;
import top.yh.utils.Direction;
import top.yh.utils.GetProperties;

import javax.swing.Timer;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuhao
 * @date 2023/2/5
 * 定时器
 **/
public class AddTimer {
    /**
     * 存放计时器
     */
    private final Set<Timer> timerSet;
    /**
     * 游戏界面重绘
     */
    private FlushGameView flushGameView;
    /**
     * 刷新敌方坦克
     */
    private FlushEnemyTank flushEnemyTank;
    /**
     * 敌方每个坦克的子弹刷新
     */
    private FlushEnemyTankBullet flushEnemyTankBullet;
    private FlushEnemyTankInspectAndExchangeDirection flushEnemyTankInspectAndExchangeDirection;

    /**
     * 构造方法
     */
    public AddTimer() {
        timerSet = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * 判断是否出界
     *
     * @param enemyTank 当前的这个坦克
     * @return 如果出界返回true 没有出界返回false
     */
    private static synchronized boolean outside(AbstractTankData enemyTank) {
        if (enemyTank.getX() + enemyTank.getWidth() <= 0) {
            //如果在左边
            //就一直往右走
            enemyTank.setDirection(Direction.RIGHT);
            return true;
        } else if (enemyTank.getX() + enemyTank.getWidth() >= Integer.parseInt(GetProperties.getSpecificData(
                PropertiesName.GAME_VIEW_PATH, "windowsWidth"))) {
            //如果在右边
            //就一直往左走
            enemyTank.setDirection(Direction.LEFT);
            return true;
        }
        if (enemyTank.getY() + enemyTank.getHeight() <= 0) {
            //如果在上边
            //就一直往下走
            enemyTank.setDirection(Direction.DOWN);
            return true;
        } else if (enemyTank.getY() - enemyTank.getHeight() >= Integer.parseInt(GetProperties.getSpecificData(
                PropertiesName.GAME_VIEW_PATH, "windowsHeight"))) {
            //如果在下边 就一直往上走
            enemyTank.setDirection(Direction.UP);
            return true;
        }
        return false;
    }

    /**
     * 游戏窗体的开始刷新定时器
     *
     * @param delay 刷新的时间
     */
    public void forGameViewRepaintStart(int delay) {
        if (flushGameView == null) {
            flushGameView = new FlushGameView();
            GameCommonData.someListenValue.setKillNumber(0);
        }
        this.timerSet.add(flushGameView.start(delay));
    }

    /**
     * 游戏窗体刷新暂停
     */
    public void forGameViewRepaintStop() {
        if (flushGameView != null) {
            flushGameView.stop();
        }
    }

    /**
     * 刷新坦克
     *
     * @param tankDistance
     * @param enemyTank
     */
    public void forFlushEnemyTankStart(int tankDistance, AbstractTankData enemyTank) {
        if (flushEnemyTank == null) {
            flushEnemyTank = new FlushEnemyTank(tankDistance, enemyTank);
            timerSet.add(flushEnemyTank.start());
        }
    }

    public void forFlushEnemyTankStop() {
        this.flushEnemyTank.stop();
    }

    /**
     * 给每一个敌方坦克都加一个定时器
     * 判断是否出界并且换方向
     */
    public void forEnemyTankInspectAndExchangeDirection(AbstractTankData enemyTank, int delay) {
        if (flushEnemyTankInspectAndExchangeDirection == null) {
            flushEnemyTankInspectAndExchangeDirection = new FlushEnemyTankInspectAndExchangeDirection();
        }
        timerSet.add(flushEnemyTankInspectAndExchangeDirection.start(enemyTank, delay));
    }

    /**
     * 每一个坦克都进行射击
     *
     * @param tank
     * @param enemyTankBullet
     */
    public void forEnemyTankBulletStart(AbstractTankData tank, AbstractTankData enemyTankBullet) {
        if (this.flushEnemyTankBullet == null) {
            flushEnemyTankBullet = new FlushEnemyTankBullet();
        }
        timerSet.add(this.flushEnemyTankBullet.start(tank, enemyTankBullet));
    }

    /**
     * 判断坦克出界和换方向
     */
    private static class FlushEnemyTankInspectAndExchangeDirection {
        public Timer start(AbstractTankData enemyTank, int delay) {
            //开启定时器
            //看看越界没 并且更换方向
            Timer timer = new Timer(delay, (e) -> {
                boolean outside = false;
                if (!outside) {
                    //没有出界
                    //换方向
                    //随机产生方向
                    int nextInt = new Random().nextInt(Direction.values().length);
                    enemyTank.setDirection(Direction.changeOfString(String.valueOf(nextInt)));
                    ViewCommonData.gameView.repaint();
                }
            });
            timer.start();
            //加入定时器
            List<Timer> list = GameCommonData.tankDataTimerMap.get(enemyTank);
            list.add(timer);
            GameCommonData.tankDataTimerMap.put(enemyTank, list);
            return timer;
        }

    }

    private static class FlushEnemyTank {
        /**
         * 初始化的距离
         */
        private final AtomicInteger size;
        /**
         * 每个敌方坦克的距离
         */
        private final int tankDistance;

        private final AbstractTankData enemyTank;
        private Timer enemyTankTimer;

        public FlushEnemyTank(int tankDistance, AbstractTankData enemyTank) {
            this.size = new AtomicInteger(0);
            this.tankDistance = tankDistance;
            this.enemyTank = enemyTank;
        }

        public Timer start() {
            if (enemyTankTimer == null) {
                enemyTankTimer = new Timer(Integer.parseInt(GetProperties.getSpecificData(PropertiesName.ENEMY_TANK_PATH, "flushMinus")), e -> {
                    AbstractTankData tank = (Super.EnemyTank) enemyTank.clone();
                    tank.setX(tank.getX() + (tankDistance * (size.getAndIncrement())));
                    GameCommonData.superList.add(tank);
                    GameCommonData.enemyTankList.add(tank);
                });
            }
            if (!enemyTankTimer.isRunning()) {
                enemyTankTimer.start();
            }
            return enemyTankTimer;
        }

        public void stop() {
            if (this.enemyTankTimer != null) {
                enemyTankTimer.stop();
            }
        }
    }

    private static class FlushEnemyTankBullet {
        public Timer start(AbstractTankData tank, AbstractTankData enemyTankBullet) {
            Timer enemyTankBulletTimer = new Timer(Integer.parseInt(GetProperties.getSpecificData(PropertiesName.ENEMY_TANK_BULLET_PATH, "flushMinus")), e1 -> {
                if (!outside(tank)) {
                    //判断是否已经出界
                    //没有出界
                    Super.EnemyTankBullet tankBullet = (Super.EnemyTankBullet) enemyTankBullet.clone();
                    tankBullet.setX(tank.getX() + 18);
                    tankBullet.setY(tank.getY() + 15);
                    tankBullet.setDirection(tank.getDirection());
                    GameCommonData.superList.add(tankBullet);
                    GameCommonData.enemyTankBulletList.add(tankBullet);
                }
            });
            enemyTankBulletTimer.start();
            //加入集合 方便关掉定时器
            List<Timer> list = Collections.synchronizedList(new ArrayList<>());
            list.add(enemyTankBulletTimer);
            GameCommonData.tankDataTimerMap.put(tank, list);
            return enemyTankBulletTimer;
        }

    }

    /**
     * 游戏界面的重绘
     */
    private static class FlushGameView {
        private Timer timer;

        /**
         * 重绘游戏界面
         *
         * @param delay
         */
        public Timer start(int delay) {
            if (this.timer == null) {
                timer = new Timer(delay, (e) -> {
                    ViewCommonData.gameView.repaint();
                    int value = GameCommonData.numberOfCycles.incrementAndGet();
                    if (value == Integer.MAX_VALUE) {
                        GameCommonData.numberOfCycles.set(1);
                    }
                });
            }
            //如果没有开始
            if (!timer.isRunning()) {
                timer.start();
            }
            return timer;
        }

        public void stop() {
            if (this.timer != null) {
                if (this.timer.isRunning()) {
                    this.timer.stop();
                }
            }
        }
    }
}
