package todoListPackage;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class AddTopicEventManager extends Frame implements WindowListener
{
    private Frame frame;
    public AddTopicEventManager(Frame frame)
    {
        super();
        this.frame = frame;
    }

    @Override
    public void windowActivated(WindowEvent e)
    {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        frame.setVisible(false);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }
}
