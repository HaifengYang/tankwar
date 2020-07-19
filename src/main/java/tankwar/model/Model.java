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

    public boolean gameStarted;
    public boolean serverCreated;
    public boolean gamePaused;
    public boolean gameOver;
    public boolean serverVoteYes;
    public Ticker t;
    /**
     * 播放gameOver的标识
     */
    private boolean isBroadcastGameOver = false;
    public Image[] textures;

    public static int gameFlow;
    public Actor[] actors;
    public Player player;

    public Model(View view) {
        this.view = view;
        t = new Ticker(1000);
        t.addActionListener(this);
    }


    public void createServer() {
        textures = new Image[88];
        for (int i = 1; i < textures.length + 1; i++)
            textures[i - 1] = Toolkit.getDefaultToolkit().getImage(Model.class.getClassLoader().getResource("image/" + i + ".jpg"));
        actors = new Actor[400];
        Level.loadLevel(this);

        player = new Player( this);
        addActor(player);

        serverCreated = true;
        gameStarted = true;
        view.mainPanel.actors = actors;
        view.mainPanel.gameStarted = true;

        new AudioPlay(AudioUtil.START).new AudioThread().start();// 播放背景音效

    }

    public void actionPerformed(ActionEvent e) {
        if (!serverCreated) {
            createServer();
            return;
        }

        try {
            while (true) {
                if (!gamePaused) gameFlow++;
                gameOver();
                winningCountHandle();
                spawnEnemy();
                acotorsMove();

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
            serverVoteYes = false;
            serverCreated = false;
            gameStarted = false;
            gameOver = false;
            gameFlow = 0;
            Enemy.freezedTime = 0;
            Enemy.freezedMoment = 0;
            view.mainPanel.gameStarted = false;
            t.stop();

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

            if (!isBroadcastGameOver){
                new AudioPlay(AudioUtil.GAMEOVER).new AudioThread().start();//新建一个音效线程，用于播放音效
                isBroadcastGameOver = true;
            }
            restart();
        }
    }

    private void restart() {
        if (serverVoteYes) {
            player = new Player(this);
            if (player.getLife() == 0){
                Level.reset();
            }else {
                Level.goOn();
            }
            Level.loadLevel(this);
            gameOver = false;
            serverVoteYes = false;
            isBroadcastGameOver = false;
            Enemy.freezedMoment = 0;
            Enemy.freezedTime = 0;
            gameFlow = 0;
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

}