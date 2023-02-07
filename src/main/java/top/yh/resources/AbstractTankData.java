package top.yh.resources;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import top.yh.utils.Direction;
import top.yh.utils.GetImage;
import top.yh.utils.GetProperties;
import top.yh.utils.SerialCloneable;

import java.awt.*;
import java.util.Map;

/**
 * @author yuhao
 * @date 2023/2/4
 * 坦克数据
 **/
@Getter
@Setter
@ToString
public abstract class AbstractTankData extends SerialCloneable{
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    /**
     * x y坐标
     */
    private int x;
    private int y;
    /**
     * 大小
     * width
     * height
     */
    private int width;
    private int height;
    /**
     * 速度
     */
    private int speed;
    /**
     * 方向
     */
    private Direction direction;
    /**
     * 图片
     */
    private Image[] images;
    private String filePath;

    public AbstractTankData(String filePath) {
        this.filePath = filePath;
        initData();
    }
    private Map<String, String> tankDataMap;
    /**
     * 初始化数据
     */
    private void initData() {
        //map数据
        tankDataMap = GetProperties.getAllProperties(filePath);
        x = Integer.parseInt(tankDataMap.get("x"));
        y = Integer.parseInt(tankDataMap.get("y"));
        width = Integer.parseInt(tankDataMap.get("width"));
        height = Integer.parseInt(tankDataMap.get("height"));
        speed = Integer.parseInt(tankDataMap.get("speed"));
        direction = Direction.changeOfString(tankDataMap.get("direction").toUpperCase());
        images = new Image[Direction.values().length];
        for (int i = 0; i < images.length; i++) {
            images[i] = GetImage.getImage(tankDataMap.get("image" + i));
        }
    }

    /**
     * 绘画
     * @param graphics 画笔
     */
    public abstract void byImage(Graphics graphics) ;
    /**
     * 获取自身矩形的方法 用于碰撞测试
     */
    public Rectangle getRec() {
        return new Rectangle(x, y, width, height);
    }
}
