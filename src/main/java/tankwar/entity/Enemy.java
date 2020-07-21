package tankwar.entity;

import tankwar.enums.*;
import tankwar.model.Model;
import tankwar.utils.AudioPlay;
import tankwar.utils.AudioUtil;

import java.awt.*;

/**
 * 敌方坦克类
 */
public class Enemy implements Actor {
    public final int UP = 0;
    public final int DOWN = 1;
    public final int LEFT = 2;
    public final int RIGHT = 3;
    public final int size = 12;
    public final Rectangle map = new Rectangle(35, 35, 452, 452);
    public static int freezedTime;
    public static int freezedMoment;
    public int numberOfBullet;
    public int coolDownTime;
    public int type;
    public int speed;
    public int direction;
    public int interval;
    public int health;
    ;
    public int xPos, yPos, xVPos, yVPos;
    public Rectangle border;
    public boolean flashing;
    public double firePossibility;
    public Image[] textures;
    public Model gameModel;

    public Enemy(int type, boolean flashing, int xPos, int yPos, Model gameModel) {
        this.type = type;
        this.xPos = xPos;
        this.yPos = yPos;
        this.flashing = flashing;
        this.gameModel = gameModel;

        //设置全部敌人的共同属性
        interval = (int) (Math.random() * 200);
        direction = (int) (Math.random() * 4);
        numberOfBullet = 1;
        xVPos = xPos;
        yVPos = yPos;
        border = new Rectangle(xPos - size, yPos - size, 25, 25);


        //根据不同类型的敌人设置独特的属性如：容貌,速度,等等
        if (type == 1) {
            firePossibility = FirePossibility.HIGH.value();
            speed = 2;
            textures = new Image[8];
            for (int i = 0; i < 8; i++)
                textures[i] = gameModel.textures[2 + i];
        } else if (type == 2) {
            firePossibility = FirePossibility.HIGH.value();
            speed = 4;
            textures = new Image[8];
            for (int i = 0; i < 8; i++)
                textures[i] = gameModel.textures[38 + i];
        } else if (type == 3) {
            firePossibility = FirePossibility.LOW.value();
            speed = 2;
            textures = new Image[8];
            for (int i = 0; i < 8; i++)
                textures[i] = gameModel.textures[10 + i];
        } else {
            firePossibility = FirePossibility.HIGH.value();
            health = 3;
            speed = 2;
            textures = new Image[20];
            for (int i = 0; i < 20; i++)
                textures[i] = gameModel.textures[18 + i];

        }

    }

    public void move() {
        if (gameModel.isGamePaused()) {
            return;
        }

        if (freezedTime > Model.gameFlow - freezedMoment) {
            return;
        }

        //敌方坦克在一个周期内将会朝着相同的方向继续移动（如果不与其他对象相互影响）
        //在每个周期结束时，它将转向新的方向
        if (interval > 0)
            interval--;
        if (interval == 0) {
            interval = (int) (Math.random() * 100);
            int newDirection = (int) (Math.random() * 4);
            if (direction != newDirection) {
                if (direction / 2 != newDirection / 2) {
                    xPos = xVPos;
                    yPos = yVPos;
                    border.x = xPos - size;
                    border.y = yPos - size;
                }
                direction = newDirection;
            }

        }

        //完全随机的决定是否要发射一颗子弹，敌方坦克不能开火
        //如果第一个不是摧毁的子弹
        if (coolDownTime > 0)
            coolDownTime--;
        if (Math.random() > firePossibility && coolDownTime == 0 && numberOfBullet > 0) {
            //获得子弹方向
            int c = direction;
            //获得子弹位置
            int a, b;
            if (direction == UP) {
                a = xPos;
                b = yPos - size;
            } else if (direction == DOWN) {
                a = xPos;
                b = yPos + size;
            } else if (direction == LEFT) {
                a = xPos - size;
                b = yPos;
            } else {
                a = xPos + size;
                b = yPos;
            }
            //获得子弹速度
            int d;
            if (type == 3) {
                d = BulletSpeed.ENHANCED.type();
            } else {
                d = BulletSpeed.NORMAL.type();
            }
            //添加子弹
            gameModel.addActor(new Bullet(a, b, c, d, BulletPower.NORMAL.type(), this, gameModel));
            coolDownTime = 7;
            if (type == 3)
                coolDownTime = 5;
            numberOfBullet--;
        }

        //保存当前位置信息,如果确定了新举措无效后,然后改变
        int xPosTemp = xPos;
        int yPosTemp = yPos;
        Rectangle borderTemp = new Rectangle(xPosTemp - size, yPosTemp - size, 25, 25);

        //定义地方坦克的下一个边界，假设它有效的根据方向来进行移动
        if (direction == UP) {
            yPos -= speed;
        } else if (direction == DOWN) {
            yPos += speed;
        } else if (direction == LEFT) {
            xPos -= speed;
        } else {
            xPos += speed;
        }

        //更新边界
        border.y = yPos - size;
        border.x = xPos - size;

        //检查下一个边界是否会与地图边界相交，如果不相交则随机生成边界
        if (!border.intersects(map)) {
            direction = (int) (Math.random() * 4);
            interval = (int) (Math.random() * 250);
            xPos = xVPos;
            yPos = yVPos;
            border.x = xPos - size;
            border.y = yPos - size;
            return;
        }

        //检查下一个边界是否与其他对象相交，例如玩家控制的坦克，墙等等
        for (int i = 0; i < gameModel.actors.length; i++) {
            if (gameModel.actors[i] != null) {
                if (this != gameModel.actors[i]) {
                    if (border.intersects(gameModel.actors[i].getBorder())) {
                        //静态对象，例如河流，墙等等
                        if (gameModel.actors[i].getType() == ActorType.STEEL_WALL || gameModel.actors[i].getType() == ActorType.WALL) {
                            if (!gameModel.actors[i].wallDestroyed()) {
                                for (int j = 0; j < gameModel.actors[i].getDetailedBorder().length; j++) {
                                    if (gameModel.actors[i].getDetailedBorder()[j] != null) {
                                        if (gameModel.actors[i].getDetailedBorder()[j].intersects(border)) {
                                            if (Math.random() > 0.90)
                                                direction = (int) (Math.random() * 4);
                                            xPos = xVPos;
                                            yPos = yVPos;
                                            border.x = xPos - size;
                                            border.y = yPos - size;
                                            return;
                                        }
                                    }
                                }
                            }
                        } else if (gameModel.actors[i].getType() == ActorType.RIVER || gameModel.actors[i].getType() == ActorType.BASE) {
                            if (Math.random() > 0.90)
                                direction = (int) (Math.random() * 4);
                            xPos = xVPos;
                            yPos = yVPos;
                            border.x = xPos - size;
                            border.y = yPos - size;
                            return;
                        }
                        //其他对象，其他的坦克
                        if (gameModel.actors[i].getType() == ActorType.PLAYER || gameModel.actors[i].getType() == ActorType.ENEMY) {
                            if (!borderTemp.intersects(gameModel.actors[i].getBorder())) {
                                xPos = xPosTemp;
                                yPos = yPosTemp;
                                border.x = xPos - size;
                                border.y = yPos - size;
                                int newDirection = (int) (Math.random() * 4);
                                if (direction != newDirection) {
                                    if (direction / 2 != newDirection / 2) {
                                        xPos = xVPos;
                                        yPos = yVPos;
                                        border.x = xPos - size;
                                        border.y = yPos - size;
                                    }
                                    direction = newDirection;
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }


        ///当坦克是90度倾斜时，找到坦克的虚拟位置，使用虚拟位置调整坦克的真实位置
        int a = (xPos - 10) / 25;
        int b = (xPos - 10) % 25;
        if (b < 7)
            b = 0;
        if (b > 18)
            b = 25;
        if ((b < 19 && b > 6) || xPos < 17 || xPos > 492)
            b = 13;
        xVPos = a * 25 + b + 10;
        int c = (yPos - 10) / 25;
        int d = (yPos - 10) % 25;
        if (d < 7)
            d = 0;
        if (d > 18)
            d = 25;
        if ((d < 19 && d > 6) || yPos < 17 || yPos > 492)
            d = 13;
        yVPos = c * 25 + d + 10;
    }

    //如果玩家坦克打出一颗子弹，判断会发生什么
    public void hurt() {
        if (flashing) {
            gameModel.addActor(new PowerUp(gameModel));
        }
        flashing = false;
        boolean death = false;
        if (type != 4) {
            death = true;
        } else {
            if (health == 0) {
                death = true;
            } else {
                new AudioPlay(AudioUtil.HIT).new AudioThread().start();
                if (health == 3) {
                    for (int i = 0; i < 4; i++)
                        textures[i] = textures[4 + i];
                } else if (health == 2) {
                    for (int i = 0; i < 4; i++)
                        textures[i] = textures[8 + i];
                } else if (health == 1) {
                    for (int i = 0; i < 4; i++)
                        textures[i] = textures[12 + i];
                }
                health--;
            }
        }

        if (death) {
            new AudioPlay(AudioUtil.BLAST).new AudioThread().start();
            Level.NoOfEnemy--;
            Level.deathCount++;
            gameModel.removeActor(this);
            gameModel.addActor(new Bomb(xPos, yPos, BombType.BIG, gameModel));
        }
    }

    public ActorType getType() {
        return ActorType.ENEMY;
    }

    public void draw(Graphics g) {
        if (flashing && Model.gameFlow % 10 > 4)
            g.drawImage(textures[textures.length - 4 + direction], xPos - size, yPos - size, null);
        else
            g.drawImage(textures[direction], xPos - size, yPos - size, null);
    }

    public Rectangle getBorder() {
        return border;
    }

    //未使用的方法
    public Rectangle[] getDetailedBorder() {
        return null;
    }

    public boolean wallDestroyed() {
        return false;
    }
}