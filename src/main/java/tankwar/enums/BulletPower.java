package tankwar.enums;

import tankwar.entity.Bullet;

public enum BulletPower {
    NORMAL(1), ENHANCED(2);

    private final int type;

    BulletPower(int type){
        this.type = type;
    }

    public int type() {
        return type;
    }
}
