package tankwar.entity;

import tankwar.constant.Direction;
import tankwar.model.Model;
import tankwar.constant.ActorType;

import java.awt.*;

/**
 * 石墙类
 */
public class SteelWall implements Actor{
	private final int xPos;
	private final int yPos;
	private final Rectangle[] border = new Rectangle[4];
	private final boolean[] shadow = new boolean[4];
	private boolean wallDestroyed;
	private boolean bulletDestroyed;
	private final Image steelWall;
	private final Rectangle generalBorder;

	public SteelWall(int xPos, int yPos, Model gameModel){
		steelWall = gameModel.textures[53];
		this.xPos = xPos;
		this.yPos = yPos;
		generalBorder = new Rectangle(this.xPos - 12, this.yPos - 12, 25, 25);
		border[0] = new Rectangle(this.xPos - 11, this.yPos - 11, 11, 11);
		border[1] = new Rectangle(this.xPos + 1, this.yPos - 11, 11, 11);
		border[2] = new Rectangle(this.xPos - 11, this.yPos + 1, 11, 11);
		border[3] = new Rectangle(this.xPos + 1, this.yPos + 1, 11, 11);
	}

	public SteelWall(int xPos, int yPos, int orientation, Model gameModel){
		this.xPos = xPos;
		this.yPos = yPos;
		steelWall = gameModel.textures[53];
		generalBorder = new Rectangle(this.xPos - 12, this.yPos - 12, 25, 25);
		if(orientation == Direction.UP.value()){
			//左上角
			border[0] = new Rectangle(this.xPos - 11, this.yPos - 11, 11, 11);
			//右上角
			border[1] = new Rectangle(this.xPos + 1, this.yPos - 11, 11, 11);

				shadow[2] = true;
				shadow[3] = true;
		}
		if(orientation == Direction.DOWN.value()){
			//左下角
			border[2] = new Rectangle(this.xPos - 11, this.yPos + 1, 11, 11);
			//右下角
			border[3] = new Rectangle(this.xPos + 1, this.yPos + 1, 11, 11);
				shadow[0] = true;
				shadow[1] = true;
		}
		if(orientation == Direction.LEFT.value()){
			//左上角
			border[0] = new Rectangle(this.xPos - 11, this.yPos - 11, 11, 11);
			//左下角
			border[2] = new Rectangle(this.xPos - 11, this.yPos + 1, 11, 11);
				shadow[1] = true;
				shadow[3] = true;
		}
		if(orientation == Direction.RIGHT.value()){
			//右上角
			border[1] = new Rectangle(this.xPos + 1, this.yPos - 11, 11, 11);
			//右下角
			border[3] = new Rectangle(this.xPos + 1, this.yPos + 1, 11, 11);
				shadow[0] = true;
				shadow[2] = true;
		}
	}


	public void damageWall(Rectangle bullet, int bulletpower){
		bulletDestroyed = false;
		if(bulletpower == 2){
			for(int i = 0; i < 4; i++){
				if(border[i] != null){
					if(bullet.intersects(border[i])){
						bulletDestroyed = true;
						border[i] = null;
						shadow[i] = true;
					}
				}
			}
		}
		if(bulletpower == 1){
			for(int i = 0; i < 4; i++){
				if(border[i] != null){
					if(bullet.intersects(border[i]))
						bulletDestroyed = true;
				}
			}
		}
	}

	public boolean wallDestroyed(){
		if(wallDestroyed) return true;
		if(border[0] == null && border[1] == null && border[2] == null && border[3] == null)
			wallDestroyed = true;
		return wallDestroyed;
	}

	public Rectangle getBorder(){
		return generalBorder;
	}

	public Rectangle[] getDetailedBorder(){
			return border;
	}

	public void draw(Graphics g) {
		if(wallDestroyed)
			return;

		g.drawImage(steelWall, xPos - 12, yPos - 12, null);
		g.setColor(new Color(128, 64, 0));
		if(shadow[0])
			g.fillRect(xPos - 12, yPos - 12, 13, 13);
		if(shadow[1])
			g.fillRect(xPos, yPos - 12, 13, 13);
		if(shadow[2])
			g.fillRect(xPos - 12, yPos, 13, 13);
		if(shadow[3])
			g.fillRect(xPos, yPos, 13, 13);
	}

	public ActorType getType(){
		return ActorType.STEEL_WALL;
	}

	public boolean isBulletDestroyed() {
		return bulletDestroyed;
	}

	//未使用的方法

	public void move(){}
}