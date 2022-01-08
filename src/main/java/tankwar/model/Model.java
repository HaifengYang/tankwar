package tankwar.model;

import tankwar.constant.Direction;
import tankwar.entity.*;
import tankwar.utils.MusicUtil;
import tankwar.view.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 模型类
 */
public class Model implements ActionListener {
    private final View view;

    private boolean gameStarted;
    private boolean gamePaused;
    private boolean gameOver;

    private volatile boolean whetherContinue;

    public Image[] textures;

    public static int gameFlow;
    public Actor[] actors;
    public Player player;

    public String[] messageQueue;
    public int messageIndex;

    public Model(View view) {
        this.view = view;
        messageQueue = new String[8];
        whetherContinue = false;
        view.mainPanel.messageQueue = messageQueue;
        Ticker t = new Ticker();
        t.addActionListener(this);
    }


    public void createServer() {
        textures = new Image[88];
        for (int i = 1; i < textures.length + 1; i++)
            textures[i - 1] = Toolkit.getDefaultToolkit().getImage(Model.class.getClassLoader().getResource("image/" + i + ".jpg"));
        actors = new Actor[400];
        Level.loadLevel(this);

        player = new Player(this);
        addActor(player);

        gameStarted = true;
        view.mainPanel.actors = actors;
        view.mainPanel.gameStarted = true;

        MusicUtil.playStartMusic();// 播放背景音效

    }

    public void actionPerformed(ActionEvent e) {
        createServer();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                if (!gamePaused) gameFlow++;
                gameOver();
                restart();
                winningCountHandle();
                spawnEnemy();
                acotorsMove();

                if (gameFlow % 300 == 0)
                    removeMessage();

                view.mainPanel.repaint();

                if (!view.mainPanel.hasFocus()) {
                    player.setMovingDirection(new boolean[4]);
                    player.setFire(false);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                gameStarted = false;
                gameOver = false;
                gameFlow = 0;
                Enemy.freezedTime = 0;
                Enemy.freezedMoment = 0;
                view.mainPanel.gameStarted = false;

                player = null;
                Level.reset();
            }
        }, 0, 30, TimeUnit.MILLISECONDS);
    }

    private void acotorsMove() {
        for (Actor actor : actors) {
            if (actor != null)
                actor.move();
        }
    }

    private void spawnEnemy() {
        if (!gamePaused)
            Level.spawnEnemy(this);
    }

    private void winningCountHandle() {
        if (Level.deathCount == 20 && !gameOver) {
            Level.winningCount++;
            if (Level.winningCount == 120) {
                player.setFrozen(1);
            }
            if (Level.winningCount == 470) {
                if (player.getLife() > 0)
                    player.reset();
                Level.loadLevel(this);
            }
            if (Level.winningCount == 500) {
                player.setFrozen(0);
                Level.deathCount = 0;
                Level.winningCount = 0;
            }
        }
    }

    private void gameOver() {
        if (gameOver || player.getLife() == 0) {
            gameOver = true;
            player.setFrozen(1);
        }
    }

    public void restart() {
        if (whetherContinue) {
            player = new Player(this);
            if (player.getLife() == 0) {
                Level.reset();
            } else {
                Level.goOn();
            }
            Level.loadLevel(this);
            gameOver = false;
            Enemy.freezedMoment = 0;
            Enemy.freezedTime = 0;
            gameFlow = 0;
            gamePaused = false;
            whetherContinue = false;
        }
    }


    public void addActor(Actor actor) {
        for (int i = 0; i < actors.length; i++)
            if (actors[i] == null) {
                actors[i] = actor;
                break;
            }
    }

    public void removeActor(Actor actor) {
        for (int i = 0; i < actors.length; i++)
            if (actors[i] == actor) {
                actors[i] = null;
                break;
            }
    }

    public void addMessage(String message) {
        if (messageIndex < 8) {
            messageQueue[messageIndex] = message;
            messageIndex++;
        } else {
            System.arraycopy(messageQueue, 1, messageQueue, 0, 7);
            messageQueue[7] = message;
        }

        if (!gameStarted)
            view.mainPanel.repaint();
    }

    public void removeMessage() {
        if (messageIndex == 0)
            return;

        messageIndex--;
        if (messageIndex >= 0) System.arraycopy(messageQueue, 1, messageQueue, 0, messageIndex);
        messageQueue[messageIndex] = null;

        if (!gameStarted)
            view.mainPanel.repaint();
    }

    public void setWhetherContinue(boolean whetherContinue) {
        this.whetherContinue = whetherContinue;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isGamePaused() {
        return gamePaused;
    }

    public void setGamePaused(boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void playerMoveTowards(Direction moveDirection){
        this.player.moveDirection(moveDirection);
    }

    public void playerStopMoveTowards(Direction moveDirection){
        this.player.stopMoveDirection(moveDirection);
    }

    public void playerFire(boolean fire){
        this.player.setFire(fire);
    }
}