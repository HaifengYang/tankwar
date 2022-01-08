package tankwar.entity;

import tankwar.model.Model;
import tankwar.constant.ActorType;

import java.awt.*;
import java.util.Random;

/**
 * 增强类
 */
public class PowerUp implements Actor{
	private final int xPos;
	private final int yPos;
	private final int function;
	private final Rectangle border;
	private int displayTime;
	private final Image[] textures;
	private final Model gameModel;

	public PowerUp(Model gameModel){
		this.gameModel = gameModel;
		//加载图像
		textures = new Image[7];
		System.arraycopy(gameModel.textures, 46, textures, 0, 7);

		xPos = 24 + new Random().nextInt(475);
		yPos = 24 + new Random().nextInt(475);
		int a =  new Random().nextInt(21);
		if(a< 3)
			function = 0;
		else if(a < 6)
			function = 1;
		else if(a < 9)
			function = 2;
		else if(a< 12)
			function = 3;
		else if(a < 15)
			function = 4;
		else if(a < 18)
			function = 5;
		else
			function = 6;
		displayTime = 300 + new Random().nextInt(300);
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
	public boolean wallDestroyed(){return false;}

}