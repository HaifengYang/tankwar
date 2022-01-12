package tankwar.entity;

import tankwar.constant.ActorType;
import tankwar.model.Model;

import java.awt.*;

public class EnemySign implements Actor {
    private final int xPos;
    private final int yPos;
    public Rectangle border;
    private final Image texture = Toolkit.getDefaultToolkit().getImage(Model.class.getClassLoader()
            .getResource("image/" + 55 + ".jpg"));


    public EnemySign(int x, int y){
        this.xPos = x;
        this.yPos = y;
        this.border = new Rectangle(-1,-1,-1,-1);
    }

    public void draw(Graphics g) {
        g.drawImage(texture, xPos, yPos, null);
    }

    @Override
    public void move() {

    }

    @Override
    public ActorType getType() {
        return null;
    }

    @Override
    public Rectangle getBorder() {
        return border;
    }

    @Override
    public Rectangle[] getDetailedBorder() {
        return new Rectangle[0];
    }

    @Override
    public boolean wallDestroyed() {
        return false;
    }
}
