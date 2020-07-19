package tankwar.enums;

public enum BulletSpeed {
    NORMAL(7), ENHANCED(12);

    private int type;

    BulletSpeed(int type)
    {
        this.type = type;
    }

    public int type()
    {
        return type;
    }
}
