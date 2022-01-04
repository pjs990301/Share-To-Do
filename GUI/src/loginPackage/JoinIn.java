package loginPackage;

import mainPackage.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinIn {
    private ClientSocket clientSocket;

    public Panel signInPanel;
    private Panel mainPanel;

    private Label titleLabel;
    private Label IDText;
    private Label PWText;
    private Label PWCheckText;
    private Label nameText;
    private Label birthText;
    private Label emailText;
    private Label telText;

    private Label IDConditionText;
    private Label PWConditionText;
    private Label birthConditionText;
    private Label telConditionText;

    private TextField IDInput;
    private TextField PWInput;
    private TextField PWCheckInput;
    private TextField nameInput;
    private TextField birthInput;
    private TextField emailInput;
    private TextField telInput;

    private Button Button;
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

    public JoinIn(Frame frame, Panel panel, ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
        frame.setSize(1280, 720);

        mainPanel = panel;
        signInPanel = new Panel();

        signInPanel.setLayout(null);
        signInPanel.setSize(frame.getWidth(), frame.getHeight());

        DrawJoinIn(signInPanel);
    }

    private void DrawJoinIn(Panel panel) {
        titleLabel = new Label("Join In");
        IDText = new Label("ID");
        PWText = new Label("PW");
        PWCheckText = new Label("PW Check");
        nameText = new Label("Name");
        birthText = new Label("Birthday");
        emailText = new Label("E-mail");
        telText = new Label("Tel.");

        IDConditionText = new Label("Within 5 to 15 characters. Mix English and numbers.");
        PWConditionText = new Label("Within 5 to 15 characters. Mix English and numbers.");
        birthConditionText = new Label("Write it in the form of yyyy-MM-dd.");
        telConditionText = new Label("Enter in the form of 000-000-0000 or 000-0000-0000.");

        IDInput = new TextField("", 10);
        PWInput = new TextField("", 10);
        PWCheckInput = new TextField("", 10);
        nameInput = new TextField("", 10);
        birthInput = new TextField("", 10);
        emailInput = new TextField("", 10);
        telInput = new TextField("", 10);

        Button = new Button("Join In");
        backButton = new Button("Back to Login");

        PWInput.setEchoChar('*');
        PWCheckInput.setEchoChar('*');

        titleLabel.setBounds(panel.getWidth() / 2 - titleWidth / 2, 50, titleWidth,titleHeight);
        titleLabel.setAlignment(Label.CENTER);
        Font titleFont = new Font("Consolas", Font.PLAIN, 20);
        titleLabel.setFont(titleFont);

        nameInput.setBounds(panel.getWidth() / 2 - inputFieldWidth / 2, panel.getHeight() / 2, inputFieldWidth, inputFieldHeight);
        PWCheckInput.setBounds(nameInput.getX(), nameInput.getY() - inputFieldHeight - 2 * interval, inputFieldWidth, inputFieldHeight);
        PWInput.setBounds(nameInput.getX(), PWCheckInput.getY() - inputFieldHeight - 2 * interval, inputFieldWidth, inputFieldHeight);
        IDInput.setBounds(nameInput.getX(), PWInput.getY() - inputFieldHeight - 2 * interval, inputFieldWidth, inputFieldHeight);
        birthInput.setBounds(nameInput.getX(), nameInput.getY() + 2 * interval + inputFieldHeight, inputFieldWidth, inputFieldHeight);
        emailInput.setBounds(nameInput.getX(), birthInput.getY() + 2 * interval + inputFieldHeight, inputFieldWidth, inputFieldHeight);
        telInput.setBounds(nameInput.getX(), emailInput.getY() + 2 * interval + inputFieldHeight, inputFieldWidth, inputFieldHeight);

        IDText.setBounds(IDInput.getX() - labelWidth - interval, IDInput.getY(), labelWidth, labelHeight);
        PWText.setBounds(PWInput.getX() - labelWidth - interval, PWInput.getY(), labelWidth, labelHeight);
        PWCheckText.setBounds(PWCheckInput.getX() - labelWidth - interval, PWCheckInput.getY(), labelWidth, labelHeight);
        nameText.setBounds(nameInput.getX() - labelWidth - interval, nameInput.getY(), labelWidth, labelHeight);
        emailText.setBounds(emailInput.getX() - labelWidth - interval, emailInput.getY(), labelWidth, labelHeight);
        birthText.setBounds(birthInput.getX() - labelWidth - interval, birthInput.getY(), labelWidth, labelHeight);
        telText.setBounds(telInput.getX() - labelWidth - interval, telInput.getY(), labelWidth, labelHeight);

        IDConditionText.setBounds(IDInput.getX() - 2 * interval, IDInput.getY() + inputFieldHeight, labelWidth * 3, labelHeight);
        PWConditionText.setBounds(IDInput.getX() - 2 * interval, PWInput.getY()+ inputFieldHeight, labelWidth * 3, labelHeight);
        birthConditionText.setBounds(IDInput.getX(), birthInput.getY()+ inputFieldHeight, labelWidth * 3, labelHeight);
        telConditionText.setBounds(IDInput.getX() - 2 * interval, telInput.getY()+ inputFieldHeight, labelWidth * 3, labelHeight);

        IDText.setAlignment(Label.RIGHT);
        PWText.setAlignment(Label.RIGHT);
        PWCheckText.setAlignment(Label.RIGHT);
        nameText.setAlignment(Label.RIGHT);
        emailText.setAlignment(Label.RIGHT);
        birthText.setAlignment(Label.RIGHT);
        telText.setAlignment(Label.RIGHT);

        Button.setBounds(IDInput.getLocation().x + IDInput.getWidth() + 3 * interval, panel.getHeight() / 2 - buttonHeight / 2, buttonWidth, buttonHeight);
        backButton.setBounds(panel.getWidth() - buttonWidth - interval, interval, buttonWidth, buttonHeight);

        Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { Register(); }
        });

        backButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        BackToLogin();
                    }
                }
        );

        panel.add(titleLabel);
        panel.add(IDInput);
        panel.add(IDText);
        panel.add(PWText);
        panel.add(PWInput);
        panel.add(PWCheckInput);
        panel.add(PWCheckText);
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
        panel.add(IDConditionText);
        panel.add(PWConditionText);
        panel.add(birthConditionText);
        panel.add(telConditionText);
    }

    private void Register() {
        String id, pw, pwCheck, name, birth, email, tel;
        id = IDInput.getText();
        pw = PWInput.getText();
        pwCheck = PWCheckInput.getText();
        name = nameInput.getText();
        birth = birthInput.getText();
        email = emailInput.getText();
        tel = telInput.getText();

        if(ValidTest.CheckAllConditions(id, pw, pwCheck, name, birth, email, tel))
        {
            if(!MainClient.withoutServerTest)
            {
                Message joinInMessage = new Message(Task.join_in);
                joinInMessage.AddContent(ContentType.user_id, id);
                joinInMessage.AddContent(ContentType.user_pw, pw);
                joinInMessage.AddContent(ContentType.user_name, name);
                joinInMessage.AddContent(ContentType.user_birth, birth);
                joinInMessage.AddContent(ContentType.user_email, email);
                joinInMessage.AddContent(ContentType.user_tel, tel);

                Message rcvMessage = clientSocket.SendToServer(joinInMessage);
                if(rcvMessage.result){
                    MainClient.login.SetActive(true);
                    SetActive(false);
                    JOptionPane.showMessageDialog(null, rcvMessage.GetContent(0), "Success Message", JOptionPane.PLAIN_MESSAGE);
                }
            }
            else
            {
                MainClient.login.SetActive(true);
                SetActive(false);
            }
        }

    }

    private void BackToLogin() {
        MainClient.login.SetActive(true);
        SetActive(false);
    }

    public void SetActive(boolean active) {
        if(active) {
            mainPanel.add(signInPanel);
        }
        else {
            mainPanel.remove(signInPanel);
        }
    }
}
