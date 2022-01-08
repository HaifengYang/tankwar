package tankwar.controller;

import tankwar.constant.Direction;
import tankwar.model.Model;
import tankwar.view.View;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//这个类处理来自视图的输入
public class Controller {
    public View view;
    public Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        //操作暂停/继续按钮的动作
        view.pauseAndResume.addActionListener(e -> {
                    if (!model.isGameOver() && model.isGameStarted()) {
                        model.setGamePaused(!model.isGamePaused());
                    }
                }
        );
        //游戏重启按钮的动作
        view.newGame.addActionListener(e -> model.setWhetherContinue(true)
        );

        //操作帮助按钮的动作
        view.helper.addActionListener(e -> model.addMessage("帮助: 按 空格 键开火,  按键盘的方向键来控制坦克的移动")
        );
        //操作退出按钮的动作
        view.exit.addActionListener(e -> System.exit(0)
        );

        JPanel panel = view.mainPanel;
        panel.addKeyListener(new KeyAdapter() {
                                 public void keyPressed(KeyEvent e) {
                                     if (model.player != null) {
                                         switch (e.getKeyCode()) {
                                             case (KeyEvent.VK_UP):
                                                 model.playerMoveTowards(Direction.UP);
                                                 break;
                                             case (KeyEvent.VK_DOWN):
                                                 model.playerMoveTowards(Direction.DOWN);
                                                 break;
                                             case (KeyEvent.VK_LEFT):
                                                 model.playerMoveTowards(Direction.LEFT);
                                                 break;
                                             case (KeyEvent.VK_RIGHT):
                                                 model.playerMoveTowards(Direction.RIGHT);
                                                 break;
                                             default:
                                         }

                                         if (e.getKeyCode() == KeyEvent.VK_SPACE)
                                             model.playerFire(true);

                                     }
                                 }

                                 public void keyReleased(KeyEvent e) {
                                     if (model.player != null) {
                                         if (e.getKeyCode() == KeyEvent.VK_UP) {
                                             model.playerStopMoveTowards(Direction.UP);
                                         } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                             model.playerStopMoveTowards(Direction.DOWN);
                                         } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                             model.playerStopMoveTowards(Direction.LEFT);
                                         } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                             model.playerStopMoveTowards(Direction.RIGHT);
                                         }

                                         if (e.getKeyCode() == KeyEvent.VK_SPACE)
                                             model.playerFire(false);
                                     }
                                 }
                             }
        );

    }
}