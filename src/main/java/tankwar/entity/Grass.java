package tankwar.entity;

import tankwar.enums.ActorType;

import java.awt.*;

/**
 * 草丛类
 */
public class Grass implements Actor{
	private final int xPos;
	private final int yPos;
	private final Rectangle border;

	public Grass(int a, int b){
		xPos = a;
		yPos = b;
		border = new Rectangle(0,0,0,0);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 225, 0));
		for(int i = yPos - 11; i <= yPos + 12; i+=5)
			g.drawLine(xPos - 12, i, xPos + 12, i);
		for(int i = xPos - 11; i <= xPos + 12; i+=5)
			g.drawLine(i, yPos - 12, i, yPos + 12);
		g.setColor(new Color(0, 128, 0));
		for(int i = yPos - 10; i <= yPos + 12; i+=5)
			g.drawLine(xPos - 12, i, xPos + 12, i);
		for(int i = xPos - 10; i <= xPos + 12; i+=5)
			g.drawLine( i, yPos - 12, i, yPos + 12);
	}

	public ActorType getType(){
		return ActorType.GRASS;
	}

	public Rectangle getBorder(){
		return border;
	}

	public void move(){}
	public Rectangle[] getDetailedBorder(){return null;}
	public boolean wallDestroyed(){return false;}


}