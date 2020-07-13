package tankwar.entity;

import tankwar.enums.BombType;
import tankwar.model.Model;
import tankwar.enums.ActorType;
import tankwar.enums.Direction;
import tankwar.utils.AudioPlay;
import tankwar.utils.AudioUtil;

import java.awt.*;

/**
 * 玩家类
 */
public class Player implements Actor {
    private final int size = 12;
    private final Rectangle map = new Rectangle(35, 35, 452, 452);
    private int scores;
    private int life;
    private int speed ;
    private int direction;
    private int InvulnerableTime;
    private int freezed;
    private int freezedTime;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;
    private boolean fire;
    private int numberOfBullet;
    private int coolDownTime;
    private int status;
    private int health;
    private int xPos, yPos, xVPos, yVPos;
    private Rectangle border;
    private Image standardImage;
    private Image[] textures;
    private Model gameModel;

    public Player(Model gameModel) {
        life = 3;
        direction = Direction.UP.value();
        status = 1;
        health = 1;
        numberOfBullet = 1;
        InvulnerableTime = 150;
        this.gameModel = gameModel;

        textures = new Image[4];
        //玩家1游戏开启时位置
        xPos = 198;
        yPos = 498;
        //玩家1的图像
        for (int i = 0; i < 4; i++)
            textures[i] = gameModel.textures[54 + i];
        standardImage = textures[0];

        xVPos = xPos;
        yVPos = yPos;
        border = new Rectangle(xPos - size, yPos - size, 25, 25);

    }

    @Override
    public void move() {
        if (gameModel.gamePaused) {
            return;
        }

        if (coolDownTime > 0) {
            coolDownTime--;
        }
        if (InvulnerableTime > 0) {
            InvulnerableTime--;
        }

        if (freezed == 1) {
            return;
        }

        //如果玩家点击“开火”键，并且满足条件，则创建一个子弹目标（即发射子弹）
        if (fire && coolDownTime == 0 && numberOfBullet > 0) {
            //子弹方向
            int c = direction;
            //子弹位置
            int a, b;
            if (direction == Direction.UP.value()) {
                a = xPos;
                b = yPos - size;
            } else if (direction == Direction.DOWN.value()) {
                a = xPos;
                b = yPos + size;
            } else if (direction == Direction.LEFT.value()) {
                a = xPos - size;
                b = yPos;
            } else {
                a = xPos + size;
                b = yPos;
            }
            //子弹速度
            int d;
            if (status == 1) {
                numberOfBullet = 1;
                d = 7;
            } else {
                d = 12;
            }
            //子弹能力
            int e;
            if (status == 4) {
                e = 2;
            } else {
                e = 1;
            }
            //添加子弹
            gameModel.addActor(new Bullet(a, b, c, d, e, this, gameModel));
            //coolDownTime是你要等到你可以发射第二颗子弹时间（与魔兽争霸3相同）
            if (status > 2) {
                coolDownTime = 5;
            } else {
                coolDownTime = 8;
            }
            //减少子弹的可用数，子弹发射时numberOfBullet会增加
            //由玩家的坦克击中目标（例如，墙壁，敌人坦克等）；
            numberOfBullet--;
        }


        //保存当前位置信息，如果新的移动确定后无效，则更改
        //以前的位置
        int xPosTemp = xPos;
        int yPosTemp = yPos;
        Rectangle borderTemp = new Rectangle(xPosTemp - size, yPosTemp - size, 25, 25);

        //根据玩家坦克的移动定义玩家坦克的下一个边界，假设它的下一个移动是有效的；
        boolean notMoving = false;
        if (moveUp) {
            if (direction != Direction.UP.value() && direction != Direction.DOWN.value())
                xPos = xVPos;
            yPos -= speed;
            direction = Direction.UP.value();
        } else if (moveDown) {
            if (direction != Direction.UP.value() && direction != Direction.DOWN.value()) {
                xPos = xVPos;
            }
            yPos += speed;
            direction = Direction.DOWN.value();
        } else if (moveLeft) {
            if (direction != Direction.LEFT.value() && direction != Direction.RIGHT.value()) {
                yPos = yVPos;
            }
            xPos -= speed;
            direction = Direction.LEFT.value();
        } else if (moveRight) {
            if (direction != Direction.LEFT.value() && direction != Direction.RIGHT.value())
                yPos = yVPos;
            xPos += speed;
            direction = Direction.RIGHT.value();
        } else {
            notMoving = true;
        }
        if (notMoving) {
            if (speed > 0)
                speed--;
        } else {
            if (speed < 3)
                speed++;
        }

        //更新边界
        border.y = yPos - size;
        border.x = xPos - size;

        //检查下一个边界是否与地图边界相交，如果不移动到任何地方
        if (!border.intersects(map)) {
            xPos = xVPos;
            yPos = yVPos;
            border.x = xPos - size;
            border.y = yPos - size;
            return;
        }

        //检查下个边界是否与其他对象相交，如玩家控制的坦克，墙等等
        for (int i = 0; i < gameModel.actors.length; i++) {
            if (gameModel.actors[i] != null) {
                if (this != gameModel.actors[i]) {
                    if (border.intersects(gameModel.actors[i].getBorder())) {
                        if (gameModel.actors[i].getType()==ActorType.POWER_UP) {
                            scores += 50;
                            PowerUp temp = (PowerUp) gameModel.actors[i];
                            int function = temp.getFunction();
                            if (function == 0) {  //普通星星，增加速度
                                upgrade();
                            } else if (function == 1) {  //钢墙保护基地
                                Base tempe = (Base) gameModel.actors[4];
                                tempe.setSteelWallTime(600);
                            } else if (function == 2) {   // 杀死所有的敌方坦克
                                for (int j = 0; j < gameModel.actors.length; j++)
                                    if (gameModel.actors[j] != null)
                                        if (gameModel.actors[j].getType()==ActorType.ENEMY) {
                                            Enemy tempe = (Enemy) gameModel.actors[j];
                                            gameModel.addActor(new Bomb(tempe.xPos, tempe.yPos, BombType.BIG, gameModel));
                                            gameModel.removeActor(gameModel.actors[j]);
                                        }
                                Level.NoOfEnemy = 0;
                                Level.deathCount = 20 - Level.enemyLeft;
                            } else if (function == 3) {   //防护盾，刀枪不入
                                InvulnerableTime = 300 + (int) Math.random() * 400;
                            } else if (function == 4) {  //冻结所有敌人
                                Enemy.freezedTime = 300 + (int) Math.random() * 400;
                                Enemy.freezedMoment = Model.gameFlow;
                            } else if (function == 5) { //超级星星
                                if (status < 3)
                                    numberOfBullet++;
                                status = 4;
                                health = 2;
                                for (int j = 0; j < 4; j++)
                                    textures[j] = gameModel.textures[66 + j];

                            } else if (function == 6) {  // 增加生命
                                life++;
                            }

                            gameModel.removeActor(gameModel.actors[i]);

                            new AudioPlay(AudioUtil.ADD).new AudioThread().start();// 播放背景音效
                        }
                        //静态对象，如墙壁，河流
                        else if (gameModel.actors[i].getType()==ActorType.STEEL_WALL || gameModel.actors[i].getType()==ActorType.WALL) {
                            if (!gameModel.actors[i].wallDestroyed()) {
                                for (int j = 0; j < gameModel.actors[i].getDetailedBorder().length; j++) {
                                    if (gameModel.actors[i].getDetailedBorder()[j] != null) {
                                        if (gameModel.actors[i].getDetailedBorder()[j].intersects(border)) {
                                            xPos = xVPos;
                                            yPos = yVPos;
                                            border.x = xPos - size;
                                            border.y = yPos - size;
                                            return;
                                        }
                                    }
                                }
                            }
                        } else if (gameModel.actors[i].getType()==ActorType.RIVER || gameModel.actors[i].getType()==ActorType.BASE) {
                            xPos = xVPos;
                            yPos = yVPos;
                            border.x = xPos - size;
                            border.y = yPos - size;

                            return;
                        }
                        //移动对象，例如敌人坦克
                        else if (gameModel.actors[i].getType()==ActorType.ENEMY || gameModel.actors[i].getType()==ActorType.PLAYER) {
                            if (!borderTemp.intersects(gameModel.actors[i].getBorder()) || gameModel.actors[i].getType()==ActorType.ENEMY) {
                                xPos = xPosTemp;
                                yPos = yPosTemp;
                                border.x = xPos - size;
                                border.y = yPos - size;
                                return;
                            }
                        }
                    }
                }
            }
        }

        //找到坦克的虚拟位置，当90度转弯时，虚拟位置用来调整坦克的真实位置。
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

    public void draw(Graphics g) {
        //绘制玩家坦克
        g.drawImage(textures[direction], xPos - size, yPos - size, null);
        if (InvulnerableTime > 0) {
            g.setColor(Color.red);
            g.drawRect(xPos - 12, yPos - 12, 25, 25);
            g.drawRect(xPos - 11, yPos - 11, 23, 23);
        }

        //关于玩家的信息，如分数，生命等
        g.setColor(Color.yellow);
        g.drawImage(standardImage, 520, 380, null);
        g.drawString("x", 555, 395);
        g.drawString(life + "", 565, 396);
        String SCORE = "000000000" + scores;
        g.drawString(" 得分:" + "", 515, 370);
        g.drawString(SCORE.substring(SCORE.length() - 7, SCORE.length()) + "", 566, 370);
    }

    public Rectangle getBorder() {
        return border;
    }

    public ActorType getType() {
        return ActorType.PLAYER;
    }

    public void hurt() {
        if (InvulnerableTime != 0)
            return;

        //如果坦克只有1级的健康状态，被击中，那么玩家坦克失去一个生命，如果玩家坦克是最后一次生命，被击中，则game over
        //只有吃掉超级星星时，玩家才会有2级的生命健康状态
        if (health == 1) {
            new AudioPlay(AudioUtil.BLAST).new AudioThread().start();//新建一个音效线程，用于播放音效

            gameModel.addActor(new Bomb(xPos, yPos, BombType.BIG, gameModel));
            life--;
            if (life == 0) {
                xPos = 100000;
                yPos = 100000;           //this will make the com.server.player never come back to the main screen, thus looks like "dead"
                border = new Rectangle(xPos - size, yPos - size, 25, 25);
                xVPos = xPos;
                yVPos = yPos;
            } else {
                direction = Direction.UP.value();
                status = 1;
                health = 1;
                numberOfBullet = 1;
                InvulnerableTime = 150;

                xPos = 198;
                yPos = 498;
                border = new Rectangle(xPos - size, yPos - size, 25, 25);
                xVPos = xPos;
                yVPos = yPos;
                for (int i = 0; i < 4; i++)
                    textures[i] = gameModel.textures[54 + i];
            }
        } else {
            new AudioPlay(AudioUtil.HIT).new AudioThread().start();
            health--;
            status = 3;
            for (int i = 0; i < 4; i++)
                textures[i] = gameModel.textures[62 + i];
        }
    }

    public void upgrade() {
        //当玩家坦克吃掉正常的星星时，他的子弹将会升级
        if (status == 1) {
            status = 2;
            for (int i = 0; i < 4; i++)
                textures[i] = gameModel.textures[58 + i];
        } else if (status == 2) {
            status = 3;
            numberOfBullet++;
            for (int i = 0; i < 4; i++)
                textures[i] = gameModel.textures[62 + i];
        } else if (status == 3) {
            status = 4;
            for (int i = 0; i < 4; i++)
                textures[i] = gameModel.textures[66 + i];
        }
    }

    public void reset() {
        direction = Direction.UP.value();
        InvulnerableTime = 150;
        xPos = 198;
        yPos = 498;

        xVPos = xPos;
        yVPos = yPos;
        border = new Rectangle(xPos - size, yPos - size, 25, 25);
    }


    //未使用的方法
    public Rectangle[] getDetailedBorder() {
        return null;
    }

    public boolean wallDestroyed() {
        return false;
    }

    public void numberOfBulletIncrease()
    {
        numberOfBullet++;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getSize() {
        return size;
    }

    public Rectangle getMap() {
        return map;
    }

    public int getScores() {
        return scores;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getInvulnerableTime() {
        return InvulnerableTime;
    }

    public void setInvulnerableTime(int invulnerableTime) {
        InvulnerableTime = invulnerableTime;
    }

    public int getFreezed() {
        return freezed;
    }

    public void setFreezed(int freezed) {
        this.freezed = freezed;
    }

    public int getFreezedTime() {
        return freezedTime;
    }

    public void setFreezedTime(int freezedTime) {
        this.freezedTime = freezedTime;
    }

    public boolean getMoveUp() {
        return moveUp;
    }

    public void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    public boolean getMoveDown() {
        return moveDown;
    }

    public void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    public boolean getMoveLeft() {
        return moveLeft;
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public boolean getMoveRight() {
        return moveRight;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public boolean isFire() {
        return fire;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public int getNumberOfBullet() {
        return numberOfBullet;
    }

    public void setNumberOfBullet(int numberOfBullet) {
        this.numberOfBullet = numberOfBullet;
    }

    public int getCoolDownTime() {
        return coolDownTime;
    }

    public void setCoolDownTime(int coolDownTime) {
        this.coolDownTime = coolDownTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public int getxVPos() {
        return xVPos;
    }

    public void setxVPos(int xVPos) {
        this.xVPos = xVPos;
    }

    public int getyVPos() {
        return yVPos;
    }

    public void setyVPos(int yVPos) {
        this.yVPos = yVPos;
    }

    public void setBorder(Rectangle border) {
        this.border = border;
    }

    public Image getStandardImage() {
        return standardImage;
    }

    public void setStandardImage(Image standardImage) {
        this.standardImage = standardImage;
    }

    public Image[] getTextures() {
        return textures;
    }

    public void setTextures(Image[] textures) {
        this.textures = textures;
    }

    public Model getGameModel() {
        return gameModel;
    }

    public void setGameModel(Model gameModel) {
        this.gameModel = gameModel;
    }
}
