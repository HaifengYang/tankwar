package tankwar.view;

import tankwar.model.Model;
import tankwar.controller.Controller;

import javax.swing.*;
import java.awt.*;


//这个类表示服务器的图形界面
public class View extends JFrame{
	public DrawingPanel mainPanel;
	public JButton createServer, exit, pauseAndResume, newGame, hiddenButton;

	public Controller controler;
	public Model model;

	public View(){
		super("坦克大战");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }

		getContentPane().setLayout(null);

		//制作动画绘制的主面板
		mainPanel = new DrawingPanel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(0,  22, 679, 605);
		mainPanel.setBackground(new Color(128, 64, 0));

		getContentPane().add(mainPanel);
		mainPanel.setFocusable(true);

		//制作选项按钮
		createServer = new JButton("启动游戏");
		createServer.setBounds(0, 0,120,22);
		getContentPane().add(createServer);
		createServer.setFocusable(false);

		pauseAndResume = new JButton("暂停/继续");
		pauseAndResume.setBounds(120, 0,120,22);
		getContentPane().add(pauseAndResume);
		pauseAndResume.setFocusable(false);

		newGame = new JButton("重新开始");
		newGame.setBounds(240, 0,120,22);
		getContentPane().add(newGame);
		newGame.setFocusable(false);

		exit = new JButton("退出");
		exit.setBounds(360, 0,120,22);
		getContentPane().add(exit);
		exit.setFocusable(false);

		//设置主框架
		setBounds(150, 130, 640, 590);
    	setVisible(true);
    	setResizable( false );

		//设置服务器模式
		model = new Model(this);

		//设置服务器控制器
		controler = new Controller(this, model);
	}

	public static void main(String[] args){
		new View();
	}
}