package top.yh.listen;

import lombok.Getter;
import lombok.SneakyThrows;
import top.yh.utils.PropertiesName;
import top.yh.obj.Super;
import top.yh.resources.TankAbstract;
import top.yh.resources.GameCommonData;
import top.yh.timer.AddTimer;
import top.yh.utils.Condition;
import top.yh.utils.Direction;
import top.yh.utils.GetProperties;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * @author yuhao
 * @date 2023/2/4
 **/
public class SuperListen {
    /**
     * 添加计时器
     */
    @Getter
    private AddTimer addTimer;
    /**
     * 开始时的资源
     */
    private BeginResources beginResources;
    /**
     * 英雄一共多少子弹
     */
    private int heroBulletNumber;
    private int enemyNumber;
    private HeroAndImageListen heroAndImageListen;
    public void heroAndImageListen(InputEvent e) {
        if (heroAndImageListen == null) {
            heroAndImageListen = new HeroAndImageListen();
        }
        heroAndImageListen.heroAndImageListen((KeyEvent) e);
    }

    public void notStartInitData() {
        beginResources = new BeginResources();
        addTimer = new AddTimer();
        GameCommonData.superList.add(beginResources.backImage);
        GameCommonData.superList.add(beginResources.hero);
    }

    /**
     * 一直刷新坦克
     */
    public void timerForFlushEnemyTank() {
        //开启定时器
        addTimer.forFlushEnemyTankStart(Integer.parseInt(beginResources.enemyTank.getTankDataMap().get("tankDistance")), beginResources.enemyTank);
        //一共要杀多少个
        enemyNumber = Integer.parseInt(beginResources.enemyTank.getTankDataMap().get("enemyNumber"));
    }

    public void stopForFlushEnemyTank(int killNumber) {
        if (killNumber >= this.enemyNumber) {
            GameCommonData.state = Condition.Win;
            addTimer.forFlushEnemyTankStop();
        }
    }

    public void timerForEnemyTankBullet(TankAbstract tank) {
        this.addTimer.forEnemyTankBulletStart(tank, this.beginResources.enemyTankBullet);
    }

    /**
     * 换方向 查看是否已经出界
     *
     * @param enemyTank 敌方坦克
     */
    public void timerForEnemyTankIsInspect(TankAbstract enemyTank) {
        this.addTimer.forEnemyTankInspectAndExchangeDirection(enemyTank, 3100);
    }


    public void timerForGameRepaint(int flushMinus) {
        this.addTimer.forGameViewRepaintStart(flushMinus);
    }

    public void stopForGameRepaint() {
        this.addTimer.forGameViewRepaintStop();
    }

    private class HeroAndImageListen {
        @SneakyThrows
        private void heroAndImageListen(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    for (TankAbstract data : GameCommonData.superList) {
                        if (!(data instanceof Super.Hero)) {
                            data.setY(data.getY() + data.getSpeed());
                        }
                    }
                    //上
                    beginResources.hero.setY(beginResources.hero.getY() - beginResources.hero.getSpeed());
                    beginResources.hero.setDirection(Direction.UP);
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    //下
                    beginResources.hero.setY(beginResources.hero.getY() + beginResources.hero.getSpeed());
                    beginResources.hero.setDirection(Direction.DOWN);
                    for (TankAbstract data : GameCommonData.superList) {
                        if (!(data instanceof Super.Hero)) {
                            data.setY(data.getY() - data.getSpeed());
                        }
                    }
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    //左
                    beginResources.hero.setX(beginResources.hero.getX() - beginResources.hero.getSpeed());
                    beginResources.hero.setDirection(Direction.LEFT);
                    for (TankAbstract data : GameCommonData.superList) {
                        if (!(data instanceof Super.Hero)) {
                            data.setX(data.getX() + data.getSpeed());
                        }
                    }
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    //右
                    beginResources.hero.setX(beginResources.hero.getX() + beginResources.hero.getSpeed());
                    beginResources.hero.setDirection(Direction.RIGHT);
                    for (TankAbstract data : GameCommonData.superList) {
                        if (!(data instanceof Super.Hero)) {
                            data.setX(data.getX() - data.getSpeed());
                        }
                    }
                    break;
                case KeyEvent.VK_J:
                case KeyEvent.VK_1:
                    //判断是否还有子弹
                    if (GameCommonData.surplusHeroBulletNumber.get() <= heroBulletNumber) {
                        GameCommonData.surplusHeroBulletNumber.getAndIncrement();
                        TankAbstract bullet = (Super.HeroBullet) beginResources.heroBullet.clone();
                        bullet.setX(beginResources.hero.getX() + 15);
                        bullet.setY(beginResources.hero.getY() + 15);
                        bullet.setDirection(beginResources.hero.getDirection());
                        GameCommonData.superList.add(bullet);
                        GameCommonData.heroBulletList.add(bullet);
                    }
                    break;
                default:
            }

        }
    }

    /**
     * 开始时的资源
     */
    private class BeginResources {
        private final TankAbstract hero;
        private final TankAbstract backImage;
        private final TankAbstract heroBullet;
        private final TankAbstract enemyTank;
        private final TankAbstract enemyTankBullet;

        private BeginResources() {
            hero = new Super.Hero(PropertiesName.HERO_PATH);
            enemyTank = new Super.EnemyTank(PropertiesName.ENEMY_TANK_PATH);
            backImage = new Super.BackImage(PropertiesName.BACK_IMAGE_PATH);
            heroBullet = new Super.HeroBullet(PropertiesName.HERO_BULLET_PATH);
            enemyTankBullet = new Super.EnemyTankBullet(PropertiesName.ENEMY_TANK_BULLET_PATH);
            heroBulletNumber = Integer.parseInt(GetProperties.getSpecificData(PropertiesName.HERO_BULLET_PATH, "number"));
        }
    }
}
