package single.entity;

import single.model.Model;
import single.enums.ActorType;

import java.awt.*;

/**
 * 河流类
 */
public class River implements Actor{
	private int xPos;
	private int yPos;
	private Rectangle Border;
	public Image river;
	public Model gameModel;

	public River(int a, int b, Model gameModel){
		this.gameModel = gameModel;
		river = gameModel.textures[71];
		xPos = a;
		yPos = b;
		Border = new Rectangle(xPos - 12, yPos - 12, 25, 25);
	}

	public Rectangle getBorder(){
		return Border;
	}

	public int getXPos(){
		return xPos;
	}

	public int getYPos(){
		return yPos;
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

