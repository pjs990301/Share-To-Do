package loginPackage;

import mainPackage.*;
import roomPackage.RoomEstablish;
import roomPackage.RoomLogin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login{
    private ClientSocket clientSocket;
    private Frame mainFrame;
    public Panel loginPanel;
    private Panel mainPanel;

    private Label titleLabel;
    static TextField ID;
    static TextField PW;
    private Label IDText;
    private Label PWText;

    private Button logIn;
    private Button joinIn;

    private int interval = 20;

    private int inputFieldWidth = 180;
    private int inputFieldHeight = 25;

    private int titleWidth = 200;
    private int titleHeight = 80;

    private int labelWidth = 100;
    private int labelHeight = 25;

    private int buttonWidth = 80;
    private int buttonHeight = 45;

    private static String user_id;

    public Login(Frame frame, Panel panel, ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
        mainFrame = frame;
        loginPanel = new Panel();
        mainPanel = panel;

        loginPanel.setLayout(null);
        loginPanel.setSize(frame.getWidth(), frame.getHeight());

        DrawLogin(loginPanel);
    }

    public void SetActive(boolean active) {
        if(active) {
            mainPanel.add(loginPanel);
        }
        else {
            mainPanel.remove(loginPanel);
        }
    }

    public static String GetUserID()
    {
        return user_id;
    }

    public static void LogoutUser()
    {
        user_id = null;
    }

    public void DrawLogin(Panel panel) {
        titleLabel = new Label("Member Login");
        IDText = new Label("ID");
        PWText = new Label("PW");
        ID = new TextField("", 10);
        PW = new TextField("", 10);
        logIn = new Button("Login");
        joinIn = new Button("Join In");

        PW.setEchoChar('*');

        titleLabel.setBounds(panel.getWidth() / 2 - titleWidth / 2, panel.getHeight() / 4, titleWidth,titleHeight);
        titleLabel.setAlignment(Label.CENTER);
        Font titleFont = new Font("Consolas", Font.PLAIN, 20);
        titleLabel.setFont(titleFont);
        ID.setBounds(panel.getWidth() / 2 - inputFieldWidth / 2 - interval, panel.getHeight() / 2 - inputFieldHeight / 2 - interval, inputFieldWidth, inputFieldHeight);
        PW.setBounds(panel.getWidth() / 2 - inputFieldWidth / 2 - interval, panel.getHeight() / 2 - inputFieldHeight / 2 + interval, inputFieldWidth, inputFieldHeight);
        IDText.setBounds(ID.getLocation().x - ID.getWidth() / 2 - interval, ID.getLocation().y, labelWidth, labelHeight);
        PWText.setBounds(PW.getLocation().x - PW.getWidth() / 2 - interval, PW.getLocation().y, labelWidth, labelHeight);
        IDText.setAlignment(Label.RIGHT);
        PWText.setAlignment(Label.RIGHT);

        logIn.setBounds(ID.getLocation().x + ID.getWidth() + interval, panel.getHeight() / 2 - buttonHeight / 2, buttonWidth, buttonHeight);
        joinIn.setBounds(logIn.getLocation().x + logIn.getWidth() + interval, logIn.getY(), buttonWidth, buttonHeight);

        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginProcess();
            }
        });
        joinIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                joinIn();
            }
        });

        panel.add(titleLabel);
        panel.add(IDText);
        panel.add(PWText);
        panel.add(ID);
        panel.add(PW);
        panel.add(logIn);
        panel.add(joinIn);
    }

    public void LoginProcess() {
        String id, pw;
        id = ID.getText();
        pw = PW.getText();

        if(!MainClient.withoutServerTest) {
            Message loginMessage = new Message(Task.login);
            loginMessage.AddContent(ContentType.user_id, id);
            loginMessage.AddContent(ContentType.user_pw, pw);
            Message rcvMessage = clientSocket.SendToServer(loginMessage);

            if (rcvMessage.result) {
                MainClient.roomLogin =  new RoomLogin(mainFrame, mainPanel, clientSocket);
                MainClient.roomEstablish = new RoomEstablish(mainFrame, mainPanel, clientSocket);
                user_id = id;
                SetActive(false);
                MainClient.roomLogin.SetActive(true);
            }
        }
        else
        {
            MainClient.roomLogin =  new RoomLogin(mainFrame, mainPanel, clientSocket);
            MainClient.roomEstablish = new RoomEstablish(mainFrame, mainPanel, clientSocket);
            SetActive(false);
            MainClient.roomLogin.SetActive(true);
        }
    }

    public void joinIn() {
        MainClient.joinIn.SetActive(true);
        SetActive(false);
    }
}
