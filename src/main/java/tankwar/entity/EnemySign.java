package tankwar.entity;

import tankwar.constant.ActorType;
import tankwar.model.Model;

import java.awt.*;

public class EnemySign implements Actor {
    private int xPos;
    private int yPos;
    public Rectangle border;
    private final Image texture = Toolkit.getDefaultToolkit().getImage(Model.class.getClassLoader()
            .getResource("image/" + 31 + ".jpg"));


    public EnemySign(int x, int y){
        this.xPos = x;
        this.yPos = y;
        this.border = new Rectangle(-1,-1,-1,-1);
    }

    @Override
    public void setXPos(int xPos) {
        this.xPos = xPos;
    }

    @Override
    public void setYPos(int yPos) {
        this.yPos = yPos;
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
