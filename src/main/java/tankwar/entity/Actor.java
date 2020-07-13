package tankwar.entity;//创建接口
import tankwar.enums.ActorType;

import java.awt.*;

public interface Actor{
	void draw(Graphics g);
	void move();
	ActorType getType();
	Rectangle getBorder();
	Rectangle[] getDetailedBorder();
	boolean wallDestroyed();
}