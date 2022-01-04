package roomPackage;

import loginPackage.Login;
import loginPackage.Modification;
import mainPackage.*;
import todoListPackage.TodoList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomEstablish {
    private ClientSocket clientSocket;

    private Frame mainFrame;
    public Panel establishPanel;
    private Panel mainPanel;

    private Label titleLabel;
    private Label roomNameText;
    private Label roomIDText;
    private Label roomPWText;
    private Label roomPWCheckText;

    private Label IDConditionText;
    private Label PWConditionText;

    private TextField roomNameInput;
    private TextField roomIDInput;
    private TextField roomPWInput;
    private TextField roomPWCheckInput;

    private Button establishButton;
    private Button backButton;

    private int interval = 20;

    private int inputFieldWidth = 180;
    private int inputFieldHeight = 25;

    private int labelWidth = 100;
    private int labelHeight = 25;

    private int titleWidth = 200;
    private int titleHeight = 80;

    private int buttonWidth = 80;
    private int buttonHeight = 45;

    public RoomEstablish(Frame frame, Panel panel, ClientSocket clientSocket)
    {
        this.clientSocket = clientSocket;
        mainFrame = frame;
        frame.setSize(1280, 720);

        mainPanel = panel;
        establishPanel = new Panel();

        establishPanel.setLayout(null);
        establishPanel.setSize(frame.getWidth(), frame.getHeight());

        DrawRoomEstablish(establishPanel);
    }

    private void DrawRoomEstablish(Panel panel)
    {
        titleLabel = new Label("Room Establish");
        roomNameText = new Label("Room Name");
        roomIDText = new Label("Room ID");
        roomPWText = new Label("Room PW");
        roomPWCheckText = new Label("PW Check");

        IDConditionText = new Label("Within 5 to 15 characters. Contain numbers only.");
        PWConditionText = new Label("Within 5 to 15 characters. Mix English and numbers.");

        roomNameInput = new TextField("", 10);
        roomIDInput = new TextField("", 10);
        roomPWInput = new TextField("", 10);
        roomPWCheckInput = new TextField("", 10);
        establishButton = new Button("Establish");
        backButton = new Button("Back to Login");

        roomPWInput.setEchoChar('*');
        roomPWCheckInput.setEchoChar('*');

        titleLabel.setBounds(panel.getWidth() / 2 - titleWidth / 2, panel.getHeight() / 4, titleWidth,titleHeight);
        titleLabel.setAlignment(Label.CENTER);
        Font titleFont = new Font("Consolas", Font.PLAIN, 20);
        titleLabel.setFont(titleFont);

        roomIDInput.setBounds(panel.getWidth() / 2 - inputFieldWidth / 2 - interval, panel.getHeight() / 2 - inputFieldHeight / 2 - interval / 2, inputFieldWidth, inputFieldHeight);
        roomNameInput.setBounds(roomIDInput.getX(), roomIDInput.getY() - 2 * interval - inputFieldHeight, inputFieldWidth, inputFieldHeight);
        roomPWInput.setBounds(roomNameInput.getX(), roomIDInput.getY() + 2 * interval + inputFieldHeight, inputFieldWidth, inputFieldHeight);
        roomPWCheckInput.setBounds(roomNameInput.getX(), roomPWInput.getY() + 2 * interval + inputFieldHeight, inputFieldWidth, inputFieldHeight);

        roomNameText.setBounds(roomNameInput.getX() - labelWidth - inputFieldHeight, roomNameInput.getY(), labelWidth, labelHeight);
        roomIDText.setBounds(roomNameInput.getX() - labelWidth - inputFieldHeight, roomIDInput.getY(), labelWidth, labelHeight);
        roomPWText.setBounds(roomNameInput.getX() - labelWidth - inputFieldHeight, roomPWInput.getY(), labelWidth, labelHeight);
        roomPWCheckText.setBounds(roomNameInput.getX() - labelWidth - inputFieldHeight, roomPWCheckInput.getY(), labelWidth, labelHeight);

        IDConditionText.setBounds(roomIDInput.getX() - 2 * interval, roomIDInput.getY() + inputFieldHeight, labelWidth * 3, labelHeight);
        PWConditionText.setBounds(roomPWInput.getX() - 2 * interval, roomPWInput.getY() + inputFieldHeight, labelWidth * 3, labelHeight);


        roomNameText.setAlignment(Label.RIGHT);
        roomIDText.setAlignment(Label.RIGHT);
        roomPWText.setAlignment(Label.RIGHT);
        roomPWCheckText.setAlignment(Label.RIGHT);

        establishButton.setBounds(roomIDInput.getLocation().x + roomIDInput.getWidth() + 3 * interval, panel.getHeight() / 2 - buttonHeight / 2, buttonWidth, buttonHeight);
        backButton.setBounds(panel.getWidth() - buttonWidth - interval, interval, buttonWidth, buttonHeight);

        establishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { EstablishRoom(); }
        });

        backButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BackToRoomLogin();
                    }
                }
        );

        panel.add(titleLabel);
        panel.add(roomNameInput);
        panel.add(roomNameText);
        panel.add(roomIDInput);
        panel.add(roomIDText);
        panel.add(roomPWText);
        panel.add(roomPWInput);
        panel.add(roomPWCheckInput);
        panel.add(roomPWCheckText);
        panel.add(establishButton);
        panel.add(backButton);
        panel.add(IDConditionText);
        panel.add(PWConditionText);
    }

    private void EstablishRoom()
    {
        String name, id, pw, pwCheck;
        name = roomNameInput.getText();
        id = roomIDInput.getText();
        pw = roomPWInput.getText();
        pwCheck = roomPWCheckInput.getText();

        if(ValidTest.roomIDCheck(id) && ValidTest.pwCheck(pw, pwCheck))
        {
            if(!MainClient.withoutServerTest) {
                Message roomEstablishMessage = new Message(Task.room_establish);
                roomEstablishMessage.AddContent(ContentType.room_name, name);
                roomEstablishMessage.AddContent(ContentType.room_id, id);
                roomEstablishMessage.AddContent(ContentType.room_pw, pw);
                roomEstablishMessage.AddContent(ContentType.user_id, Login.GetUserID());

                Message rcvMessage = clientSocket.SendToServer(roomEstablishMessage);

                if (rcvMessage.result) {
                    SetActive(false);
                    RoomLogin.SetRoomID(id);
                    MainClient.roomManagement= new RoomManagement(mainFrame, mainPanel, clientSocket);
                    MainClient.todoList = new TodoList(mainFrame, mainPanel, clientSocket);
                    MainClient.modification = new Modification(mainFrame, mainPanel, clientSocket);
                    MainClient.todoList.SetActive(true);
                    JOptionPane.showMessageDialog(null, rcvMessage.GetContent(0), "Success Message", JOptionPane.PLAIN_MESSAGE);
                }
            }
            else
            {
                MainClient.roomManagement= new RoomManagement(mainFrame, mainPanel, clientSocket);
                MainClient.todoList = new TodoList(mainFrame, mainPanel, clientSocket);
                MainClient.modification = new Modification(mainFrame, mainPanel, clientSocket);
                SetActive(false);
                MainClient.todoList.SetActive(true);
            }
        }
    }

    private void BackToRoomLogin()
    {
        MainClient.roomLogin.SetActive(true);
        SetActive(false);
    }

    public void SetActive(boolean active)
    {
        if(active)
        {
            mainPanel.add(establishPanel);
        }
        else
        {
            mainPanel.remove(establishPanel);
        }
    }
}
