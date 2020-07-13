package tankwar.controller;

import tankwar.model.Model;
import tankwar.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

//这个类处理来自视图的输入
public class Controller {
    public View view;
    public Model model;

    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;

        //操作建立主机按钮的动作
        view.createServer.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               if (!model.serverCreated)
                  model.t.start();
           }
        });

        //操作暂停/继续按钮的动作
        view.pauseAndResume.addActionListener(new ActionListener() {
                                                  public void actionPerformed(ActionEvent e) {
                                                      if (!model.gameOver && model.gameStarted) {
                                                          model.gamePaused = !model.gamePaused;
                                                      }
                                                  }
                                              }
        );
        //游戏重启按钮的动作
        view.newGame.addActionListener(new ActionListener() {
                                           public void actionPerformed(ActionEvent e) {
                                               model.serverVoteYes = true;
                                           }
                                       }
        );
        //操作退出按钮的动作
        view.exit.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            System.exit(0);
                                        }
                                    }
        );

        JPanel panel = view.mainPanel;
        panel.addKeyListener(new KeyAdapter() {
                                 public void keyPressed(KeyEvent e) {
                                     if (model.player != null) {
                                         if (e.getKeyCode() == KeyEvent.VK_UP) {
                                             model.player.setMoveUp(true);
                                             model.player.setMoveDown(false);
                                             model.player.setMoveLeft(false);
                                             model.player.setMoveRight(false);
                                         }
                                         if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                             model.player.setMoveDown(true);
                                             model.player.setMoveUp(false);
                                             model.player.setMoveLeft(false);
                                             model.player.setMoveRight(false);
                                         }
                                         if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                                             model.player.setMoveLeft(true);
                                             model.player.setMoveUp(false);
                                             model.player.setMoveDown(false);
                                             model.player.setMoveRight(false);
                                         }
                                         if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                                             model.player.setMoveLeft(false);
                                             model.player.setMoveUp(false);
                                             model.player.setMoveDown(false);
                                             model.player.setMoveRight(true);
                                         }
                                         if (e.getKeyCode() == KeyEvent.VK_SPACE)
                                             model.player.setFire(true);

                                     }
                                 }

                                 public void keyReleased(KeyEvent e) {
                                     if (model.player != null) {
                                         if (e.getKeyCode() == KeyEvent.VK_UP)
                                             model.player.setMoveUp(false);
                                         if (e.getKeyCode() == KeyEvent.VK_DOWN)
                                             model.player.setMoveDown(false);
                                         if (e.getKeyCode() == KeyEvent.VK_LEFT)
                                             model.player.setMoveLeft(false);
                                         if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                                             model.player.setMoveRight(false);
                                         if (e.getKeyCode() == KeyEvent.VK_SPACE)
                                             model.player.setFire(false);
                                     }
                                 }
                             }
        );

    }
}