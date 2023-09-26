package top.yh.utils;

/**
 * @author yuhao
 * @date 2023/2/4
 **/
public enum Direction {
    /**
     * 上下左右
     */
    UP,
    DOWN,
    LEFT,
    RIGHT,
    DEFAULT;

    /**
     * @return 根据数字进行转化
     * 如果是数字的话
     * case 1: 上
     * case 2: 下
     * case 3: 左
     * case 4: 右
     * 如果是字符串的话
     * case up: 上
     * case down: 下
     * case left: 左
     * case right: 右
     */
    public static Direction changeOfString(String string){
        String regex = "^\\d$";
        String value = string.trim();
        if (value.matches(regex)) {
            //强转
            int dir = Integer.parseInt(value);
            return switch (dir) {
                case 0, 1 -> UP;
                case 2 -> DOWN;
                case 3 -> LEFT;
                case 4 -> RIGHT;
                default -> DEFAULT;
            };
        }
        if (UP.name().equalsIgnoreCase(value)) {
            return UP;
        } else if (DOWN.name().equalsIgnoreCase(value)) {
            return DOWN;
        } else if (LEFT.name().equalsIgnoreCase(value)) {
            return LEFT;
        } else if (RIGHT.name().equalsIgnoreCase(value)) {
            return RIGHT;
        }
        return DEFAULT;
    }
}
