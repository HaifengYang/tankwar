package tankwar.model;

import tankwar.entity.Actor;
import tankwar.entity.Enemy;
import tankwar.entity.Level;
import tankwar.entity.Player;
import tankwar.utils.AudioPlay;
import tankwar.utils.AudioUtil;
import tankwar.view.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 模型类
 */
public class Model implements ActionListener {
    public View view;

    private boolean gameStarted;
    private boolean serverCreated;
    private boolean gamePaused;
    private boolean gameOver;
    private volatile boolean serverVoteYes;
    private Ticker t;
    /**
     * 播放gameOver的标识
     */
    private boolean isBroadcastGameOver = false;
    public Image[] textures;

    public static int gameFlow;
    public Actor[] actors;
    public Player player;

    public String[] messageQueue;
    public int messageIndex;

    public Model(View view) {
        this.view = view;
        messageQueue = new String[8];
        serverVoteYes = false;
        view.mainPanel.messageQueue = messageQueue;
        t = new Ticker(1000);
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

        serverCreated = true;
        gameStarted = true;
        view.mainPanel.actors = actors;
        view.mainPanel.gameStarted = true;

        new AudioPlay(AudioUtil.START).new AudioThread().start();// 播放背景音效

    }

    public void actionPerformed(ActionEvent e) {
        createServer();
        try {
            while (true) {
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
                    player.setMoveLeft(false);
                    player.setMoveUp(false);
                    player.setMoveDown(false);
                    player.setMoveRight(false);
                    player.setFire(false);
                }

                Thread.sleep(30);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            serverCreated = false;
            gameStarted = false;
            gameOver = false;
            gameFlow = 0;
            Enemy.freezedTime = 0;
            Enemy.freezedMoment = 0;
            view.mainPanel.gameStarted = false;

            player = null;
            Level.reset();
        }
    }

    private void acotorsMove() {
        for (int i = 0; i < actors.length; i++) {
            if (actors[i] != null)
                actors[i].move();
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
                player.setFreezed(1);
            }
            if (Level.winningCount == 470) {
                if (player.getLife() > 0)
                    player.reset();
                Level.loadLevel(this);
            }
            if (Level.winningCount == 500) {
                player.setFreezed(0);
                Level.deathCount = 0;
                Level.winningCount = 0;
            }
        }
    }

    private void gameOver() {
        if (gameOver || player.getLife() == 0) {
            gameOver = true;
            player.setFreezed(1);

            if (!isBroadcastGameOver) {
                new AudioPlay(AudioUtil.GAMEOVER).new AudioThread().start();//新建一个音效线程，用于播放音效
                isBroadcastGameOver = true;
            }
        }
    }

    public void restart() {
        if (serverVoteYes) {
            player = new Player(this);
            if (player.getLife() == 0) {
                Level.reset();
            } else {
                Level.goOn();
            }
            Level.loadLevel(this);
            gameOver = false;
            isBroadcastGameOver = false;
            Enemy.freezedMoment = 0;
            Enemy.freezedTime = 0;
            gameFlow = 0;
            serverVoteYes = false;

            new AudioPlay(AudioUtil.START).new AudioThread().start();// 播放背景音效
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
            for (int i = 0; i < 7; i++)
                messageQueue[i] = messageQueue[i + 1];
            messageQueue[7] = message;
        }

        if (!gameStarted)
            view.mainPanel.repaint();
    }

    public void removeMessage() {
        if (messageIndex == 0)
            return;

        messageIndex--;
        for (int i = 0; i < messageIndex; i++)
            messageQueue[i] = messageQueue[i + 1];
        messageQueue[messageIndex] = null;

        if (!gameStarted)
            view.mainPanel.repaint();
    }

    public void setServerVoteYes(boolean serverVoteYes) {
        this.serverVoteYes = serverVoteYes;
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
}