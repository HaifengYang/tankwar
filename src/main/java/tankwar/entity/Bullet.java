package tankwar.entity;

import tankwar.enums.BombType;
import tankwar.model.Model;
import tankwar.enums.ActorType;
import tankwar.enums.Direction;
import tankwar.utils.*;

import java.awt.*;

/**
 * 子弹类
 */
public class Bullet implements Actor{
	private final Rectangle map = new Rectangle(18, 18, 486, 486);
	private final Rectangle border;
	private final int direction;
	private final int speed;
	private final int bulletPower;
	private int xPos, yPos;
	private final Actor owner;
	private final Model gameModel;
	private boolean hitTarget;

	public Bullet(int xPos, int yPos, int direction, int speed, int bulletPower, Actor owner, Model gameModel){
		new AudioPlay(AudioUtil.FIRE).new AudioThread().start();

		this.owner = owner;
		this.gameModel = gameModel;
		this.xPos = xPos;
		this.yPos = yPos;
		this.direction = direction;
		if(this.direction == Direction.DOWN.value() || this.direction == Direction.UP.value()) {
			this.border = new Rectangle(xPos - 2, yPos - 5, 5, 13);
		}
		else {
			this.border = new Rectangle(xPos - 5, yPos - 2, 13, 5);
		}

		this.speed = speed;
		this.bulletPower = bulletPower;
	}

	public void draw(Graphics g) {
		g.setColor(Color.lightGray);
		if(direction == Direction.DOWN.value() || direction == Direction.UP.value())
			g.fillRect(border.x + 1, border.y +1, 3, 9);
		if(direction == Direction.LEFT.value() || direction == Direction.RIGHT.value())
			g.fillRect(border.x +1, border.y + 1, 9, 3);
	}

	public void move(){
		if(gameModel.gamePaused){
			return;
		}

		//检查这子弹是否撞击地图边界
		if(!border.intersects(map)){
			gameModel.removeActor(this);
			notifiyOwner();
			makeBomb();
			return;
		}
		//检查这颗子弹是否击中其他对象
		for(int i = 0; i < gameModel.actors.length; i++){
			if(gameModel.actors[i] != null){
				if(gameModel.actors[i] != this && gameModel.actors[i] != owner){
					if(border.intersects(gameModel.actors[i].getBorder())){

						if(gameModel.actors[i].getType()==ActorType.STEEL_WALL){
							SteelWall temp = (SteelWall)gameModel.actors[i];
							if(!temp.isWallDestroyed()){
								temp.damageWall(border, bulletPower);
								if(temp.isBulletDestroyed())
									hitTarget = true;
							}
						}else if(gameModel.actors[i].getType()==ActorType.WALL){
							Wall temp = (Wall)gameModel.actors[i];
							if(!temp.wallDestroyed()){
								temp.damageWall(border, bulletPower, direction);
								if(temp.wallDestroyed())
									hitTarget = true;
							}
						}else if(gameModel.actors[i].getType()==ActorType.BULLET){
							Bullet temp = (Bullet)gameModel.actors[i];
							if(temp.owner.getType()==ActorType.PLAYER){
								hitTarget = true;
								gameModel.removeActor(gameModel.actors[i]);
								temp.notifiyOwner();
							}
						}else if(gameModel.actors[i].getType()==ActorType.PLAYER){
							if(owner.getType()==ActorType.ENEMY){
								Player temp = (Player)gameModel.actors[i];
							    temp.hurt();
							}
							hitTarget = true;
						}else if(gameModel.actors[i].getType()==ActorType.ENEMY && owner.getType()==ActorType.PLAYER){
							Enemy temp = (Enemy)gameModel.actors[i];
							Player tempe = (Player)owner;
							if(temp.health == 0)
								tempe.setScores(tempe.getScores() + temp.type*100);
							temp.hurt();
							hitTarget = true;
						}else if(gameModel.actors[i].getType()==ActorType.BASE){
							Base temp = (Base)gameModel.actors[i];
							temp.doom();
							hitTarget = true;
							gameModel.gameOver = true;
						}
					}
				}
			}
		}

		//如果子弹打到其他对象,从游戏系统中删除这个子弹对象
		if(hitTarget){
			gameModel.removeActor(this);
			notifiyOwner();
			makeBomb();
			return;
		}

		if(direction == 0){
				border.y -= speed;
				yPos -= speed;
			}
			if(direction == 1){
				border.y += speed;
				yPos += speed;
			}
			if(direction == 2){
				border.x -= speed;
				xPos -= speed;
			}
			if(direction == 3){
				border.x += speed;
				xPos += speed;
		}
	}


	public Rectangle getBorder(){
		return border;
	}

	public ActorType getType(){
		return ActorType.BULLET;
	}

	public void notifiyOwner(){
			if(owner != null){
				if(owner.getType()==ActorType.PLAYER){
					Player temp = (Player)owner;
					temp.numberOfBulletIncrease();
				}else if(owner.getType()==ActorType.ENEMY){
					Enemy temp = (Enemy)owner;
					temp.numberOfBullet++;
				}
			}
	}

	public void makeBomb(){
		gameModel.addActor(new Bomb(xPos, yPos, BombType.SMALL , gameModel));
	}

	//未使用的方法
	public Rectangle[] getDetailedBorder(){return null;}
	public boolean wallDestroyed(){return false;}




}
