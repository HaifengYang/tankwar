package tankwar.entity;

import tankwar.enums.Direction;
import tankwar.enums.Orientation;
import tankwar.model.Model;
import tankwar.enums.ActorType;

import java.awt.*;

/**
 * 砖墙类
 */
public class Wall implements Actor{
	private final int xPos;
	private final int yPos;
	private final Rectangle[] border = new Rectangle[4];
	private final boolean[] shadow = new boolean[16];
	private boolean wallDestroyed;
	private boolean bulletDestroyed;
	private final Image image;
	private final Rectangle generalBorder;


	public Wall(int xPos, int yPos, Model gameModel){
		this.xPos = xPos;
		this.yPos = yPos;
		image = gameModel.textures[70];
		generalBorder = new Rectangle(this.xPos - 12, this.yPos - 12, 25, 25);
		border[0] = new Rectangle(this.xPos - 11, this.yPos - 11, 11, 11);
		border[1] = new Rectangle(this.xPos + 1, this.yPos - 11, 11, 11);
		border[2] = new Rectangle(this.xPos - 11, this.yPos + 1, 11, 11);
		border[3] = new Rectangle(this.xPos + 1, this.yPos + 1, 11, 11);

	}

	public Wall(int xPos, int yPos, int orientation, Model gameModel){
		this.xPos = xPos;
		this.yPos = yPos;
		image = gameModel.textures[70];
		generalBorder = new Rectangle(this.xPos - 12, this.yPos - 12, 25, 25);
		if(orientation == Orientation.UP.value()){
			border[0] = new Rectangle(this.xPos - 11, this.yPos - 11, 11, 11);
			border[1] = new Rectangle(this.xPos + 1, this.yPos - 11, 11, 11);
			for(int i = 8; i < 12; i ++)
				shadow[i] = true;
			for(int i = 12; i < 16; i ++)
				shadow[i] = true;
		}
		else if(orientation == Orientation.DOWN.value()){
			border[2] = new Rectangle(this.xPos - 11, this.yPos + 1, 11, 11);
			border[3] = new Rectangle(this.xPos + 1, this.yPos + 1, 11, 11);
			for(int i = 0; i < 4; i ++)
				shadow[i] = true;
			for(int i = 4; i < 8; i ++)
				shadow[i] = true;
		}
		else if(orientation == Orientation.LEFT.value()){
			border[0] = new Rectangle(this.xPos - 11, this.yPos - 11, 11, 11);
			border[2] = new Rectangle(this.xPos - 11, this.yPos + 1, 11, 11);
			for(int i = 3; i <= 15; i+=4)
				shadow[i] = true;
			for(int i = 2; i <= 14; i+=4)
				shadow[i] = true;
		}
		if(orientation == Orientation.RIGHT.value()){
			border[1] = new Rectangle(this.xPos + 1, this.yPos - 11, 11, 11);
			border[3] = new Rectangle(this.xPos + 1, this.yPos + 1, 11, 11);
			for(int i = 1; i <= 13; i+=4)
				shadow[i] = true;
			for(int i = 0; i <= 12; i+=4)
				shadow[i] = true;
		}

	}

	public void damageWall(Rectangle bullet, int bulletPower, int bulletDirection){

		bulletDestroyed = false;
		if(bulletPower == 1){
			if(border[0] != null && border[1] != null && bulletDirection == Direction.DOWN.value()){
				if(bullet.intersects(border[0]) && bullet.intersects(border[1])){
					if(shadow[1] && shadow[2]){
						for(int i = 4; i < 8; i ++)
							shadow[i] = true;
						border[0] = null;
						border[1] = null;
					}
					if(!shadow[1] || !shadow[2]){
						for(int i = 0; i < 4; i ++)
							shadow[i] = true;
					}
					bulletDestroyed = true;
				}
			}
			if(border[0] != null && border[1] != null && bulletDirection == Direction.UP.value()){
				if(bullet.intersects(border[0]) && bullet.intersects(border[1])){
					if(shadow[5] && shadow[6]){
						for(int i = 0; i < 4; i ++)
							shadow[i] = true;
						border[0] = null;
						border[1] = null;
					}
					if(!shadow[5] || !shadow[6]){
						for(int i = 4; i < 8; i ++)
							shadow[i] = true;
					}
					bulletDestroyed = true;
				}
			}
			if(border[2] != null && border[3] != null && bulletDirection == Direction.DOWN.value()){
				if(bullet.intersects(border[2]) && bullet.intersects(border[3])){
					if(shadow[9] && shadow[10]){
						for(int i = 12; i < 16; i ++)
							shadow[i] = true;
						border[2] = null;
						border[3] = null;
					}
					if(!shadow[9] || !shadow[10]){
						for(int i = 8; i < 12; i ++)
							shadow[i] = true;
					}
					bulletDestroyed = true;
				}
			}
			if(border[2] != null && border[3] != null && bulletDirection == Direction.UP.value()){
				if(bullet.intersects(border[2]) && bullet.intersects(border[3])){
					if(shadow[13] && shadow[14]){
						for(int i = 8; i < 12; i ++)
							shadow[i] = true;
						border[2] = null;
						border[3] = null;
					}
					if(!shadow[13] || !shadow[14]){
						for(int i = 12; i < 16; i ++)
							shadow[i] = true;
					}
					bulletDestroyed = true;
				}
			}
			if(border[0] != null && border[2] != null && bulletDirection == Direction.RIGHT.value()){
				if(bullet.intersects(border[0]) && bullet.intersects(border[2])){
					if(shadow[4] && shadow[8]){
						for(int i = 1; i <= 13; i+=4)
							shadow[i] = true;
						border[0] = null;
						border[2] = null;
					}
					if(!shadow[4] || !shadow[8]){
						for(int i = 0; i <= 12; i+=4)
							shadow[i] = true;
					}
					bulletDestroyed = true;
				}
			}
			if(border[0] != null && border[2] != null && bulletDirection == Direction.LEFT.value()){
				if(bullet.intersects(border[0]) && bullet.intersects(border[2])){
					if(shadow[5] && shadow[9]){
						for(int i = 0; i <= 12; i+=4)
							shadow[i] = true;
						border[0] = null;
						border[2] = null;
					}
					if(!shadow[5] || !shadow[9]){
						for(int i = 1; i <= 13; i+=4)
							shadow[i] = true;
					}
					bulletDestroyed = true;
				}
			}
			if(border[1] != null && border[3] != null && bulletDirection == Direction.RIGHT.value()){
				if(bullet.intersects(border[1]) && bullet.intersects(border[3])){
					if(shadow[6] && shadow[10]){
						for(int i = 3; i <= 15; i+=4)
							shadow[i] = true;
						border[1] = null;
						border[3] = null;
					}
					if(!shadow[6] || !shadow[10]){
						for(int i = 2; i <= 14; i+=4)
							shadow[i] = true;
					}
					bulletDestroyed = true;
				}
			}
			if(border[1] != null && border[3] != null && bulletDirection == Direction.LEFT.value()){
				if(bullet.intersects(border[1]) && bullet.intersects(border[3])){
					if(shadow[7] && shadow[11]){
						for(int i = 2; i <= 14; i+=4)
							shadow[i] = true;
						border[1] = null;
						border[3] = null;
					}
					if(!shadow[7] || !shadow[11]){
						for(int i = 3; i <= 15; i+=4)
							shadow[i] = true;
					}
					bulletDestroyed = true;
				}
			}
		}

		if(bulletPower == 2){
			if(border[0] != null && border[1] != null && (bulletDirection == Direction.UP.value() || bulletDirection == Direction.DOWN.value())){
				if(bullet.intersects(border[0]) && bullet.intersects(border[1])){
					for(int i = 0; i < 8; i++)
						shadow[i] = true;
					border[0] = null;
					border[1] = null;
					bulletDestroyed = true;
				}
			}
			if(border[2] != null && border[3] != null && (bulletDirection == Direction.UP.value() || bulletDirection == Direction.DOWN.value())){
				if(bullet.intersects(border[2]) && bullet.intersects(border[3])){
					for(int i = 8; i < 16; i++)
						shadow[i] = true;
					border[2] = null;
					border[3] = null;
					bulletDestroyed = true;
				}
			}
			if(border[0] != null && border[2] != null && (bulletDirection == Direction.LEFT.value() || bulletDirection == Direction.RIGHT.value())){
				if(bullet.intersects(border[0]) && bullet.intersects(border[2])){
					for(int i = 0; i <= 12; i+=4)
						shadow[i] = true;
					for(int i = 1; i <= 13; i+=4)
						shadow[i] = true;
					border[0] = null;
					border[2] = null;
					bulletDestroyed = true;
				}
			}
			if(border[1] != null && border[3] != null && (bulletDirection == Direction.LEFT.value() || bulletDirection == Direction.RIGHT.value())){
				if(bullet.intersects(border[1]) && bullet.intersects(border[3])){
					for(int i = 2; i <= 14; i+=4)
						shadow[i] = true;
					for(int i = 3; i <= 15; i+=4)
						shadow[i] = true;
					border[1] = null;
					border[3] = null;
					bulletDestroyed = true;
				}
			}
		}



//*******************************************************************************************		if(border[0] != null ){
		if(border[0] != null){
			Rectangle a = new Rectangle(border[0].x, border[0].y, 5, 5);
			Rectangle b = new Rectangle(border[0].x + 7, border[0].y, 5, 5);
			Rectangle c = new Rectangle(border[0].x, border[0].y + 7, 5, 5);
			Rectangle d = new Rectangle(border[0].x + 7, border[0].y + 7, 5, 5);
			if(bullet.intersects(border[0]) && bulletDirection == Direction.DOWN.value()
					&& !(bullet.intersects(b)
					&& ((!shadow[2] || !shadow[6]) || (shadow[1] && shadow[2] && shadow[5] && shadow[6])))){
				if(bullet.intersects(a) && shadow[0] && !shadow[4]){
					shadow[4] = true;
					shadow[5] = true;
					bulletDestroyed = true;
					border[0] = null;
				}
				if(bullet.intersects(b) && shadow[1] && !shadow[5]){
					shadow[4] = true;
					shadow[5] = true;
					bulletDestroyed = true;
					border[0] = null;
				}
				if(bullet.intersects(a) && !shadow[0]) {
					if(bulletPower == 1){
						shadow[0] = true;
						shadow[1] = true;
					}
					if(bulletPower == 2){
						shadow[0] = true;
						shadow[1] = true;
						shadow[4] = true;
						shadow[5] = true;
					}
					bulletDestroyed = true;
					if(shadow[4] && shadow[5])
						border[0] = null;
				}
				if(bullet.intersects(b) && !shadow[1]) {
					if(bulletPower == 1){
						shadow[0] = true;
						shadow[1] = true;
					}
					if(bulletPower == 2){
						shadow[0] = true;
						shadow[1] = true;
						shadow[4] = true;
						shadow[5] = true;
					}
					bulletDestroyed = true;
					if(shadow[4] && shadow[5])
						border[0] = null;
				}
			}
			if(border[0] != null){
				if(bullet.intersects(border[0]) && bulletDirection == Direction.UP.value() && !(bullet.intersects(d) && ((!shadow[2] || !shadow[6]) || (shadow[1] && shadow[2] && shadow[5] && shadow[6])))){
					if(bullet.intersects(c) && shadow[4] && !shadow[0]){
						shadow[0] = true;
						shadow[1] = true;
						bulletDestroyed = true;
						border[0] = null;
					}
					if(bullet.intersects(d) && shadow[5] && !shadow[1]){
						shadow[0] = true;
						shadow[1] = true;
						bulletDestroyed = true;
						border[0] = null;
					}
					if(bullet.intersects(c) && !shadow[4]){
						if(bulletPower == 1){
							shadow[4] = true;
							shadow[5] = true;
						}
						if(bulletPower == 2){
							shadow[4] = true;
							shadow[5] = true;
							shadow[1] = true;
							shadow[0] = true;
						}
						bulletDestroyed = true;
						if(shadow[0] && shadow[1])
							border[0] = null;
					}
					if(bullet.intersects(d) && !shadow[5]){
						if(bulletPower == 1){
							shadow[4] = true;
							shadow[5] = true;
						}
						if(bulletPower == 2){
							shadow[4] = true;
							shadow[5] = true;
							shadow[1] = true;
							shadow[0] = true;
						}
						bulletDestroyed = true;
						if(shadow[0] && shadow[1])
							border[0] = null;
					}
				}
			}
			if(border[0] != null){
				if(bullet.intersects(border[0]) && bulletDirection == Direction.RIGHT.value() && !(bullet.intersects(c) && ((!shadow[8] || !shadow[9]) || (shadow[4] && shadow[5] && shadow[8] && shadow[9])))){
					if(bullet.intersects(a) && shadow[0] && !shadow[1]){
						shadow[1] = true;
						shadow[5] = true;
						bulletDestroyed = true;
						border[0] = null;
					}
					if(bullet.intersects(c) && shadow[4] && !shadow[5]){
						shadow[1] = true;
						shadow[5] = true;
						bulletDestroyed = true;
						border[0] = null;
					}
					if(bullet.intersects(a) && !shadow[0]){
						if(bulletPower == 1){
							shadow[0] = true;
							shadow[4] = true;
						}
						if(bulletPower == 2){
							shadow[0] = true;
							shadow[4] = true;
							shadow[1] = true;
							shadow[5] = true;
						}
						bulletDestroyed = true;
						if(shadow[1] && shadow[5])
							border[0] = null;
					}
					if(bullet.intersects(c) && !shadow[4]){
						if(bulletPower == 1){
							shadow[0] = true;
							shadow[4] = true;
						}
						if(bulletPower == 2){
							shadow[0] = true;
							shadow[4] = true;
							shadow[1] = true;
							shadow[5] = true;
						}
						bulletDestroyed = true;
						if(shadow[1] && shadow[5])
							border[0] = null;
					}
				}
			}
			if(border[0] != null){
				if(bullet.intersects(border[0]) && bulletDirection == Direction.LEFT.value() && !(bullet.intersects(d) && ((!shadow[8] || !shadow[9]) || (shadow[4] && shadow[5] && shadow[8] && shadow[9])))){
					if(bullet.intersects(b) && shadow[1] && !shadow[0]){
						shadow[0] = true;
						shadow[4] = true;
						bulletDestroyed = true;
						border[0] = null;
					}
					if(bullet.intersects(d) && shadow[5] && !shadow[4]){
						shadow[0] = true;
						shadow[4] = true;
						bulletDestroyed = true;
						border[0] = null;
					}
					if(bullet.intersects(b) && !shadow[1]){
						if(bulletPower == 1){
							shadow[1] = true;
							shadow[5] = true;
						}
						if(bulletPower == 2){
							shadow[1] = true;
							shadow[5] = true;
							shadow[0] = true;
							shadow[4] = true;
						}
						bulletDestroyed = true;
						if(shadow[0] && shadow[4])
							border[0] = null;
					}
					if(bullet.intersects(d) && !shadow[5]){
						if(bulletPower == 1){
							shadow[1] = true;
							shadow[5] = true;
						}
						if(bulletPower == 2){
							shadow[1] = true;
							shadow[5] = true;
							shadow[0] = true;
							shadow[4] = true;
						}
						bulletDestroyed = true;
						if(shadow[0] && shadow[4])
							border[0] = null;
					}
				}
			}
		}
		//*******************************************************************************
		if(border[1] != null ){
			Rectangle a = new Rectangle(border[1].x, border[1].y, 5, 5);
			Rectangle b = new Rectangle(border[1].x + 7, border[1].y, 5, 5);
			Rectangle c = new Rectangle(border[1].x, border[1].y + 7, 5, 5);
			Rectangle d = new Rectangle(border[1].x + 7, border[1].y + 7, 5, 5);
			if(border[1] != null){
				if(bullet.intersects(border[1]) && bulletDirection == Direction.DOWN.value() && !(bullet.intersects(a) && ((!shadow[1] || !shadow[5]) || (shadow[1] && shadow[2] && shadow[5] && shadow[6])))){
					if(bullet.intersects(a) && shadow[2] && !shadow[6]){
						shadow[6] = true;
						shadow[7] = true;
						bulletDestroyed = true;
						border[1] = null;
					}
					if(bullet.intersects(b) && shadow[3] && !shadow[7]){
						shadow[6] = true;
						shadow[7] = true;
						bulletDestroyed = true;
						border[1] = null;
					}
					if(bullet.intersects(a) && !shadow[2]) {
						if(bulletPower == 1){
							shadow[2] = true;
							shadow[3] = true;
						}
						if(bulletPower == 2){
							shadow[2] = true;
							shadow[3] = true;
							shadow[6] = true;
							shadow[7] = true;
						}
						bulletDestroyed = true;
						if(shadow[6] && shadow[7])
							border[1] = null;
					}
					if(bullet.intersects(b) && !shadow[3]) {
						if(bulletPower == 1){
							shadow[2] = true;
							shadow[3] = true;
						}
						if(bulletPower == 2){
							shadow[2] = true;
							shadow[3] = true;
							shadow[6] = true;
							shadow[7] = true;
						}
						bulletDestroyed = true;
						if(shadow[6] && shadow[7])
							border[1] = null;
					}
				}
			}
			if(border[1] != null){
				if(bullet.intersects(border[1]) && bulletDirection == Direction.UP.value() && !(bullet.intersects(c) && ((!shadow[1] || !shadow[5]) || (shadow[1] && shadow[2] && shadow[5] && shadow[6])))){
					if(bullet.intersects(c) && shadow[6] && !shadow[2]){
						shadow[2] = true;
						shadow[3] = true;
						bulletDestroyed = true;
						border[1] = null;
					}
					if(bullet.intersects(d) && shadow[7] && !shadow[3]){
						shadow[2] = true;
						shadow[3] = true;
						bulletDestroyed = true;
						border[1] = null;
					}
					if(bullet.intersects(c) && !shadow[6]){
						if(bulletPower == 1){
							shadow[6] = true;
							shadow[7] = true;
						}
						if(bulletPower == 2){
							shadow[6] = true;
							shadow[7] = true;
							shadow[2] = true;
							shadow[3] = true;
						}
						bulletDestroyed = true;
						if(shadow[2] && shadow[3])
							border[1] = null;
					}
					if(bullet.intersects(d) && !shadow[7]){
						if(bulletPower == 1){
							shadow[6] = true;
							shadow[7] = true;
						}
						if(bulletPower == 2){
							shadow[6] = true;
							shadow[7] = true;
							shadow[2] = true;
							shadow[3] = true;
						}
						bulletDestroyed = true;
						if(shadow[2] && shadow[3])
							border[1] = null;
					}
				}
			}
			if(border[1] != null){
				if(bullet.intersects(border[1]) && bulletDirection == Direction.RIGHT.value() && !(bullet.intersects(c) && ((!shadow[10] || !shadow[11]) || (shadow[6] && shadow[7] && shadow[10] && shadow[11])))){
					if(bullet.intersects(a) && shadow[2] && !shadow[3]){
						shadow[3] = true;
						shadow[7] = true;
						bulletDestroyed = true;
						border[1] = null;
					}
					if(bullet.intersects(c) && shadow[6] && !shadow[7]){
						shadow[3] = true;
						shadow[7] = true;
						bulletDestroyed = true;
						border[1] = null;
					}
					if(bullet.intersects(a) && !shadow[2]){
						if(bulletPower == 1){
							shadow[2] = true;
							shadow[6] = true;
						}
						if(bulletPower == 2){
							shadow[2] = true;
							shadow[6] = true;
							shadow[3] = true;
							shadow[7] = true;
						}
						bulletDestroyed = true;
						if(shadow[3] && shadow[7])
							border[1] = null;
					}
					if(bullet.intersects(c) && !shadow[6]){
						if(bulletPower == 1){
							shadow[2] = true;
							shadow[6] = true;
						}
						if(bulletPower == 2){
							shadow[2] = true;
							shadow[6] = true;
							shadow[3] = true;
							shadow[7] = true;
						}
						bulletDestroyed = true;
						if(shadow[3] && shadow[7])
							border[1] = null;
					}
				}
			}
			if(border[1] != null){
				if(bullet.intersects(border[1]) && bulletDirection == Direction.LEFT.value() && !(bullet.intersects(d) && ((!shadow[10] || !shadow[11]) || (shadow[6] && shadow[7] && shadow[10] && shadow[11])))){
					if(bullet.intersects(b) && shadow[3] && !shadow[2]){
						shadow[2] = true;
						shadow[6] = true;
						bulletDestroyed = true;
						border[1] = null;
					}
					if(bullet.intersects(d) && shadow[7] && !shadow[6]){
						shadow[2] = true;
						shadow[6] = true;
						bulletDestroyed = true;
						border[1] = null;
					}
					if(bullet.intersects(b) && !shadow[3]){
						if(bulletPower == 1){
							shadow[3] = true;
							shadow[7] = true;
						}
						if(bulletPower == 2){
							shadow[3] = true;
							shadow[7] = true;
							shadow[2] = true;
							shadow[6] = true;
						}
						bulletDestroyed = true;
						if(shadow[2] && shadow[6])
							border[1] = null;
					}
					if(bullet.intersects(d) && !shadow[7]){
						if(bulletPower == 1){
							shadow[3] = true;
							shadow[7] = true;
						}
						if(bulletPower == 2){
							shadow[3] = true;
							shadow[7] = true;
							shadow[2] = true;
							shadow[6] = true;
						}
						bulletDestroyed = true;
						if(shadow[2] && shadow[6])
							border[1] = null;
					}
				}
			}
		}

		//***********************************************************************************
		if(border[2] != null ){
			Rectangle a = new Rectangle(border[2].x, border[2].y, 5, 5);
			Rectangle b = new Rectangle(border[2].x + 7, border[2].y, 5, 5);
			Rectangle c = new Rectangle(border[2].x, border[2].y + 7, 5, 5);
			Rectangle d = new Rectangle(border[2].x + 7, border[2].y + 7, 5, 5);
			if(border[2] != null ){
				if(bullet.intersects(border[2]) && bulletDirection == Direction.DOWN.value() && !(bullet.intersects(b) && ((!shadow[10] || !shadow[14]) || (shadow[9] && shadow[10] && shadow[13] && shadow[14])))){
					if(bullet.intersects(a) && shadow[8] && !shadow[12]){
						shadow[12] = true;
						shadow[13] = true;
						bulletDestroyed = true;
						border[2] = null;
					}
					if(bullet.intersects(b) && shadow[9] && !shadow[13]){
						shadow[12] = true;
						shadow[13] = true;
						bulletDestroyed = true;
						border[2] = null;
					}
					if(bullet.intersects(a) && !shadow[8]) {
						if(bulletPower == 1){
							shadow[8] = true;
							shadow[9] = true;
						}
						if(bulletPower == 2){
							shadow[8] = true;
							shadow[9] = true;
							shadow[12] = true;
							shadow[13] = true;
						}
						bulletDestroyed = true;
						if(shadow[12] && shadow[13])
							border[2] = null;
					}
					if(bullet.intersects(b) && !shadow[9]) {
						if(bulletPower == 1){
							shadow[8] = true;
							shadow[9] = true;
						}
						if(bulletPower == 2){
							shadow[8] = true;
							shadow[9] = true;
							shadow[12] = true;
							shadow[13] = true;
						}
						bulletDestroyed = true;
						if(shadow[12] && shadow[13])
							border[2] = null;
					}
				}
			}
			if(border[2] != null){
				if(bullet.intersects(border[2]) && bulletDirection == Direction.UP.value() && !(bullet.intersects(d) && ((!shadow[10] || !shadow[14]) || (shadow[9] && shadow[10] && shadow[13] && shadow[14])))){
					if(bullet.intersects(c) && shadow[12] && !shadow[8]){
						shadow[8] = true;
						shadow[9] = true;
						bulletDestroyed = true;
						border[2] = null;
					}
					if(bullet.intersects(d) && shadow[13] && !shadow[9]){
						shadow[8] = true;
						shadow[9] = true;
						bulletDestroyed = true;
						border[2] = null;
					}
					if(bullet.intersects(c) && !shadow[12]){
						if(bulletPower == 1){
							shadow[12] = true;
							shadow[13] = true;
						}
						if(bulletPower == 2){
							shadow[12] = true;
							shadow[13] = true;
							shadow[8] = true;
							shadow[9] = true;
						}
						bulletDestroyed = true;
						if(shadow[8] && shadow[9])
							border[2] = null;
					}
					if(bullet.intersects(d) && !shadow[13]){
						if(bulletPower == 1){
							shadow[12] = true;
							shadow[13] = true;
						}
						if(bulletPower == 2){
							shadow[12] = true;
							shadow[13] = true;
							shadow[8] = true;
							shadow[9] = true;
						}
						bulletDestroyed = true;
						if(shadow[8] && shadow[9])
							border[2] = null;
					}
				}
			}
			if(border[2] != null){
				if(bullet.intersects(border[2]) && bulletDirection == Direction.RIGHT.value() && !(bullet.intersects(a) && ((!shadow[4] || !shadow[5]) || (shadow[4] && shadow[5] && shadow[8] && shadow[9])))){
					if(bullet.intersects(a) && shadow[8] && !shadow[9]){
						shadow[9] = true;
						shadow[13] = true;
						bulletDestroyed = true;
						border[2] = null;
					}
					if(bullet.intersects(c) && shadow[12] && !shadow[13]){
						shadow[9] = true;
						shadow[13] = true;
						bulletDestroyed = true;
						border[2] = null;
					}
					if(bullet.intersects(a) && !shadow[8]){
						if(bulletPower == 1){
							shadow[8] = true;
							shadow[12] = true;
						}
						if(bulletPower == 2){
							shadow[8] = true;
							shadow[12] = true;
							shadow[9] = true;
							shadow[13] = true;
						}
						bulletDestroyed = true;
						if(shadow[9] && shadow[13])
							border[2] = null;
					}
					if(bullet.intersects(c) && !shadow[12]){
						if(bulletPower == 1){
							shadow[8] = true;
							shadow[12] = true;
						}
						if(bulletPower == 2){
							shadow[8] = true;
							shadow[12] = true;
							shadow[9] = true;
							shadow[13] = true;
						}
						bulletDestroyed = true;
						if(shadow[9] && shadow[13])
							border[2] = null;
					}
				}
			}
			if(border[2] != null){
				if(bullet.intersects(border[2]) && bulletDirection == Direction.LEFT.value() && !(bullet.intersects(b) && ((!shadow[4] || !shadow[5]) || (shadow[4] && shadow[5] && shadow[8] && shadow[9])))){
					if(bullet.intersects(b) && shadow[9] && !shadow[8]){
						shadow[8] = true;
						shadow[12] = true;
						bulletDestroyed = true;
						border[2] = null;
					}
					if(bullet.intersects(d) && shadow[13] && !shadow[12]){
						shadow[8] = true;
						shadow[12] = true;
						bulletDestroyed = true;
						border[2] = null;
					}
					if(bullet.intersects(b) && !shadow[9]){
						if(bulletPower == 1){
							shadow[9] = true;
							shadow[13] = true;
						}
						if(bulletPower == 2){
							shadow[9] = true;
							shadow[13] = true;
							shadow[8] = true;
							shadow[12] = true;
						}
						bulletDestroyed = true;
						if(shadow[8] && shadow[12])
							border[2] = null;
					}
					if(bullet.intersects(d) && !shadow[13]){
						if(bulletPower == 1){
							shadow[9] = true;
							shadow[13] = true;
						}
						if(bulletPower == 2){
							shadow[9] = true;
							shadow[13] = true;
							shadow[8] = true;
							shadow[12] = true;
						}
						bulletDestroyed = true;
						if(shadow[8] && shadow[12])
							border[2] = null;
					}
				}
			}

		}
		//************************************************************************************
		if(border[3] != null ){
			Rectangle a = new Rectangle(border[3].x, border[3].y, 5, 5);
			Rectangle b = new Rectangle(border[3].x + 7, border[3].y, 5, 5);
			Rectangle c = new Rectangle(border[3].x, border[3].y + 7, 5, 5);
			Rectangle d = new Rectangle(border[3].x + 7, border[3].y + 7, 5, 5);
			if(border[3] != null ){
				if(bullet.intersects(border[3]) && bulletDirection == Direction.DOWN.value() && !(bullet.intersects(a) && ((!shadow[9] || !shadow[13]) || (shadow[9] && shadow[10] && shadow[13] && shadow[14])))){
					if(bullet.intersects(a) && shadow[10] && !shadow[14]){
						shadow[14] = true;
						shadow[15] = true;
						bulletDestroyed = true;
						border[3] = null;
					}
					if(bullet.intersects(b) && shadow[11] && !shadow[15]){
						shadow[14] = true;
						shadow[15] = true;
						bulletDestroyed = true;
						border[3] = null;
					}
					if(bullet.intersects(a) && !shadow[10]) {
						if(bulletPower == 1){
							shadow[10] = true;
							shadow[11] = true;
						}
						if(bulletPower == 2){
							shadow[10] = true;
							shadow[11] = true;
							shadow[14] = true;
							shadow[15] = true;
						}
						bulletDestroyed = true;
						if(shadow[14] && shadow[15])
							border[3] = null;
					}
					if(bullet.intersects(b) && !shadow[11]) {
						if(bulletPower == 1){
							shadow[10] = true;
							shadow[11] = true;
						}
						if(bulletPower == 2){
							shadow[10] = true;
							shadow[11] = true;
							shadow[14] = true;
							shadow[15] = true;
						}
						bulletDestroyed = true;
						if(shadow[14] && shadow[15])
							border[3] = null;
					}
				}
			}
			if(border[3] != null){
				if(bullet.intersects(border[3]) && bulletDirection == Direction.UP.value() && !(bullet.intersects(c) && ((!shadow[9] || !shadow[13]) || (shadow[9] && shadow[10] && shadow[13] && shadow[14])))){
					if(bullet.intersects(c) && shadow[14] && !shadow[10]){
						shadow[10] = true;
						shadow[11] = true;
						bulletDestroyed = true;
						border[3] = null;
					}
					if(bullet.intersects(d) && shadow[15] && !shadow[11]){
						shadow[10] = true;
						shadow[11] = true;
						bulletDestroyed = true;
						border[3] = null;
					}
					if(bullet.intersects(c) && !shadow[14]){
						if(bulletPower == 1){
							shadow[14] = true;
							shadow[15] = true;
						}
						if(bulletPower == 2){
							shadow[14] = true;
							shadow[15] = true;
							shadow[10] = true;
							shadow[11] = true;
						}
						bulletDestroyed = true;
						if(shadow[10] && shadow[11])
							border[3] = null;
					}
					if(bullet.intersects(d) && !shadow[15]){
						if(bulletPower == 1){
							shadow[14] = true;
							shadow[15] = true;
						}
						if(bulletPower == 2){
							shadow[14] = true;
							shadow[15] = true;
							shadow[10] = true;
							shadow[11] = true;
						}
						bulletDestroyed = true;
						if(shadow[10] && shadow[11])
							border[3] = null;
					}
				}
			}
			if(border[3] != null){
				if(bullet.intersects(border[3]) && bulletDirection == Direction.RIGHT.value() && !(bullet.intersects(a) && ((!shadow[6] || !shadow[7]) || (shadow[6] && shadow[7] && shadow[10] && shadow[11])))){
					if(bullet.intersects(a) && shadow[10] && !shadow[11]){
						shadow[11] = true;
						shadow[15] = true;
						bulletDestroyed = true;
						border[3] = null;
					}
					if(bullet.intersects(c) && shadow[14] && !shadow[15]){
						shadow[11] = true;
						shadow[15] = true;
						bulletDestroyed = true;
						border[3] = null;
					}
					if(bullet.intersects(a) && !shadow[10]){
						if(bulletPower == 1){
							shadow[10] = true;
							shadow[14] = true;
						}
						if(bulletPower == 2){
							shadow[10] = true;
							shadow[14] = true;
							shadow[11] = true;
							shadow[15] = true;
						}
						bulletDestroyed = true;
						if(shadow[11] && shadow[15])
							border[3] = null;
					}
					if(bullet.intersects(c) && !shadow[14]){
						if(bulletPower == 1){
							shadow[10] = true;
							shadow[14] = true;
						}
						if(bulletPower == 2){
							shadow[10] = true;
							shadow[14] = true;
							shadow[11] = true;
							shadow[15] = true;
						}
						bulletDestroyed = true;
						if(shadow[11] && shadow[15])
							border[3] = null;
					}
				}
			}
			if(border[3] != null){
				if(bullet.intersects(border[3]) && bulletDirection == Direction.LEFT.value() && !(bullet.intersects(b) && ((!shadow[6] || !shadow[7]) || (shadow[6] && shadow[7] && shadow[10] && shadow[11])))){
					if(bullet.intersects(b) && shadow[11] && !shadow[10]){
						shadow[10] = true;
						shadow[14] = true;
						bulletDestroyed = true;
						border[3] = null;
					}
					if(bullet.intersects(d) && shadow[15] && !shadow[14]){
						shadow[10] = true;
						shadow[14] = true;
						bulletDestroyed = true;
						border[3] = null;
					}
					if(bullet.intersects(b) && !shadow[11]){
						if(bulletPower == 1){
							shadow[11] = true;
							shadow[15] = true;
						}
						if(bulletPower == 2){
							shadow[11] = true;
							shadow[15] = true;
							shadow[10] = true;
							shadow[14] = true;
						}
						bulletDestroyed = true;
						if(shadow[10] && shadow[14])
							border[3] = null;
					}
					if(bullet.intersects(d) && !shadow[15]){
						if(bulletPower == 1){
							shadow[11] = true;
							shadow[15] = true;
						}
						if(bulletPower == 2){
							shadow[11] = true;
							shadow[15] = true;
							shadow[10] = true;
							shadow[14] = true;
						}
						bulletDestroyed = true;
						if(shadow[10] && shadow[14])
							border[3] = null;
					}
				}
			}
		}
	}
//===========================================================================================

	public boolean wallDestroyed(){
		if(wallDestroyed)
			return true;
		wallDestroyed = true;
		for(int i = 0; i < shadow.length; i++)
			if(!shadow[i]){
				wallDestroyed = false;
				break;
			}
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
		g.drawImage(image, xPos - 12, yPos - 12, null);
		g.setColor(new Color(128, 64, 0));
		if(shadow[0])
			g.fillRect(xPos - 12, yPos - 12, 7, 7);
		if(shadow[1])
			g.fillRect(xPos - 6, yPos - 12, 7, 7);
		if(shadow[2])
			g.fillRect(xPos, yPos - 12, 7, 7);
		if(shadow[3])
			g.fillRect(xPos + 6, yPos - 12, 7, 7);
		if(shadow[4])
			g.fillRect(xPos - 12, yPos - 6, 7, 7);
		if(shadow[5])
			g.fillRect(xPos - 6, yPos - 6, 7, 7);
		if(shadow[6])
			g.fillRect(xPos, yPos - 6, 7, 7);
		if(shadow[7])
			g.fillRect(xPos + 6, yPos - 6, 7, 7);
		if(shadow[8])
			g.fillRect(xPos - 12, yPos, 7, 7);
		if(shadow[9])
			g.fillRect(xPos - 6, yPos, 7, 7);
		if(shadow[10])
			g.fillRect(xPos, yPos, 7, 7);
		if(shadow[11])
			g.fillRect(xPos + 6, yPos, 7, 7);
		if(shadow[12])
			g.fillRect(xPos - 12, yPos + 6, 7, 7);
		if(shadow[13])
			g.fillRect(xPos - 6, yPos + 6, 7, 7);
		if(shadow[14])
			g.fillRect(xPos, yPos + 6, 7, 7);
		if(shadow[15])
			g.fillRect(xPos + 6, yPos + 6, 7, 7);
	}

	public ActorType getType(){
		return ActorType.WALL;
	}


	//未使用的方法
	public void move(){}

	public boolean isBulletDestroyed() {
		return bulletDestroyed;
	}
}



