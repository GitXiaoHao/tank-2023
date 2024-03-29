package top.yh.obj;


import lombok.SneakyThrows;
import top.yh.resources.GameCommonData;
import top.yh.resources.TankAbstract;
import top.yh.utils.Condition;
import top.yh.utils.Direction;
import top.yh.utils.GetProperties;
import top.yh.utils.PropertiesName;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * 作者 小豪<p>
 * 时间 2022/11/27<p>
 *
 * @author yu<p>
 * 超类
 */
public class Super extends TankAbstract {
    private static final int WINDOWS_WIDTH = Integer.parseInt(GetProperties.getSpecificData(
            PropertiesName.GAME_VIEW_PATH, "windowsWidth"));
    private static final int WINDOWS_HEIGHT = Integer.parseInt(GetProperties.getSpecificData(
            PropertiesName.GAME_VIEW_PATH, "windowsHeight"));

    public Super(String filePath) {
        super(filePath);
    }

    public void byImage(Graphics graphics) {
        //画
        graphics.drawImage(getImages()[getDirection().ordinal()], getX(), getY(), getWidth(), getHeight(), null);
        //移动
        switch (getDirection()) {
            //上
            case UP -> setY(getY() - getSpeed());
            //下
            case DOWN -> setY(getY() + getSpeed());
            //左
            case LEFT -> setX(getX() - getSpeed());
            //右
            case RIGHT -> setX(getX() + getSpeed());
            default -> {
            }
        }
    }

    public static class Hero extends Super {

        public Hero(String filePath) {
            super(filePath);
        }

        @Override
        public void byImage(Graphics graphics) {
            //判断
            if (this.getX() + this.getWidth() >= WINDOWS_WIDTH) {
                //向右超出了界限
                this.setX(this.getX() - this.getSpeed());
            } else if (this.getX() <= 0) {
                //向左超出了界限
                this.setX(WINDOWS_WIDTH - this.getWidth());
            }
            if (this.getY() + this.getHeight() >= WINDOWS_HEIGHT) {
                this.setY(WINDOWS_HEIGHT - this.getHeight());
            } else if (this.getY() <= 0) {
                this.setY(0);
            }
            for (int i = 0; i < GameCommonData.enemyTankBulletList.size(); i++) {
                TankAbstract data = GameCommonData.enemyTankBulletList.get(i);
                if (data.getRec().intersects(this.getRec())) {
                    //碰到了
                    //变为失败
                    GameCommonData.state = Condition.Fail;
                    //敌方子弹移除
                    GameCommonData.enemyTankBulletList.remove(data);
                    //加入到没用的物体中
                    GameCommonData.uselessList.add(data);
                    GameCommonData.uselessList.add(this);
                }
            }
            //画
            graphics.drawImage(getImages()[getDirection().ordinal()], getX(), getY(), getWidth(), getHeight(), null);
        }
    }

    public static class BackImage extends Super {

        public BackImage(String filePath) {
            super(filePath);
        }

        @Override
        public void byImage(Graphics graphics) {
            //判断是否出界
            if (this.getX() >= 0 || this.getX() <= (WINDOWS_WIDTH - this.getWidth())) {
                this.setX(Integer.parseInt(this.getTankDataMap().get("x")));
            }
            if (this.getY() >= 0 || this.getY() <= (WINDOWS_HEIGHT - this.getHeight())) {
                this.setY(Integer.parseInt(this.getTankDataMap().get("y")));
            }
            super.byImage(graphics);
        }
    }

    public static class HeroBullet extends Super {
        public HeroBullet(String filePath) {
            super(filePath);
        }

        @Override
        public void byImage(Graphics graphics) {
            super.byImage(graphics);
            if (this.getX() + this.getWidth() <= 0 ||
                    this.getX() - this.getWidth() >= WINDOWS_WIDTH ||
                    this.getY() + this.getHeight() <= 0 ||
                    this.getY() - this.getHeight() >= WINDOWS_HEIGHT ||
                    this.getDirection() == Direction.DEFAULT ||
                    this.getSpeed() == 0) {
                //丢掉
                GameCommonData.uselessList.add(this);
                GameCommonData.heroBulletList.remove(this);
                //子弹++
                GameCommonData.surplusHeroBulletNumber.decrementAndGet();
            }

        }
    }

    public static class EnemyTank extends Super {

        public EnemyTank(String filePath) {
            super(filePath);
        }

        @Override
        @SneakyThrows
        public void byImage(Graphics graphics) {
            //如果方向错误
            if (this.getDirection() == Direction.DEFAULT) {
                int i = new Random().nextInt(Direction.values().length - 2);
                this.setDirection(Direction.changeOfString(i + ""));
            }
            for (int i = 0; i < GameCommonData.heroBulletList.size(); i++) {
                TankAbstract data = GameCommonData.heroBulletList.get(i);
                if (data.getRec().intersects(this.getRec())) {
                    //如果相撞
                    //丢掉
                    GameCommonData.uselessList.add(data);
                    GameCommonData.uselessList.add(this);
                    GameCommonData.heroBulletList.remove(data);
                    GameCommonData.enemyTankList.remove(this);
                    List<Timer> timerList = GameCommonData.tankDataTimerMap.get(this);
                    timerList.forEach(Timer::stop);
                    GameCommonData.tankDataTimerMap.remove(this);
                    GameCommonData.surplusHeroBulletNumber.decrementAndGet();
                    //消灭敌人的数量++
                    GameCommonData.someListenValue.setKillNumber(GameCommonData.someListenValue.getKillNumber() + 1);
                }
            }
            super.byImage(graphics);
        }
    }

    public static class EnemyTankBullet extends Super {

        public EnemyTankBullet(String filePath) {
            super(filePath);
        }

        @Override
        public void byImage(Graphics graphics) {
            if (this.getX() + this.getWidth() <= 0 ||
                    this.getX() - this.getWidth() >= WINDOWS_WIDTH ||
                    this.getY() + this.getHeight() <= 0 ||
                    this.getY() - this.getHeight() >= WINDOWS_HEIGHT ||
                    this.getDirection() == Direction.DEFAULT ||
                    this.getSpeed() == 0) {
                //移除
                GameCommonData.enemyTankBulletList.remove(this);
                GameCommonData.uselessList.add(this);
            }
            super.byImage(graphics);
        }
    }
}
