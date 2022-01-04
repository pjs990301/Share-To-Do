package mainPackage;

import loginPackage.JoinIn;
import loginPackage.Login;
import loginPackage.Modification;
import roomPackage.RoomEstablish;
import roomPackage.RoomLogin;
import roomPackage.RoomManagement;
import todoListPackage.TodoList;

import java.awt.*;

public class MainClient {

    public static RoomLogin roomLogin;
    public static RoomEstablish roomEstablish;
    public static RoomManagement roomManagement;

    public static Login login;
    public static JoinIn joinIn;
    public static Modification modification;

    public static TodoList todoList;

    public static boolean withoutServerTest = false;
    public static int synchronizeDelay = 3;

    public static void main(String[] args)
    {
        ClientSocket clientSocket = new ClientSocket();
        Frame mainFrame = new Frame("Share To-Do");

        mainFrame.setSize(1280, 720);
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());

        panel.setSize(mainFrame.getWidth(), mainFrame.getHeight());
        mainFrame.add(panel);

        login =  new Login(mainFrame, panel, clientSocket);
        joinIn = new JoinIn(mainFrame, panel, clientSocket);

        login.SetActive(true);

        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.addWindowListener(new MainEventManager(clientSocket));
    }
}
