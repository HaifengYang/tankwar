package tankwar.entity;

import tankwar.model.Model;
import tankwar.enums.ActorType;

import java.awt.*;

/**
 * 增强类
 */
public class PowerUp implements Actor{
	private final int xPos;
	private final int yPos;
	private int function;
	private final Rectangle border;
	private int displayTime;
	private final Image[] textures;
	private final Model gameModel;

	public PowerUp(Model gameModel){
		this.gameModel = gameModel;
		//加载图像
		textures = new Image[7];
		for(int i = 0; i < 7; i ++)
			textures[i] = gameModel.textures[46+i];

		xPos = 24 + (int)(Math.random()*475);
		yPos = 24 + (int)(Math.random()*475);
		int a = (int)(Math.random()*21);
		if(0 <= a && a< 3)
			function = 0;
		if(3 <= a && a < 6)
			function = 1;
		if(6 <= a && a < 9)
			function = 2;
		if(9 <= a && a< 12)
			function = 3;
		if(12 <= a && a < 15)
			function = 4;
		if(15 <= a && a < 18)
			function = 5;
		if(18 <= a && a < 21)
			function = 6;
		displayTime = 300 + (int)(Math.random()*630);
		border= new Rectangle(xPos - 12, yPos -12, 25, 25);
	}

	public Rectangle getBorder(){
		return border;
	}

	public int getFunction(){
		return function;
	}

	public ActorType getType(){
		return ActorType.POWER_UP;
	}

	public void move(){
		displayTime--;
		if(displayTime == 0)
			gameModel.removeActor(this);
	}

	public void draw(Graphics g){
		g.drawImage(textures[function], xPos - 12, yPos - 12, null);
	}

	//未使用的方法
	public Rectangle[] getDetailedBorder(){return null;}
	public boolean wallDestroyed(){return false;};

}