package roomPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemberInfo extends JPanel {
    private String userName;
    private String userID;
    private Color backgroundColor = new Color(221, 250, 246);
    private JLabel memberNameLabel;
    private JButton outButton;

    private int buttonWidth = 80;
    private int buttonHeight = 40;
    private int interval = 10;

    private RoomManagement roomManagement;

    public MemberInfo(RoomManagement roomMng, String name, String id, boolean isAdmin, int width, int height, int index)
    {
        roomManagement = roomMng;
        userName = name;
        userID = id;

        setLayout(null);
        setBackground(backgroundColor);
        add(memberNameLabel = new JLabel(name));
        outButton = new JButton("OUT");
        setBounds(interval, interval * (index + 1) + height * index, width, height);

        Font nameFont = new Font("Consolas", Font.PLAIN, 15);
        memberNameLabel.setFont(nameFont);
        memberNameLabel.setBounds(interval, interval, getWidth() - buttonWidth - 2 * interval, buttonHeight);
        outButton.setBounds(getWidth() - buttonWidth - interval, interval, buttonWidth, buttonHeight);
        outButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MemberOut();
            }
        });
        if(!isAdmin)
            add(outButton);
    }

    public String GetUserName()
    {
        return userName;
    }
    public String GetUserID()
    {
        return userID;
    }

    private void MemberOut()
    {
        int result = JOptionPane.showConfirmDialog(null, "Do you want to kick " + roomManagement.GetNameByID(userID) + "?", "Kick?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if(result == 0)
            roomManagement.MemberOut(userID, this);
    }
}
