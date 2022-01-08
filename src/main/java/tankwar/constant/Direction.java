package tankwar.constant;

/**
 * 方向
 */
public enum Direction {
    /**
     * 上
     */
    UP(0),
    /**
     * 下
     */
    DOWN(1),
    /**
     * 左
     */
    LEFT(2),
    /**
     * 右
     */
    RIGHT(3);

    /**
     * 值
     */
    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
