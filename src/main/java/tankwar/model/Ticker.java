package tankwar.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 程序运行线程
 */
public class Ticker implements Runnable{
	private ActionListener actionListener;
	private boolean isTicking;
	private final int delay;

	public Ticker(int i){
		delay = i;
		Thread t = new Thread(this);
		t.start();
		isTicking = false;
	}

	public void addActionListener(ActionListener actionlistener){
		if(actionListener == null) {
			actionListener = actionlistener;
		}
		else {
			System.out.println("WARNING: ActionListener already added to com.server.Ticker.");
		}
	}

	public void start(){
		isTicking = true;
	}

	public void stop(){
		isTicking = false;
	}

	private void performing(){
		if (isTicking) {
			ActionEvent actionevent = new ActionEvent(this, 0, null);
			actionListener.actionPerformed(actionevent);
		}
	}
	
	public void run(){
		do{
			performing();
			try{
				Thread.sleep(delay);
			}catch(InterruptedException interruptedexception){
				System.out.println("WARNING: com.server.Ticker thread interrupted.");
			}
		} while(true);
	}
}


