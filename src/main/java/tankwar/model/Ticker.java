package tankwar.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 程序运行线程
 */
public class Ticker implements Runnable {
    private ActionListener actionListener;

    public Ticker(int i) {
        Thread t = new Thread(this);
        t.start();
    }

    public void addActionListener(ActionListener actionlistener) {
        if (actionListener == null) {
            actionListener = actionlistener;
        } else {
            System.out.println("WARNING: ActionListener already added to com.server.Ticker.");
        }
    }

    public void run() {
        ActionEvent actionevent = new ActionEvent(this, 0, null);
        actionListener.actionPerformed(actionevent);
    }
}


