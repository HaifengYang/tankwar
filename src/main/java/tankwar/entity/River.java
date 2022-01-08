package tankwar.entity;

import tankwar.model.Model;
import tankwar.constant.ActorType;

import java.awt.*;

/**
 * 河流类
 */
public class River implements Actor{
	private final int xPos;
	private final int yPos;
	private final Rectangle Border;
	private final Image river;

	public River(int xPos, int yPos, Model gameModel){
		river = gameModel.textures[71];
		this.xPos = xPos;
		this.yPos = yPos;
		Border = new Rectangle(this.xPos - 12, this.yPos - 12, 25, 25);
	}

	public Rectangle getBorder(){
		return Border;
	}

	public ActorType getType(){
		return ActorType.RIVER;
	}

	public void draw(Graphics g){
		g.drawImage(river, xPos - 12, yPos - 12, null);
	}

	//未使用的方法
	public void move(){}
	public Rectangle[] getDetailedBorder(){return null;}
	public boolean wallDestroyed(){return false;}

}

