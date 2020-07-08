package single.enums;

/**
 * 方向
 */
public enum Direction {
    /**
     * 上
     */
    UP(0, "UP"),
    /**
     * 下
     */
    DOWN(1, "DOWN"),
    /**
     * 左
     */
    LEFT(2, "LEFT"),
    /**
     * 右
     */
    RIGHT(3, "RIGHT");

    /**
     * 值
     */
    private final int value;
    /**
     * 名字
     */
    private final String defaultName;

    private Direction(int value, String name) {
        this.value = value;
        this.defaultName = name;
    }

    public int value() {
        return value;
    }

    public String defaultName() {
        return defaultName;
    }
}
