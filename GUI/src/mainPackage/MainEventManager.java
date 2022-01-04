package mainPackage;

import java.awt.Button;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainEventManager extends Frame implements WindowListener
{
    private ClientSocket clientSocket;
    public MainEventManager(ClientSocket clientSocket)
    {
        super();
        this.clientSocket = clientSocket;
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
        clientSocket.CloseConnection();
        System.out.println("Window close");
        System.exit(0);
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
