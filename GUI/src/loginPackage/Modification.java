package loginPackage;

import mainPackage.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Modification {
    private ClientSocket clientSocket;
    public Panel modificationPanel;
    private Panel mainPanel;

    private Label titleLabel;
    private Label curIDText;
    private Label curPWText;
    private Label newPWText;
    private Label newPWCheckText;
    private Label nameText;
    private Label birthText;
    private Label emailText;
    private Label telText;

    private TextField curIDInput;
    private TextField curPWInput;
    private TextField newPWInput;
    private TextField newPWCheckInput;
    private TextField nameInput;
    private TextField birthInput;
    private TextField emailInput;
    private TextField telInput;

    private Label PWConditionText;
    private Label birthConditionText;
    private Label telConditionText;

    private Button Button;
    private Button backButton;

    private int interval = 20;

    private int inputFieldWidth = 150;
    private int inputFieldHeight = 25;

    private int labelWidth = 150;
    private int labelHeight = 25;

    private int titleWidth = 400;
    private int titleHeight = 80;

    private int buttonWidth = 80;
    private int buttonHeight = 45;

    public Modification(Frame frame, Panel panel, ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
        frame.setSize(1280, 720);

        mainPanel = panel;
        modificationPanel = new Panel();

        modificationPanel.setLayout(null);
        modificationPanel.setSize(frame.getWidth(), frame.getHeight());

        DrawModification(modificationPanel);
    }

    private void DrawModification(Panel panel) {
        titleLabel = new Label("User Information Modification");

        curIDText = new Label("ID");
        curPWText = new Label("Current Password");
        newPWText = new Label("New Password");
        newPWCheckText = new Label("New PW Check");
        nameText = new Label("Name");
        birthText = new Label("Birthday");
        emailText = new Label("E-mail");
        telText = new Label("Tel.");

        newPWInput = new TextField("", 10);
        curIDInput = new TextField(Login.GetUserID(), 10);
        curPWInput = new TextField("", 10);
        newPWCheckInput = new TextField("", 10);
        nameInput = new TextField("", 10);
        birthInput = new TextField("", 10);
        emailInput = new TextField("", 10);
        telInput = new TextField("", 10);

        curIDInput.setEditable(false);

        PWConditionText = new Label("Within 5 to 15 characters. Mix English and numbers.");
        birthConditionText = new Label("Write it in the form of yyyy-MM-dd.");
        telConditionText = new Label("Enter in the form of 000-000-0000 or 000-0000-0000.");

        Button = new Button("Modify");
        backButton = new Button("Back");

        //curPWInput.setEchoChar('*');
        //newPWInput.setEchoChar('*');
        //newPWCheckInput.setEchoChar('*');

        titleLabel.setBounds(panel.getWidth() / 2 - titleWidth / 2, 40, titleWidth,titleHeight);
        titleLabel.setAlignment(Label.CENTER);
        Font titleFont = new Font("Consolas", Font.PLAIN, 20);
        titleLabel.setFont(titleFont);

        nameInput.setBounds(panel.getWidth() / 2 - inputFieldWidth / 2, panel.getHeight() / 2 + 2 * interval, inputFieldWidth, inputFieldHeight);
        newPWCheckInput.setBounds(nameInput.getX(), nameInput.getY() - inputFieldHeight - 2 * interval, inputFieldWidth, inputFieldHeight);
        newPWInput.setBounds(nameInput.getX(), newPWCheckInput.getY() - inputFieldHeight - 2 * interval, inputFieldWidth, inputFieldHeight);
        curPWInput.setBounds(nameInput.getX(), newPWInput.getY() - inputFieldHeight - 2 * interval, inputFieldWidth, inputFieldHeight);
        curIDInput.setBounds(nameInput.getX(), curPWInput.getY() - inputFieldHeight - 2 * interval, inputFieldWidth, inputFieldHeight);
        birthInput.setBounds(nameInput.getX(), nameInput.getY() + 2 * interval + inputFieldHeight, inputFieldWidth, inputFieldHeight);
        emailInput.setBounds(nameInput.getX(), birthInput.getY() + 2 * interval + inputFieldHeight, inputFieldWidth, inputFieldHeight);
        telInput.setBounds(nameInput.getX(), emailInput.getY() + 2 * interval + inputFieldHeight, inputFieldWidth, inputFieldHeight);

        curIDText.setBounds(curIDInput.getX() - inputFieldWidth - interval, curIDInput.getY(), labelWidth, labelHeight);
        curPWText.setBounds(curPWInput.getX() - inputFieldWidth - interval, curPWInput.getY(), labelWidth, labelHeight);
        newPWText.setBounds(newPWInput.getX() - inputFieldWidth - interval, newPWInput.getY(), labelWidth, labelHeight);
        newPWCheckText.setBounds(newPWCheckInput.getX() - inputFieldWidth - interval, newPWCheckInput.getY(), labelWidth, labelHeight);
        nameText.setBounds(nameInput.getX() - inputFieldWidth - interval, nameInput.getY(), labelWidth, labelHeight);
        emailText.setBounds(emailInput.getX() - inputFieldWidth - interval, emailInput.getY(), labelWidth, labelHeight);
        birthText.setBounds(birthInput.getX() - inputFieldWidth - interval, birthInput.getY(), labelWidth, labelHeight);
        telText.setBounds(telInput.getX() - inputFieldWidth - interval, telInput.getY(), labelWidth, labelHeight);

        PWConditionText.setBounds(newPWInput.getX() - 2 * interval, newPWInput.getY()+ inputFieldHeight, labelWidth * 3, labelHeight);
        birthConditionText.setBounds(newPWInput.getX(), birthInput.getY()+ inputFieldHeight, labelWidth * 3, labelHeight);
        telConditionText.setBounds(newPWInput.getX() - 2 * interval, telInput.getY()+ inputFieldHeight, labelWidth * 3, labelHeight);

        curIDText.setAlignment(Label.RIGHT);
        curPWText.setAlignment(Label.RIGHT);
        newPWText.setAlignment(Label.RIGHT);
        newPWCheckText.setAlignment(Label.RIGHT);
        nameText.setAlignment(Label.RIGHT);
        emailText.setAlignment(Label.RIGHT);
        birthText.setAlignment(Label.RIGHT);
        telText.setAlignment(Label.RIGHT);

        GetUserInfo();

        Button.setBounds(curIDInput.getLocation().x + curIDInput.getWidth() + 3 * interval, panel.getHeight() / 2 - buttonHeight / 2, buttonWidth, buttonHeight);
        backButton.setBounds(panel.getWidth() - buttonWidth - 15, 0, buttonWidth, buttonHeight);

        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { Modify(); }
        });

        backButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BackTo();
                    }
                }
        );

        panel.add(titleLabel);
        panel.add(curIDInput);
        panel.add(curIDText);
        panel.add(curPWText);
        panel.add(newPWText);
        panel.add(curPWInput);
        panel.add(newPWInput);
        panel.add(newPWCheckInput);
        panel.add(newPWCheckText);
        panel.add(nameInput);
        panel.add(nameText);
        panel.add(birthInput);
        panel.add(birthText);
        panel.add(emailInput);
        panel.add(emailText);
        panel.add(telInput);
        panel.add(telText);
        panel.add(Button);
        panel.add(backButton);
        panel.add(PWConditionText);
        panel.add(birthConditionText);
        panel.add(telConditionText);
    }

    private void GetUserInfo()
    {
        if(!MainClient.withoutServerTest) {
            Message getUserInfoMessage = new Message(Task.get_user_info);
            getUserInfoMessage.AddContent(ContentType.user_id, Login.GetUserID());
            Message rcvMessage = clientSocket.SendToServer(getUserInfoMessage);

            if (rcvMessage.result) {
                String name = rcvMessage.GetContent(0);
                String birth = rcvMessage.GetContent(1);
                String email = rcvMessage.GetContent(2);
                String tel = rcvMessage.GetContent(3);

                nameInput.setText(name);
                birthInput.setText(birth);
                emailInput.setText(email);
                telInput.setText(tel);
            }
        }
    }

    private void Modify() {
        String curPW, newPW, pwCheck, name, birth, email, tel;

        curPW = curPWInput.getText();
        newPW = newPWInput.getText();
        pwCheck = newPWCheckInput.getText();
        name = nameInput.getText();
        birth = birthInput.getText();
        email = emailInput.getText();
        tel = telInput.getText();

        if(ValidTest.pwCheck(newPW, pwCheck) && ValidTest.nameCheck(name) && ValidTest.birthdayCheck(birth) && ValidTest.emailCheck(email) && ValidTest.phoneCheck(tel))
        {
            if(!MainClient.withoutServerTest) {
                Message roomLoginMessage = new Message(Task.set_user_info);
                roomLoginMessage.AddContent(ContentType.user_id, Login.GetUserID());
                roomLoginMessage.AddContent(ContentType.user_pw, curPW);
                roomLoginMessage.AddContent(ContentType.new_user_pw, newPW);
                roomLoginMessage.AddContent(ContentType.user_name, name);
                roomLoginMessage.AddContent(ContentType.user_birth, birth);
                roomLoginMessage.AddContent(ContentType.user_email, email);
                roomLoginMessage.AddContent(ContentType.user_tel, tel);
                Message rcvMessage = clientSocket.SendToServer(roomLoginMessage);

                if (rcvMessage.result) {
                    JOptionPane.showMessageDialog(null, rcvMessage.GetContent(0), "Success Message", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    private void BackTo() {
        MainClient.todoList.SetActive(true);
        SetActive(false);
    }

    public void SetActive(boolean active) {
        if(active) {
            mainPanel.add(modificationPanel);
        }
        else {
            mainPanel.remove(modificationPanel);
        }
    }
}
