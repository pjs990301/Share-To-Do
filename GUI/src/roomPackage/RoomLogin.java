package roomPackage;

import loginPackage.Login;
import loginPackage.Modification;
import mainPackage.*;
import todoListPackage.TodoList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomLogin{
    private ClientSocket clientSocket;

    private Frame mainFrame;
    public Panel loginPanel;
    private Panel mainPanel;

    private Label titleLabel;
    private TextField roomIDInput;
    private TextField roomPWInput;
    private Label roomIDText;
    private Label roomPWText;

    private Button logIn;
    private Button createRoom;
    private Button logOut;

    private int interval = 20;

    private int inputFieldWidth = 180;
    private int inputFieldHeight = 25;

    private int titleWidth = 200;
    private int titleHeight = 80;

    private int labelWidth = 100;
    private int labelHeight = 25;

    private int buttonWidth = 80;
    private int buttonHeight = 45;

    private static String roomID;

    public RoomLogin(Frame frame, Panel panel, ClientSocket clientSocket)
    {
        mainFrame = frame;
        this.clientSocket = clientSocket;
        loginPanel = new Panel();
        mainPanel = panel;

        loginPanel.setLayout(null);
        loginPanel.setSize(frame.getWidth(), frame.getHeight());

        DrawRoomLogin(loginPanel);
    }

    public void SetActive(boolean active)
    {
        if(active)
        {
            mainPanel.add(loginPanel);
        }
        else
        {
            mainPanel.remove(loginPanel);
        }
    }

    public static String GetRoomID()
    {
        return roomID;
    }

    public static void SetRoomID(String newID)
    {
        roomID = newID;
    }

    public static void RoomLogout()
    {
        roomID = null;
    }

    public void DrawRoomLogin(Panel panel)
    {
        titleLabel = new Label("Room Login");
        roomIDText = new Label("Room ID");
        roomPWText = new Label("Room PW");
        roomIDInput = new TextField("", 10);
        roomPWInput = new TextField("", 10);
        logIn = new Button("Room Login");
        createRoom = new Button("Room Create");
        logOut = new Button("Log Out");

        roomPWInput.setEchoChar('*');

        titleLabel.setBounds(panel.getWidth() / 2 - titleWidth / 2, panel.getHeight() / 4, titleWidth,titleHeight);
        titleLabel.setAlignment(Label.CENTER);
        Font titleFont = new Font("Consolas", Font.PLAIN, 20);
        titleLabel.setFont(titleFont);

        roomIDInput.setBounds(panel.getWidth() / 2 - inputFieldWidth / 2 - interval, panel.getHeight() / 2 - inputFieldHeight / 2 - interval, inputFieldWidth, inputFieldHeight);
        roomPWInput.setBounds(panel.getWidth() / 2 - inputFieldWidth / 2 - interval, panel.getHeight() / 2 - inputFieldHeight / 2 + interval, inputFieldWidth, inputFieldHeight);

        roomIDText.setBounds(roomIDInput.getLocation().x - roomIDInput.getWidth() / 2 - interval, roomIDInput.getLocation().y, labelWidth, labelHeight);
        roomPWText.setBounds(roomPWInput.getLocation().x - roomPWInput.getWidth() / 2 - interval, roomPWInput.getLocation().y, labelWidth, labelHeight);
        roomIDText.setAlignment(Label.RIGHT);
        roomPWText.setAlignment(Label.RIGHT);

        logIn.setBounds(roomIDInput.getLocation().x + roomIDInput.getWidth() + interval, panel.getHeight() / 2 - buttonHeight / 2, buttonWidth, buttonHeight);
        createRoom.setBounds(logIn.getLocation().x + logIn.getWidth() + interval, logIn.getY(), buttonWidth, buttonHeight);
        logOut.setBounds(panel.getWidth() - buttonWidth - interval, interval, buttonWidth, buttonHeight);

        logIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login();
            }
        });
        createRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateRoom();
            }
        });
        logOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogOut();
            }
        });

        panel.add(titleLabel);
        panel.add(roomIDText);
        panel.add(roomPWText);
        panel.add(roomIDInput);
        panel.add(roomPWInput);
        panel.add(logIn);
        panel.add(createRoom);
        panel.add(logOut);
    }

    public void Login()
    {
        String id, pw;
        id = roomIDInput.getText();
        pw = roomPWInput.getText();

        if(!MainClient.withoutServerTest) {
            Message roomLoginMessage = new Message(Task.room_login);
            roomLoginMessage.AddContent(ContentType.room_id, id);
            roomLoginMessage.AddContent(ContentType.room_pw, pw);
            roomLoginMessage.AddContent(ContentType.user_id, Login.GetUserID());
            Message rcvMessage = clientSocket.SendToServer(roomLoginMessage);

            if (rcvMessage.result) {
                roomID = id;
                SetActive(false);
                MainClient.todoList = new TodoList(mainFrame, mainPanel, clientSocket);
                MainClient.modification = new Modification(mainFrame, mainPanel, clientSocket);
                MainClient.todoList.SetActive(true);
            }
        }
        else
        {
            MainClient.todoList = new TodoList(mainFrame, mainPanel, clientSocket);
            MainClient.modification = new Modification(mainFrame, mainPanel, clientSocket);
            SetActive(false);
            MainClient.todoList.SetActive(true);
        }
    }

    public void CreateRoom()
    {
        MainClient.roomEstablish.SetActive(true);
        SetActive(false);
    }

    private void LogOut()
    {
        SetActive(false);
        MainClient.login.SetActive(true);
        Login.LogoutUser();
    }
}
