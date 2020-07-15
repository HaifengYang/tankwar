package tankwar.enums;

/**
 * 处于哪个方位
 */
public enum Orientation {
    /**
     * 上方
     */
    UP(0, "UP"),
    /**
     * 下方
     */
    DOWN(1, "DOWN"),
    /**
     * 左侧
     */
    LEFT(2, "LEFT"),
    /**
     * 右侧
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

    private Orientation(int value, String name) {
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
