package single.entity;//创建接口
import single.enums.ActorType;

import java.awt.*;

public interface Actor{
	void draw(Graphics g);
	void move();
	ActorType getType();
	Rectangle getBorder();
	Rectangle[] getDetailedBorder();
	boolean wallDestroyed();
}