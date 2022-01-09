package tankwar.entity;//创建接口
import tankwar.constant.ActorType;

import java.awt.*;

public interface Actor{
	void draw(Graphics g);
	void move();
	ActorType getType();
	Rectangle getBorder();
	Rectangle[] getDetailedBorder();
	boolean wallDestroyed();
	default void setXPos(int xPos){}
	default void setYPos(int yPos){}
}