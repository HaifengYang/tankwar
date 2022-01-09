package tankwar.entity;

import tankwar.config.ThreadPoolFactory;
import tankwar.constant.*;
import tankwar.model.Model;
import tankwar.utils.AudioPlay;
import tankwar.utils.MusicUtil;

import java.awt.*;
import java.util.Random;

/**
 * 玩家类
 */
public class Player implements Actor {
    private final int size = 12;
    private final Rectangle map = new Rectangle(35, 35, 452, 452);
    private int scores;
    private int life;
    private int speed;
    private int invulnerableTime;
    private Direction direction;
    // 长度为4的数组，分别代表moveUp，moveDown, moveLeft, moveRight
    private boolean[] movingDirection;
    private boolean fire;
    private int numberOfBullet;
    private int coolDownTime;
    private int status;
    private int health;
    private int frozen;
    private int xPos, yPos, xVPos, yVPos;
    private Rectangle border;
    private final Image standardImage;
    private final Image[] textures;
    private final Model gameModel;

    public Player(Model gameModel) {
        life = 3;
        status = 1;
        health = 1;
        numberOfBullet = 1;
        invulnerableTime = 150;
        this.movingDirection = new boolean[4];
        this.direction = Direction.UP;
        this.gameModel = gameModel;

        textures = new Image[4];
        //玩家游戏开启时位置
        xPos = 198;
        yPos = 498;
        //玩家的图像
        System.arraycopy(gameModel.textures, 18, textures, 0, 4);
        standardImage = textures[0];

        xVPos = xPos;
        yVPos = yPos;
        border = new Rectangle(xPos - size, yPos - size, 25, 25);
    }

    @Override
    public void move() {
        if (gameModel.isGamePaused()) {
            return;
        }

        if (coolDownTime > 0) {
            coolDownTime--;
        }
        if (invulnerableTime > 0) {
            invulnerableTime--;
        }
        //如果玩家点击“开火”键，并且满足条件，则创建一个子弹目标（即发射子弹）
        if (fire && coolDownTime == 0 && numberOfBullet > 0) {
            //子弹方向
            int c = direction.value();
            //子弹位置
            int a, b;
            if (direction.value() == Direction.UP.value()) {
                a = xPos;
                b = yPos - size;
            } else if (direction.value() == Direction.DOWN.value()) {
                a = xPos;
                b = yPos + size;
            } else if (direction.value() == Direction.LEFT.value()) {
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
                d = BulletSpeed.NORMAL.type();
            } else {
                d = BulletSpeed.ENHANCED.type();
            }
            //子弹能力
            int bulletPower;
            if (status == 4) {
                bulletPower = BulletPower.ENHANCED.type();
            } else {
                bulletPower = BulletPower.NORMAL.type();
            }
            //添加子弹
            gameModel.addActor(new Bullet(a, b, c, d, bulletPower, this, gameModel));
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
        if (movingDirection[0]) {
            if (direction.value() != Direction.UP.value() || direction.value() != Direction.DOWN.value())
                xPos = xVPos;
            yPos -= speed;
            direction = Direction.UP;
        } else if (movingDirection[1]) {
            if (direction.value() != Direction.UP.value() || direction.value() != Direction.DOWN.value())
                xPos = xVPos;
            yPos += speed;
            direction = Direction.DOWN;
        } else if (movingDirection[2]) {
            if (direction.value() != Direction.LEFT.value() && direction.value() != Direction.RIGHT.value())
                yPos = yVPos;
            xPos -= speed;
            direction = Direction.LEFT;
        } else if (movingDirection[3]) {
            if (direction.value() != Direction.LEFT.value() && direction.value() != Direction.RIGHT.value())
                yPos = yVPos;
            xPos += speed;
            direction = Direction.RIGHT;
        } else {
            notMoving = true;
        }


        if (notMoving) {
            if (speed > 0)
                speed--;
        } else {
            if (speed < 4)
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
        for (
                int i = 0;
                i < gameModel.actors.length; i++) {
            if (gameModel.actors[i] != null) {
                if (this != gameModel.actors[i]) {
                    if (border.intersects(gameModel.actors[i].getBorder())) {
                        if (gameModel.actors[i].getType() == ActorType.POWER_UP) {
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
                                        if (gameModel.actors[j].getType() == ActorType.ENEMY) {
                                            Enemy tempe = (Enemy) gameModel.actors[j];
                                            gameModel.addActor(new Bomb(tempe.xPos, tempe.yPos, BombType.BIG, gameModel));
                                            gameModel.removeActor(gameModel.actors[j]);
                                            gameModel.removeLastActor();
                                        }
                                Level.noOfEnemy = 0;
                                Level.deathCount = 20 - Level.enemyLeft;
                            } else if (function == 3) {   //防护盾，刀枪不入
                                invulnerableTime = 300 + new Random().nextInt(200);
                            } else if (function == 4) {  //冻结所有敌人
                                Enemy.freezedTime = 300 + new Random().nextInt(200);
                                Enemy.freezedMoment = Model.gameFlow;
                            } else if (function == 5) { //枪
                                if (status < 3)
                                    numberOfBullet++;
                                status = 4;
                                health = 2;
                                System.arraycopy(gameModel.textures, 84, textures, 0, 4);

                            } else if (function == 6) {  // 增加生命
                                life++;
                            }

                            gameModel.removeActor(gameModel.actors[i]);

                            MusicUtil.playAddMusic();
                        }
                        //静态对象，如墙壁，河流
                        else if (gameModel.actors[i].getType() == ActorType.STEEL_WALL || gameModel.actors[i].getType() == ActorType.WALL) {
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
                        } else if (gameModel.actors[i].getType() == ActorType.RIVER || gameModel.actors[i].getType() == ActorType.BASE) {
                            xPos = xVPos;
                            yPos = yVPos;
                            border.x = xPos - size;
                            border.y = yPos - size;

                            return;
                        }
                        //移动对象，例如敌人坦克
                        else if (gameModel.actors[i].getType() == ActorType.ENEMY || gameModel.actors[i].getType() == ActorType.PLAYER) {
                            if (!borderTemp.intersects(gameModel.actors[i].getBorder()) || gameModel.actors[i].getType() == ActorType.ENEMY) {
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
        g.drawImage(textures[direction.value()], xPos - size, yPos - size, null);
        if (invulnerableTime > 0) {
            g.setColor(Color.red);
            g.drawRect(xPos - 12, yPos - 12, 25, 25);
            g.drawRect(xPos - 11, yPos - 11, 23, 23);
        }

        //关于玩家的信息，如分数，生命等
        g.setColor(Color.yellow);
        g.drawImage(standardImage, 520, 430, null);
        g.drawString("x", 555, 445);
        g.drawString(life + "", 565, 446);
        String SCORE = "000000000" + scores;
        g.drawString(" 得分:" + "", 515, 420);
        g.drawString(SCORE.substring(SCORE.length() - 7) + "", 566, 420);
    }

    public Rectangle getBorder() {
        return border;
    }

    public ActorType getType() {
        return ActorType.PLAYER;
    }

    public void hurt() {
        if (invulnerableTime != 0)
            return;

        //如果坦克只有1级的健康状态，被击中，那么玩家坦克失去一个生命，如果玩家坦克是最后一次生命，被击中，则game over
        if (health == 1) {
            ThreadPoolFactory.getExecutor().submit(new AudioPlay(AudioFiles.BLAST).new AudioThread());//新建一个音效线程，用于播放音效

            gameModel.addActor(new Bomb(xPos, yPos, BombType.BIG, gameModel));
            life--;
            if (life == 0) {
                xPos = 100000;
                yPos = 100000;           //this will make the com.server.player never come back to the main screen, thus looks like "dead"
                border = new Rectangle(xPos - size, yPos - size, 25, 25);
                xVPos = xPos;
                yVPos = yPos;
                MusicUtil.playGameOverMusic();//播放gameOver音效
            } else {
                direction = Direction.UP;
                status = 1;
                health = 1;
                numberOfBullet = 1;
                invulnerableTime = 150;

                xPos = 198;
                yPos = 498;
                border = new Rectangle(xPos - size, yPos - size, 25, 25);
                xVPos = xPos;
                yVPos = yPos;
                System.arraycopy(gameModel.textures, 76, textures, 0, 4);
            }
        } else {
            MusicUtil.playHitMusic();
            health--;
            status = 3;
            System.arraycopy(gameModel.textures, 80, textures, 0, 4);
        }
    }

    public void upgrade() {
        //当玩家坦克吃掉正常的星星时，他的子弹将会升级
        if (status == 1) {
            status = 2;
            System.arraycopy(gameModel.textures, 76, textures, 0, 4);
        } else if (status == 2) {
            status = 3;
            numberOfBullet++;
            System.arraycopy(gameModel.textures, 80, textures, 0, 4);
        } else if (status == 3) {
            status = 4;
            health = 2;
            System.arraycopy(gameModel.textures, 84, textures, 0, 4);
        }
    }

    public void reset() {
        direction = Direction.UP;
        invulnerableTime = 150;
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

    public void numberOfBulletIncrease() {
        numberOfBullet++;
    }

    public void setScores(int scores) {
        this.scores = scores;
    }

    public int getScores() {
        return scores;
    }

    public int getLife() {
        return life;
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getFrozen() {
        return frozen;
    }

    public void setFrozen(int frozen) {
        this.frozen = frozen;
    }

    public void setMovingDirection(boolean[] movingDirection) {
        this.movingDirection = movingDirection;
    }

    public void moveDirection(Direction moveDirection) {
        switch (moveDirection){
            case UP:
                movingDirection[0] = true;
                break;
            case DOWN:
                movingDirection[1] = true;
                break;
            case LEFT:
                movingDirection[2] = true;
                break;
            case RIGHT:
                movingDirection[3] = true;
                break;
            default:
        }
    }


    public void stopMoveDirection(Direction moveDirection) {
        switch (moveDirection){
            case UP:
                movingDirection[0] = false;
                break;
            case DOWN:
                movingDirection[1] = false;
                break;
            case LEFT:
                movingDirection[2] = false;
                break;
            case RIGHT:
                movingDirection[3] = false;
                break;
            default:
        }
    }
}
