package tankwar.model;

import tankwar.config.ThreadPoolFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 程序运行线程
 */
public class Ticker implements Runnable {
    private ActionListener actionListener;

    public Ticker() {
        ThreadPoolFactory.getExecutor().submit(this);
    }

    public void addActionListener(ActionListener actionlistener) {
        if (actionListener == null) {
            actionListener = actionlistener;
        } else {
            System.out.println("WARNING: ActionListener already added to com.server.Ticker.");
        }
    }

    @Override
    public void run() {
        ActionEvent actionevent = new ActionEvent(this, 0, null);
        actionListener.actionPerformed(actionevent);
    }
}


