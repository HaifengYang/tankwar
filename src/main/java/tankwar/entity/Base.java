package tankwar.entity;

import tankwar.enums.BombType;
import tankwar.enums.Orientation;
import tankwar.model.Model;
import tankwar.enums.ActorType;

import java.awt.*;

/**
 * 基地类
 */

public class Base implements Actor{
	private final Rectangle border;

	private Image image;
	private final int xPos;
	private final int yPos;
	private final Model gameModel;
	private int steelWallTime;
	private boolean baseKilled;

	public Base(Model gameModel){
		this.gameModel = gameModel;
		xPos = 260;
		yPos = 498;
		image = gameModel.textures[0];
		border = new Rectangle(xPos - 11, yPos - 11, 23, 23);

	}

	public Rectangle getBorder(){
		return border;
	}

	public void doom(){
		image = gameModel.textures[1];
		if(!baseKilled)
			gameModel.addActor(new Bomb(xPos, yPos, BombType.BIG, gameModel));
		baseKilled = true;
	}

	public void move(){
		if(steelWallTime == 600){
			SteelWall temp = new SteelWall(248, 498, Orientation.LEFT.value(), gameModel);
			gameModel.actors[0] = temp;

			temp = new SteelWall(273, 498, Orientation.RIGHT.value(), gameModel);
			gameModel.actors[1] = temp;

			temp = new SteelWall(248, 473, Orientation.DOWN.value(), gameModel);
			gameModel.actors[2] = temp;

			temp = new SteelWall(273, 473, Orientation.DOWN.value(), gameModel);
			gameModel.actors[3] = temp;
		}
		if(steelWallTime> 0)
			steelWallTime--;
		if(steelWallTime == 1){
			Wall temp = new Wall(248, 498,  Orientation.LEFT.value(), gameModel);
			gameModel.actors[0] = temp;

			temp = new Wall(273, 498, Orientation.RIGHT.value(), gameModel);
			gameModel.actors[1] = temp;

			temp = new Wall(248, 473, Orientation.DOWN.value(), gameModel);
			gameModel.actors[2] = temp;

			temp = new Wall(273, 473, Orientation.DOWN.value(), gameModel);
			gameModel.actors[3] = temp;
		}

	}


	public ActorType getType(){
		return ActorType.BASE;
	}

	public void draw(Graphics g){
		g.drawImage(image, xPos - 12, yPos - 12, null );
	}

	public void setSteelWallTime(int steelWallTime) {
		this.steelWallTime = steelWallTime;
	}

	//未使用的方法
	public Rectangle[] getDetailedBorder(){return null;}

	public boolean wallDestroyed(){return false;}

}