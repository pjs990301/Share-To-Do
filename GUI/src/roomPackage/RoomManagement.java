package roomPackage;

import loginPackage.Login;
import mainPackage.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RoomManagement {
    private Panel mainPanel;
    private ClientSocket clientSocket;

    private enum panelType {Management, RoomName, Header, LeftBody, RightBody};
    private Panel[] managementPanels = new Panel[5];
    private JPanel memberInfoPanel;
    private Panel roomInfoPanel;
    private JScrollPane memberInfoScroll;

    private Label roomNameLabel;
    private Label titleLabel;
    private Label roomNewNameLabel;
    private Label roomNewPWLabel;
    private Label roomIDLabel;
    private Label roomCurrentPWLabel;
    private Label roomPWCheckLabel;

    private TextField roomNameInput;
    private TextField roomIDInput;
    private TextField roomCurrentPWInput;
    private TextField roomNewPWInput;
    private TextField roomPWCheckInput;

    private Button backButton;
    private Button deleteButton;
    private Button roomInfoButton;
    private Button memberInfoButton;
    private Button changeRoomInfoButton;

    private int leftWidth = 400;
    private int headerHeight = 60;
    private int interval = 10;

    private int labelWidth = 250;
    private int labelHeight = 40;

    private int buttonWidth = 100;
    private int buttonHeight = 40;

    private int inputFieldWidth = 180;
    private int inputFieldHeight = 25;
    private int inputInterval = 20;

    Timer timer = new Timer(true);
    TimerTask task;

    private ArrayList<MemberInfo> memberList;

    public RoomManagement(Frame frame, Panel panel, ClientSocket clientSocket)
    {
        this.clientSocket = clientSocket;
        mainPanel = panel;

        for(int i = 0; i < managementPanels.length; i++)
        {
            managementPanels[i] = new Panel();
            managementPanels[i].setLayout(null);
        }

        managementPanels[panelType.Management.ordinal()].setBounds(0, 0, panel.getWidth(), panel.getHeight());
        managementPanels[panelType.Management.ordinal()].add(managementPanels[panelType.RoomName.ordinal()]);
        managementPanels[panelType.Management.ordinal()].add(managementPanels[panelType.Header.ordinal()]);
        managementPanels[panelType.Management.ordinal()].add(managementPanels[panelType.LeftBody.ordinal()]);
        managementPanels[panelType.Management.ordinal()].add(managementPanels[panelType.RightBody.ordinal()]);

        /*TEST CODE*/
        managementPanels[panelType.RoomName.ordinal()].setBackground(new Color(193, 193, 193));
        managementPanels[panelType.Header.ordinal()].setBackground(new Color(193, 193, 193));
        managementPanels[panelType.LeftBody.ordinal()].setBackground(new Color(255, 255, 255));
        managementPanels[panelType.RightBody.ordinal()].setBackground(new Color(217, 217, 217));

        managementPanels[panelType.RoomName.ordinal()].setBounds(0, 0, leftWidth, headerHeight);
        managementPanels[panelType.Header.ordinal()].setBounds(leftWidth, 0, frame.getWidth() - leftWidth, headerHeight);
        managementPanels[panelType.LeftBody.ordinal()].setBounds(0, headerHeight, leftWidth, frame.getHeight() - headerHeight);
        managementPanels[panelType.RightBody.ordinal()].setBounds(leftWidth, headerHeight, frame.getWidth() - leftWidth, frame.getHeight() - headerHeight);

        DrawTitle("Room Name");
        DrawHeader();
        DrawLeftBody();
    }

    private void DrawTitle(String roomName)
    {
        roomNameLabel = new Label(roomName);
        roomNameLabel.setBounds(interval, interval, labelWidth, labelHeight);
        Font nameFont = new Font("Consolas", Font.PLAIN, 25);
        roomNameLabel.setFont(nameFont);
        GetRoomInfo();
        managementPanels[panelType.RoomName.ordinal()].add(roomNameLabel);
    }

    private void DrawHeader()
    {
        backButton = new Button("Back");
        backButton.setBounds(managementPanels[panelType.Header.ordinal()].getWidth() - buttonWidth - 2 * interval, interval, buttonWidth, buttonHeight);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BackToList();
            }
        });

        titleLabel = new Label("Room Management");
        Font titleFont = new Font("Consolas", Font.PLAIN, 25);
        titleLabel.setFont(titleFont);
        titleLabel.setBounds(0, interval, labelWidth, labelHeight);

        managementPanels[panelType.Header.ordinal()].add(titleLabel);
        managementPanels[panelType.Header.ordinal()].add(backButton);
    }

    private void DrawLeftBody()
    {
        int panelWidth = managementPanels[panelType.LeftBody.ordinal()].getWidth();

        Font btnFont = new Font("Consolas", Font.PLAIN, 16);

        memberInfoButton = new Button("Member Information");
        memberInfoButton.setBounds(3 * interval, interval, panelWidth - 6 * interval, buttonHeight);
        memberInfoButton.setFont(btnFont);
        memberInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MemberRoomChange(true);
            }
        });

        roomInfoButton = new Button("Room Information");
        roomInfoButton.setBounds(3 * interval, 2 * interval + buttonHeight, panelWidth - 6 * interval, buttonHeight);
        roomInfoButton.setFont(btnFont);
        roomInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MemberRoomChange(false);
            }
        });

        deleteButton = new Button("Delete Room");
        deleteButton.setBounds(interval, managementPanels[panelType.LeftBody.ordinal()].getHeight() - buttonHeight - 4 * interval, buttonWidth, buttonHeight);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteRoom();
            }
        });

        managementPanels[panelType.LeftBody.ordinal()].add(memberInfoButton);
        managementPanels[panelType.LeftBody.ordinal()].add(roomInfoButton);
        managementPanels[panelType.LeftBody.ordinal()].add(deleteButton);
    }

    private void DeleteRoom()
    {
        int result = JOptionPane.showConfirmDialog(null, "Do you want to delete this room?", "Delete Room?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(result == 0) // YES
        {
            if(!MainClient.withoutServerTest) {
                Message deleteRoomMessage = new Message(Task.delete_room);
                deleteRoomMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
                Message rcvMessage = clientSocket.SendToServer(deleteRoomMessage);

                if (rcvMessage.result) {
                    SetActive(false);
                    MainClient.roomLogin.SetActive(true);
                }
            }
            else
            {
                SetActive(false);
                MainClient.roomLogin.SetActive(true);
            }
        }
    }

    private void DrawMemberInfo()
    {
        if(roomInfoPanel != null) {
            managementPanels[panelType.RightBody.ordinal()].remove(roomInfoPanel);
        }

        if(memberInfoScroll == null)
        {
            memberInfoPanel = new JPanel();
            memberInfoPanel.setLayout(null);
            memberInfoPanel.setBounds(0, 0, managementPanels[panelType.RightBody.ordinal()].getWidth() - 5, managementPanels[panelType.RightBody.ordinal()].getHeight());

            memberInfoScroll = new JScrollPane(memberInfoPanel);
            memberInfoScroll.setBounds(0, 0, memberInfoPanel.getWidth() - 5, memberInfoPanel.getHeight() - interval * 3);
            memberInfoScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            memberInfoScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            memberInfoScroll.getViewport().getView().setBackground(managementPanels[panelType.RightBody.ordinal()].getBackground());
            memberInfoScroll.getVerticalScrollBar().setUnitIncrement(16);
            memberInfoScroll.setOpaque(false);

            GetMemberInfoFromServer();


            if(memberList.size() != 0) {
                MemberInfo info = memberList.get(memberList.size() - 1);
                memberInfoPanel.setPreferredSize(new Dimension(leftWidth, info.getY() + info.getHeight() - memberList.get(0).getY()));
            }
            managementPanels[panelType.RightBody.ordinal()].add(memberInfoScroll);
        }
        else
            managementPanels[panelType.RightBody.ordinal()].add(memberInfoScroll);

        memberInfoScroll.getViewport().revalidate();
        memberInfoScroll.getViewport().repaint();
    }

    private void GetMemberInfoFromServer()
    {
        if(!MainClient.withoutServerTest) {
            Message getSubListMessage = new Message(Task.get_member_list);
            getSubListMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());

            Message rcvMessage = clientSocket.SendToServer(getSubListMessage);

            if (rcvMessage.result) {
                memberList = new ArrayList<MemberInfo>();
                for(int i = 0, j = 0; i < rcvMessage.GetContentLength() - 1; i += 2, j++) // -1은 메시지를 제외한 것
                {
                    String memberName = rcvMessage.GetContent(i);
                    String memberID = rcvMessage.GetContent(i + 1);
                    boolean isMemberAdmin = memberID.equals(Login.GetUserID()) ? true : false;

                    MemberInfo memberInfo = new MemberInfo(this, memberName, memberID, isMemberAdmin,managementPanels[panelType.RightBody.ordinal()].getWidth() - 5 * interval , headerHeight, j);
                    memberList.add(memberInfo);
                    memberInfoPanel.add(memberInfo);
                }
            }
        }
        else
        {
            memberList = new ArrayList<MemberInfo>();
            int memberLength = 3;
            for(int i = 0; i < memberLength; i++)
            {
                MemberInfo memberInfo = new MemberInfo(this,"Kim", "ID", true,managementPanels[panelType.RightBody.ordinal()].getWidth() - 5 * interval, headerHeight, i);
                memberList.add(memberInfo);
                memberInfoPanel.add(memberInfo);
            }
        }
    }

    private void DrawRoomInfo()
    {
        if(memberInfoPanel != null) {
            managementPanels[panelType.RightBody.ordinal()].remove(memberInfoScroll);
            task.cancel();
        }

        if(roomInfoPanel == null)
        {
            roomInfoPanel = new Panel();
            roomInfoPanel.setLayout(null);
            roomInfoPanel.setBounds(0, 0, managementPanels[panelType.RightBody.ordinal()].getWidth(),managementPanels[panelType.RightBody.ordinal()].getHeight());

            roomNewNameLabel = new Label("Room Name");
            roomNewNameLabel.setAlignment(Label.RIGHT);
            roomIDLabel = new Label("Room ID");
            roomIDLabel.setAlignment(Label.RIGHT);
            roomCurrentPWLabel = new Label("Current room PW");
            roomCurrentPWLabel.setAlignment(Label.RIGHT);
            roomNewPWLabel = new Label("New room PW");
            roomNewPWLabel.setAlignment(Label.RIGHT);
            roomPWCheckLabel = new Label("Room PW Check");
            roomPWCheckLabel.setAlignment(Label.RIGHT);

            roomNameInput = new TextField(roomNameLabel.getText());
            roomIDInput = new TextField(RoomLogin.GetRoomID());
            roomIDInput.setEditable(false);
            roomCurrentPWInput = new TextField("");
            roomNewPWInput = new TextField("");
            roomPWCheckInput = new TextField("");

            changeRoomInfoButton = new Button("Change Info");
            changeRoomInfoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ChangeRoomInfo();
                }
            });

            roomCurrentPWInput.setBounds(roomInfoPanel.getWidth() / 2 - inputFieldWidth / 2 - inputInterval, roomInfoPanel.getHeight() / 2 - inputFieldHeight, inputFieldWidth, inputFieldHeight);
            roomIDInput.setBounds(roomCurrentPWInput.getX(), roomCurrentPWInput.getY() - inputFieldHeight - inputInterval, inputFieldWidth, inputFieldHeight);
            roomNameInput.setBounds(roomIDInput.getX(), roomIDInput.getY() - inputFieldHeight - inputInterval, inputFieldWidth, inputFieldHeight);
            roomNewPWInput.setBounds(roomIDInput.getX(), roomCurrentPWInput.getY() + inputFieldHeight + inputInterval, inputFieldWidth, inputFieldHeight);
            roomPWCheckInput.setBounds(roomIDInput.getX(), roomNewPWInput.getY() + inputFieldHeight + inputInterval, inputFieldWidth, inputFieldHeight);

            roomCurrentPWInput.setEchoChar('*');
            roomNewPWInput.setEchoChar('*');
            roomPWCheckInput.setEchoChar('*');

            roomIDLabel.setBounds(roomIDInput.getX() - labelWidth - inputInterval, roomIDInput.getY(), labelWidth, inputFieldHeight);
            roomNewNameLabel.setBounds(roomNameInput.getX() - labelWidth - inputInterval, roomNameInput.getY(), labelWidth, inputFieldHeight);
            roomCurrentPWLabel.setBounds(roomCurrentPWInput.getX() - labelWidth - inputInterval, roomCurrentPWInput.getY(), labelWidth, inputFieldHeight);
            roomNewPWLabel.setBounds(roomNewPWInput.getX() - labelWidth - inputInterval, roomNewPWInput.getY(), labelWidth, inputFieldHeight);
            roomPWCheckLabel.setBounds(roomPWCheckInput.getX() - labelWidth - inputInterval, roomPWCheckInput.getY(), labelWidth, inputFieldHeight);

            changeRoomInfoButton.setBounds(roomCurrentPWInput.getX() + inputFieldWidth + 2 * interval, roomCurrentPWInput.getY() - (buttonHeight -  roomCurrentPWInput.getHeight()) / 2, buttonWidth, buttonHeight);

            roomInfoPanel.add(roomIDInput);
            roomInfoPanel.add(roomNameInput);
            roomInfoPanel.add(roomCurrentPWInput);
            roomInfoPanel.add(roomNewPWInput);
            roomInfoPanel.add(roomPWCheckInput);
            roomInfoPanel.add(roomIDLabel);
            roomInfoPanel.add(roomNewNameLabel);
            roomInfoPanel.add(roomCurrentPWLabel);
            roomInfoPanel.add(roomNewPWLabel);
            roomInfoPanel.add(roomPWCheckLabel);
            roomInfoPanel.add(changeRoomInfoButton);

            managementPanels[panelType.RightBody.ordinal()].add(roomInfoPanel);
        }
        else
            managementPanels[panelType.RightBody.ordinal()].add(roomInfoPanel);
    }

    private void GetRoomInfo()
    {
        if(!MainClient.withoutServerTest) {
            Message getRoomInfoMessage = new Message(Task.get_room_info);
            getRoomInfoMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
            Message rcvMessage = clientSocket.SendToServer(getRoomInfoMessage);

            if (rcvMessage.result) {
                String roomName = rcvMessage.GetContent(0);
                roomNameLabel.setText(roomName);
            }
        }
    }

    private void ChangeRoomInfo()
    {
        String currentPW, newPW, pwCheck, newName;
        currentPW = roomCurrentPWInput.getText();
        newPW = roomNewPWInput.getText();
        pwCheck = roomPWCheckInput.getText();
        newName = roomNameInput.getText();

        if(newName.equals(""))
            newName = roomNameLabel.getText();
        if(newPW.equals(""))
            newPW = currentPW;

        if(ValidTest.pwCheck(newPW, pwCheck) || newPW.equals(currentPW))
        {
            if(!MainClient.withoutServerTest) {
                Message setRoomInfoMessage = new Message(Task.set_room_info);
                setRoomInfoMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
                setRoomInfoMessage.AddContent(ContentType.room_pw, currentPW);
                setRoomInfoMessage.AddContent(ContentType.new_room_pw, newPW);
                setRoomInfoMessage.AddContent(ContentType.new_room_name, newName);
                Message rcvMessage = clientSocket.SendToServer(setRoomInfoMessage);

                if (rcvMessage.result) {
                    JOptionPane.showMessageDialog(null, rcvMessage.GetContent(0), "Success Message", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    private void BackToList()
    {
        SetActive(false);
        MainClient.todoList.SetActive(true);
    }
    private void SynchronizeDisplay()
    {
        if(memberInfoScroll != null) {
            managementPanels[panelType.RightBody.ordinal()].remove(memberInfoScroll);
            DrawMemberInfo();
        }
    }
    public void SetActive(boolean onOff)
    {
        if(onOff) {
            mainPanel.add(managementPanels[panelType.Management.ordinal()]);
        }
        else {
            mainPanel.remove(managementPanels[panelType.Management.ordinal()]);
            task.cancel();
        }
    }

    public void MemberOut(String userID, MemberInfo memberInfo)
    {
        if(!MainClient.withoutServerTest) {
            Message setRoomInfoMessage = new Message(Task.member_out);
            setRoomInfoMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
            setRoomInfoMessage.AddContent(ContentType.user_id, userID);
            Message rcvMessage = clientSocket.SendToServer(setRoomInfoMessage);

            if (rcvMessage.result) {
                JOptionPane.showMessageDialog(null, rcvMessage.GetContent(0), "Success Message", JOptionPane.PLAIN_MESSAGE);
                memberInfoPanel.remove(memberInfo);
                memberList.remove(memberInfo);
                ReArrangeMemberList();
            }
        }
        else
        {
            memberInfoPanel.remove(memberInfo);
            memberList.remove(memberInfo);
            ReArrangeMemberList();
        }
    }

    private void MemberRoomChange(boolean isMemberInfo)
    {
        if(isMemberInfo)
        {
            task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println(LocalTime.now() + " / Synchronize");
                    SynchronizeDisplay();
                }
            };
            timer.schedule(task, MainClient.synchronizeDelay * 1000, MainClient.synchronizeDelay * 1000);
            DrawMemberInfo();
        }
        else
        {
            DrawRoomInfo();
        }
    }

    private void ReArrangeMemberList()
    {
        for(int i = 0; i < memberList.size(); i++)
            memberList.get(i).setBounds(interval, interval * (i + 1) + headerHeight * i, managementPanels[panelType.RightBody.ordinal()].getWidth() - 5 * interval, headerHeight);
        memberInfoScroll.getViewport().revalidate();
        memberInfoScroll.getViewport().repaint();
    }

    public String GetNameByID(String id)
    {
        for(int i = 0; i < memberList.size(); i++)
        {
            if(memberList.get(i).GetUserID().equals(id))
                return memberList.get(i).GetUserName();
        }
        return null;
    }
}
