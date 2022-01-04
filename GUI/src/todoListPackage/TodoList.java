package todoListPackage;

import loginPackage.Login;
import mainPackage.*;
import roomPackage.RoomLogin;
import roomPackage.RoomManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TodoList {
    private Frame mainFrame;
    private Panel mainPanel;

    private JPanel mainTopicScrollPanel;
    private JPanel subTopicScrollPanel;
    private JScrollPane mainScroll;
    private JScrollPane subScroll;
    private ClientSocket clientSocket;
    private enum panelType {Main, Header, MainTopic, SubTopic}
    private Panel[] managementPanels = new Panel[4];

    private Label roomNameLabel;

    private Button roomManagementButton;
    private Button modifyInfoButton;
    private Button leaveRoomButton;
    private Button roomLogout;
    private Button addMainTopicButton;
    private Button addSubTopicButton;

    private JPanel mainTopicNamePanel;
    private JTextField mainTopicNameModify;
    private JComboBox managerComboBox;

    private Frame addTopicFrame;
    private Panel addTopicPanel;
    private Label addTopicTitleLabel;
    private Label addTopicNameLabel;
    private Label addTopicManagerLabel;
    private Label addTopicDeadlineLabel;
    private TextField addTopicNameInput;
    private JComboBox aTManagerComboBox;
    private TextField aTDeadlineInput;
    private Label aTDeadlineFormatLabel;
    private Button aTButton;
    private int addTopicWidth = 400;
    private int addTopicHeight = 450;
    private int addTopicLabelWidth = 100;
    private int addTopicLabelHeight = 30;

    private int leftWidth = 600;
    private int headerHeight = 60;
    private int interval = 10;
    private int buttonWidth = 140;
    private int buttonHeight = 45;
    private int comboBoxWidth = 150;

    private ArrayList<MainTopic> mainTopics;
    private ArrayList<SubTopic> subTopics;
    private int mainTopicLength = 0;
    private int currentTopicID;

    Timer timer = new Timer(true);
    TimerTask task;

    private boolean isAdmin = false;
    private ArrayList<Member> memberList;
    private String[] memberNames;

    public TodoList(Frame frame, Panel panel, ClientSocket clientSocket)
    {
        mainFrame = frame;
        mainPanel = panel;
        this.clientSocket = clientSocket;

        for(int i = 0; i < managementPanels.length; i++)
        {
            managementPanels[i] = new Panel();
            managementPanels[i].setLayout(null);
        }
        GetPanel(panelType.Main).setBounds(0, 0, panel.getWidth(), panel.getHeight());

        GetPanel(panelType.Header).setBackground(new Color(222, 222, 222));
        GetPanel(panelType.MainTopic).setBackground(new Color(255, 255, 255));
        GetPanel(panelType.SubTopic).setBackground(new Color(190, 190, 190));

        GetPanel(panelType.Header).setBounds(0, 0, frame.getWidth(), headerHeight);
        GetPanel(panelType.MainTopic).setBounds(0, headerHeight, leftWidth, frame.getHeight() - headerHeight);
        GetPanel(panelType.SubTopic).setBounds(leftWidth, headerHeight, frame.getWidth() - leftWidth, frame.getHeight() - headerHeight);

        GetPanel(panelType.Main).add(GetPanel(panelType.Header));
        GetPanel(panelType.Main).add(GetPanel(panelType.MainTopic));
        GetPanel(panelType.Main).add(GetPanel(panelType.SubTopic));

        DrawHeader();
        DrawMainTopics();
    }

    private Panel GetPanel(panelType type)
    {
        return managementPanels[type.ordinal()];
    }

    private void DrawHeader()
    {
        roomNameLabel = new Label("RoomName"); // TODO: Get From Server
        roomNameLabel.setBounds(interval, 0, leftWidth, headerHeight);
        roomNameLabel.setFont(new Font("Consolas", Font.PLAIN, 28));
        GetPanel(panelType.Header).add(roomNameLabel);

        roomManagementButton = new Button("Room Management");
        modifyInfoButton = new Button("Modify User Info");
        leaveRoomButton = new Button("Leave Room");
        roomLogout = new Button("Room Log Out");

        int x = GetHeaderButtonX(GetPanel(panelType.Header).getWidth()) - 2 * interval;
        int y = (GetPanel(panelType.Header).getHeight() - buttonHeight) / 2;
        roomLogout.setBounds(x, y, buttonWidth, buttonHeight);
        leaveRoomButton.setBounds(x = GetHeaderButtonX(x), y, buttonWidth, buttonHeight);
        modifyInfoButton.setBounds(x = GetHeaderButtonX(x), y, buttonWidth, buttonHeight);
        roomManagementButton.setBounds(GetHeaderButtonX(x), y, buttonWidth, buttonHeight);

        roomLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetActive(false);
                MainClient.roomLogin.SetActive(true);
            }
        });

        roomManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenRoomManagement();
            }
        });

        modifyInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenModifyInfo();
            }
        });

        leaveRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LeaveRoom();
            }
        });

        GetPanel(panelType.Header).add(roomLogout);
        GetPanel(panelType.Header).add(leaveRoomButton);
        GetPanel(panelType.Header).add(modifyInfoButton);
    }

    private int GetHeaderButtonX(int x)
    {
        return x - interval - buttonWidth;
    }

    private void DrawMainTopics()
    {
        mainTopicScrollPanel = new JPanel();
        mainTopicScrollPanel.setBounds(0, 0, leftWidth, GetPanel(panelType.MainTopic).getHeight() - buttonHeight - interval * 3);
        mainTopicScrollPanel.setLayout(null);
        mainTopicScrollPanel.setBackground(Color.white);

        mainScroll = new JScrollPane(mainTopicScrollPanel);
        mainScroll.setBounds(0, 0, leftWidth, GetPanel(panelType.MainTopic).getHeight() - buttonHeight - interval * 3);
        mainScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScroll.getViewport().getView().setBackground(Color.white);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.setOpaque(false);

        addMainTopicButton = new Button("Add Main Topic");
        addMainTopicButton.setBounds(leftWidth - buttonWidth, GetPanel(panelType.MainTopic).getHeight() - buttonHeight - 3 * interval, buttonWidth, buttonHeight);
        GetPanel(panelType.MainTopic).add(addMainTopicButton);
        addMainTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMainTopic();
            }
        });

        GetTodoListFromServer();

        if(mainTopicLength != 0)
            mainTopicScrollPanel.setPreferredSize(new Dimension(leftWidth, (mainTopics.get(mainTopicLength - 1).getY() +  headerHeight - (mainTopics.get(0).getY() - 2 * interval))));
        GetPanel(panelType.MainTopic).add(mainScroll);
    }
    private void GetTodoListFromServer()
    {
        String roomName = "RoomName";
        memberNames = GetMemberListFromServer();
        mainTopics = new ArrayList<>();

        if(!MainClient.withoutServerTest) {
            Message topicsMessage = new Message(Task.get_todo_list);
            topicsMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
            topicsMessage.AddContent(ContentType.user_id, Login.GetUserID());
            Message rcvMessage = clientSocket.SendToServer(topicsMessage);

            if (rcvMessage.result) {
                roomName = rcvMessage.GetContent(0);
                isAdmin = Boolean.parseBoolean(rcvMessage.GetContent(1));
                for(int i = 2; i < rcvMessage.GetContentLength() - 1; i += 5) // -1은 메시지를 제외한 것
                {
                    int topicID = Integer.parseInt(rcvMessage.GetContent(i));
                    String topicName = rcvMessage.GetContent(i + 1);
                    boolean topicCompleted = Integer.parseInt(rcvMessage.GetContent(i + 2)) == 1 ? true : false;
                    String topicDeadline = rcvMessage.GetContent(i + 3);
                    String topicManager = rcvMessage.GetContent(i + 4);

                    MainTopic topic = new MainTopic(this,topicName, topicCompleted, topicDeadline, topicManager, GetPanel(panelType.MainTopic).getWidth() - 4 * interval, headerHeight, topicID);
                    mainTopics.add(topic);
                    mainTopicScrollPanel.add(topic);
                }

            }
        }
        else
        {
            int topicLength = 8;
            for(int i = 0; i < topicLength; i++)
            {
                MainTopic topic = new MainTopic(this, "TOPIC NAME", false,"2018-10-29", "1515", GetPanel(panelType.MainTopic).getWidth() - 4 * interval, headerHeight, i);
                mainTopicScrollPanel.add(topic);
                mainTopics.add(topic);
            }
            isAdmin = true;
        }

        roomNameLabel.setText(roomName);
        RoomMngButtonActive(isAdmin);
        mainTopicLength = mainTopics.size();
    }

    public void DrawSubTopics(int topicID)
    {
        if(subScroll != null) {
            GetPanel(panelType.SubTopic).remove(subScroll);
        }

        subTopicScrollPanel = new JPanel();
        subTopicScrollPanel.setBounds(0, 0, GetPanel(panelType.SubTopic).getWidth() - 5, GetPanel(panelType.SubTopic).getHeight() - buttonHeight - 3 * interval);
        subTopicScrollPanel.setLayout(null);
        subTopicScrollPanel.setBackground(GetPanel(panelType.SubTopic).getBackground());

        subScroll = new JScrollPane(subTopicScrollPanel);
        subScroll.setBounds(0, 0, subTopicScrollPanel.getWidth(), GetPanel(panelType.SubTopic).getHeight() - buttonHeight - 3 * interval);
        subScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        subScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        subScroll.getViewport().getView().setBackground(GetPanel(panelType.SubTopic).getBackground());
        subScroll.getVerticalScrollBar().setUnitIncrement(16);
        subScroll.setOpaque(false);

        mainTopicNamePanel = new JPanel();
        mainTopicNamePanel.setLayout(null);
        mainTopicNamePanel.setBounds(interval, interval, GetPanel(panelType.SubTopic).getWidth() - 4 * interval, headerHeight);
        mainTopicNamePanel.setBackground(Color.white);

        addSubTopicButton = new Button("Add Sub-Topic");
        addSubTopicButton.setBounds(GetPanel(panelType.SubTopic).getWidth() - buttonWidth, GetPanel(panelType.SubTopic).getHeight() - buttonHeight - 3 * interval, buttonWidth, buttonHeight);
        addSubTopicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSubTopic();
            }
        });

        // TODO: Get From Server
        mainTopicNameModify = new JTextField(mainTopics.get(topicID).GetTopicName());
        mainTopicNameModify.setBounds(interval, 0, mainTopicNamePanel.getWidth(), headerHeight);
        mainTopicNameModify.setFont(new Font("Consolas", Font.PLAIN, 25));
        mainTopicNameModify.setAlignmentY(SwingConstants.CENTER);
        mainTopicNameModify.setBorder(null);
        mainTopicNameModify.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetMainTopicName();
            }
        });

        managerComboBox = new JComboBox(memberNames);
        managerComboBox.setBounds(mainTopicNamePanel.getWidth() - comboBoxWidth - interval, interval / 2, comboBoxWidth, headerHeight - interval);
        managerComboBox.setFont(new Font("Consolas", Font.BOLD, 20));
        managerComboBox.setSelectedIndex(GetManagerIndexByID(mainTopics.get(topicID).GetManager()));
        managerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetManager();
            }
        });

        mainTopicNamePanel.add(managerComboBox);
        mainTopicNamePanel.add(mainTopicNameModify);

        GetSubListFromServer();

        if(subTopics.size() > 0)
            subTopicScrollPanel.setPreferredSize(new Dimension(leftWidth, (subTopics.get(subTopics.size() - 1).getY() +  headerHeight - (mainTopicNamePanel.getY() - 2 * interval))));

        subTopicScrollPanel.add(mainTopicNamePanel);
        GetPanel(panelType.SubTopic).add(addSubTopicButton);
        GetPanel(panelType.SubTopic).add(subScroll);
        GetPanel(panelType.SubTopic).repaint();
        subScroll.getViewport().revalidate();
        subScroll.getViewport().repaint();
    }
    private String[] GetMemberListFromServer()
    {
        String[] memberNameArr = null;
        if(!MainClient.withoutServerTest) {
            Message getSubListMessage = new Message(Task.get_member_list);
            getSubListMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());

            Message rcvMessage = clientSocket.SendToServer(getSubListMessage);

            if (rcvMessage.result) {
                memberList = new ArrayList<Member>();
                for(int i = 0; i < rcvMessage.GetContentLength() - 1; i += 2) // -1은 메시지를 제외한 것
                {
                    String memberName = rcvMessage.GetContent(i);
                    String memberID = rcvMessage.GetContent(i + 1);

                    Member member = new Member(memberName, memberID);
                    memberList.add(member);
                }

                memberNameArr = new String[memberList.size()];
                for(int i = 0; i < memberNameArr.length; i++)
                    memberNameArr[i] = memberList.get(i).GetMemberName();
            }
        }
        else
        {
            memberList = new ArrayList<Member>();
            int memberLength = 3;
            for(int i = 0; i < memberLength; i++)
            {
                Member member = new Member("Kim", "1515");
                memberList.add(member);
            }

            memberNameArr = new String[memberList.size()];
            for(int i = 0; i < memberNameArr.length; i++)
                memberNameArr[i] = memberList.get(i).GetMemberName();
        }
        return memberNameArr;
    }
    private void GetSubListFromServer()
    {
        subTopics = new ArrayList<>();
        if(!MainClient.withoutServerTest) {
            Message getSubListMessage = new Message(Task.get_sub_todo_list);
            getSubListMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
            getSubListMessage.AddContent(ContentType.topic_id, Integer.toString(currentTopicID));
            getSubListMessage.AddContent(ContentType.user_id, Login.GetUserID());

            Message rcvMessage = clientSocket.SendToServer(getSubListMessage);

            if (rcvMessage.result) {
                subTopics = new ArrayList<SubTopic>();
                for(int i = 0; i < rcvMessage.GetContentLength() - 1; i += 3) // -1은 메시지를 제외한 것
                {
                    String topicName = rcvMessage.GetContent(i);
                    boolean topicCompleted = Integer.parseInt(rcvMessage.GetContent(i + 1)) == 1 ? true : false;
                    int subID = Integer.parseInt(rcvMessage.GetContent(i + 2));

                    SubTopic topic = new SubTopic(this, topicName, topicCompleted, GetPanel(panelType.SubTopic).getWidth() - 4 * interval, headerHeight, mainTopicNamePanel.getHeight(), subID);
                    subTopics.add(topic);
                    subTopicScrollPanel.add(topic);
                }
            }
        }
        else
        {
            int subTopicLength = 8;
            subTopics = new ArrayList<SubTopic>();
            for(int i = 0; i < subTopicLength; i++)
            {
                SubTopic topic = new SubTopic(this, "SUB TOPIC NAME", false,GetPanel(panelType.SubTopic).getWidth() - 4 * interval, headerHeight, mainTopicNamePanel.getHeight(), i);
                subTopicScrollPanel.add(topic);
                subTopics.add(topic);
            }
        }
    }

    private void SetMainTopicName()
    {
        String newMainName = mainTopicNameModify.getText();
        if(!newMainName.equals(mainTopics.get(currentTopicID).GetTopicName()))
        {
            if(!MainClient.withoutServerTest) {
                Message setTopicNameMessage = new Message(Task.set_topic_name);
                setTopicNameMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
                setTopicNameMessage.AddContent(ContentType.topic_id, Integer.toString(currentTopicID));
                setTopicNameMessage.AddContent(ContentType.new_topic_name, newMainName);
                setTopicNameMessage.AddContent(ContentType.user_id, Login.GetUserID());
                Message rcvMessage = clientSocket.SendToServer(setTopicNameMessage);

                if (rcvMessage.result) {
                    mainTopics.get(currentTopicID).SetTopicName(newMainName);
                } else {
                    mainTopicNameModify.setText(mainTopics.get(currentTopicID).GetTopicName());
                }
            }
            else
            {
                mainTopics.get(currentTopicID).SetTopicName(newMainName);
            }

        }
    }

    private void OpenRoomManagement()
    {
        MainClient.roomManagement= new RoomManagement(mainFrame, mainPanel, clientSocket);
        SetActive(false);
        MainClient.roomManagement.SetActive(true);
    }

    private void LeaveRoom()
    {
        if(!MainClient.withoutServerTest) {
            Message leaveRoomMessage = new Message(Task.leave_room);
            leaveRoomMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
            leaveRoomMessage.AddContent(ContentType.user_id, Login.GetUserID());
            Message rcvMessage = clientSocket.SendToServer(leaveRoomMessage);

            if (rcvMessage.result) {
                SetActive(false);
                MainClient.roomLogin.SetActive(true);
                JOptionPane.showMessageDialog(null, rcvMessage.GetContent(0), "Success Message", JOptionPane.PLAIN_MESSAGE);
            }
        }
        else {
            SetActive(false);
            MainClient.roomLogin.SetActive(true);
        }
    }

    private void AddMainTopic()
    {
        addTopicFrame = new Frame("Add Main Topic");
        addTopicFrame.setSize(addTopicWidth, addTopicHeight);
        addTopicFrame.setLocation(mainFrame.getX() + mainFrame.getWidth() / 2 - addTopicWidth / 2, mainFrame.getY() + mainFrame.getHeight() / 2 - addTopicHeight / 2);
        addTopicFrame.addWindowListener(new AddTopicEventManager(addTopicFrame));
        addTopicFrame.setResizable(false);
        addTopicFrame.setVisible(true);

        DrawNewTopicInfo();
    }

    private void DrawNewTopicInfo()
    {
        addTopicPanel = new Panel();
        addTopicPanel.setLayout(null);
        addTopicPanel.setSize(addTopicWidth, addTopicHeight);

        aTManagerComboBox = new JComboBox(memberNames);
        aTManagerComboBox.setFont(new Font("Consolas", Font.BOLD, 20));
        aTManagerComboBox.setBounds(addTopicWidth / 2 - comboBoxWidth / 4, addTopicHeight / 2 - addTopicLabelHeight, comboBoxWidth, addTopicLabelHeight);

        addTopicNameInput = new TextField();
        addTopicNameInput.setBounds(aTManagerComboBox.getX(), aTManagerComboBox.getY() - headerHeight - interval, comboBoxWidth, addTopicLabelHeight);
        addTopicNameInput.setFont(new Font("Consolas", Font.PLAIN, 18));

        aTDeadlineInput = new TextField();
        aTDeadlineInput.setBounds(aTManagerComboBox.getX(), aTManagerComboBox.getY() + headerHeight + interval, comboBoxWidth, addTopicLabelHeight);
        aTDeadlineInput.setFont(new Font("Consolas", Font.PLAIN, 18));

        aTButton = new Button("Confirm");
        aTButton.setFont(new Font("Consolas", Font.PLAIN, 18));
        aTButton.setBounds(addTopicWidth / 2 - buttonWidth / 4, addTopicHeight - buttonHeight - 4 * interval, buttonWidth / 2, buttonHeight);
        aTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMainTopicProcess();
            }
        });

        addTopicTitleLabel = new Label("Add Topic");
        addTopicTitleLabel.setFont(new Font("Consolas", Font.PLAIN, 23));
        addTopicTitleLabel.setBounds(addTopicWidth / 2 - addTopicLabelWidth / 2, addTopicHeight / 8, addTopicLabelWidth, addTopicLabelHeight);

        addTopicManagerLabel = new Label("Manager");
        addTopicManagerLabel.setFont(new Font("Consolas", Font.PLAIN, 18));
        addTopicManagerLabel.setBounds(aTManagerComboBox.getX() - addTopicLabelWidth - interval, aTManagerComboBox.getY(), addTopicLabelWidth, addTopicLabelHeight);

        addTopicNameLabel = new Label("Topic Name");
        addTopicNameLabel.setFont(new Font("Consolas", Font.PLAIN, 18));
        addTopicNameLabel.setBounds(aTManagerComboBox.getX() - addTopicLabelWidth - interval, addTopicNameInput.getY(), addTopicLabelWidth, addTopicLabelHeight);

        addTopicDeadlineLabel = new Label("Deadline");
        addTopicDeadlineLabel.setFont(new Font("Consolas", Font.PLAIN, 18));
        addTopicDeadlineLabel.setBounds(aTManagerComboBox.getX() - addTopicLabelWidth - interval, aTDeadlineInput.getY(), addTopicLabelWidth, addTopicLabelHeight);

        aTDeadlineFormatLabel = new Label("Please enter it in the form of yyyy-MM-dd or yyyyMMdd");
        aTDeadlineFormatLabel.setFont(new Font("Consolas", Font.PLAIN, 14));
        aTDeadlineFormatLabel.setBounds(0, aTDeadlineInput.getY() + addTopicLabelHeight + interval, addTopicWidth, addTopicLabelHeight * 2);

        addTopicTitleLabel.setAlignment(Label.CENTER);
        addTopicManagerLabel.setAlignment(Label.RIGHT);
        addTopicNameLabel.setAlignment(Label.RIGHT);
        addTopicDeadlineLabel.setAlignment(Label.RIGHT);
        aTDeadlineFormatLabel.setAlignment(Label.CENTER);

        addTopicPanel.add(addTopicTitleLabel);
        addTopicPanel.add(addTopicNameInput);
        addTopicPanel.add(aTManagerComboBox);
        addTopicPanel.add(aTDeadlineInput);
        addTopicPanel.add(aTButton);
        addTopicPanel.add(addTopicManagerLabel);
        addTopicPanel.add(addTopicNameLabel);
        addTopicPanel.add(addTopicDeadlineLabel);
        addTopicPanel.add(aTDeadlineFormatLabel);

        addTopicFrame.add(addTopicPanel);
    }

    public String DateFormat(String original)
    {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            return dateFormat.format(dateFormat.parse(original));

        } catch(ParseException e){
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                return newDateFormat.format(dateFormat.parse(original));

            } catch(ParseException e2){
                return null;
            }
        }
    }

    private void AddMainTopicProcess()
    {
        String topicName = addTopicNameInput.getText();
        String topicManager = memberList.get(aTManagerComboBox.getSelectedIndex()).GetMemberID();
        String topicDeadLine = DateFormat(aTDeadlineInput.getText());

        if(topicDeadLine != null)
        {
            if(!MainClient.withoutServerTest) {
                Message addTopicMessage = new Message(Task.add_main_topic);
                addTopicMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
                addTopicMessage.AddContent(ContentType.topic_id, Integer.toString(mainTopicLength));
                addTopicMessage.AddContent(ContentType.topic_name, topicName);
                addTopicMessage.AddContent(ContentType.topic_manager, topicManager);
                addTopicMessage.AddContent(ContentType.topic_deadline, topicDeadLine);
                addTopicMessage.AddContent(ContentType.user_id, Login.GetUserID());

                Message rcvMessage = clientSocket.SendToServer(addTopicMessage);

                if (rcvMessage.result) {
                    MainTopic topic = new MainTopic(this, topicName, false, topicDeadLine, topicManager, GetPanel(panelType.MainTopic).getWidth() - 4 * interval, headerHeight, mainTopicLength);

                    mainTopicLength++;
                    mainTopics.add(topic);
                    mainTopicScrollPanel.add(topic);
                    mainTopicScrollPanel.setPreferredSize(new Dimension(leftWidth, (mainTopics.get(mainTopics.size() - 1).getY() +  headerHeight - (mainTopics.get(0).getY() - 2 * interval))));
                    mainScroll.getViewport().revalidate();
                    mainScroll.getViewport().repaint();
                    addTopicFrame.setVisible(false);
                }
            }
            else
            {
                MainTopic topic = new MainTopic(this, topicName, false, topicDeadLine, topicManager, GetPanel(panelType.MainTopic).getWidth() - 4 * interval, headerHeight, mainTopicLength);

                mainTopicLength++;
                mainTopics.add(topic);
                mainTopicScrollPanel.add(topic);
                mainTopicScrollPanel.setPreferredSize(new Dimension(leftWidth, (mainTopics.get(mainTopics.size() - 1).getY() +  headerHeight - (mainTopics.get(0).getY() - 2 * interval))));
                mainScroll.getViewport().revalidate();
                mainScroll.getViewport().repaint();
                addTopicFrame.setVisible(false);
            }

        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please enter it in the form of yyyy-MM-dd or yyyyMMdd", "[Invalid Format]", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void OpenModifyInfo()
    {
        SetActive(false);
        MainClient.modification.SetActive(true);
    }

    public void SetCurrentTopicID(int id)
    {
        currentTopicID = id;
    }

    private void AddSubTopic()
    {
        String subName = JOptionPane.showInputDialog(null, "What is sub-topic name?");

        if(!subName.equals("")) {
            if (!MainClient.withoutServerTest) {
                Message addSubMessage = new Message(Task.add_sub_topic);
                addSubMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
                addSubMessage.AddContent(ContentType.topic_id, Integer.toString(currentTopicID));
                addSubMessage.AddContent(ContentType.sub_id, Integer.toString(subTopics.size()));
                addSubMessage.AddContent(ContentType.sub_name, subName);
                addSubMessage.AddContent(ContentType.user_id, Login.GetUserID());

                Message rcvMessage = clientSocket.SendToServer(addSubMessage);

                if (rcvMessage.result) {
                    SubTopic topic = new SubTopic(this, subName, false, GetPanel(panelType.SubTopic).getWidth() - 4 * interval, headerHeight, mainTopicNamePanel.getHeight(), subTopics.size());
                    subTopics.add(topic);
                    subTopicScrollPanel.add(topic);
                    subTopicScrollPanel.setPreferredSize(new Dimension(leftWidth, (subTopics.get(subTopics.size() - 1).getY() + headerHeight - (mainTopicNamePanel.getY() - 2 * interval))));
                    subScroll.getViewport().revalidate();
                    subScroll.getViewport().repaint();
                }
            } else {
                SubTopic topic = new SubTopic(this, subName, false, GetPanel(panelType.SubTopic).getWidth() - 4 * interval, headerHeight, mainTopicNamePanel.getHeight(), subTopics.size());
                subTopics.add(topic);
                subTopicScrollPanel.add(topic);
                subTopicScrollPanel.setPreferredSize(new Dimension(leftWidth, (subTopics.get(subTopics.size() - 1).getY() + headerHeight - (mainTopicNamePanel.getY() - 2 * interval))));
                subScroll.getViewport().revalidate();
                subScroll.getViewport().repaint();
            }
        }
    }

    private void RoomMngButtonActive(boolean active)
    {
        if(active)
        {
            GetPanel(panelType.Header).add(roomManagementButton);
        }
        else
        {
            GetPanel(panelType.Header).remove(roomManagementButton);
        }
    }

    public void SetMainComplete(boolean completed, int id)
    {
        if(!MainClient.withoutServerTest) {
            int checked = completed ? 0 : 1;
            Message mainCompleteMessage = new Message(Task.set_main_complete);
            mainCompleteMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
            mainCompleteMessage.AddContent(ContentType.topic_id, Integer.toString(id));
            mainCompleteMessage.AddContent(ContentType.topic_completed, Integer.toString(checked));
            mainCompleteMessage.AddContent(ContentType.user_id, Login.GetUserID());
            Message rcvMessage = clientSocket.SendToServer(mainCompleteMessage);

            if (rcvMessage.result) {
                mainTopics.get(id).CheckToDo();
            }
        }
        else
            mainTopics.get(id).CheckToDo();
    }
    public void SetSubComplete(boolean completed, int id)
    {
        if(!MainClient.withoutServerTest) {
            int checked = completed ? 0 : 1;
            Message subCompleteMessage = new Message(Task.set_sub_complete);
            subCompleteMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
            subCompleteMessage.AddContent(ContentType.topic_id, Integer.toString(currentTopicID));
            subCompleteMessage.AddContent(ContentType.sub_id, Integer.toString(id));
            subCompleteMessage.AddContent(ContentType.sub_completed, Integer.toString(checked));
            subCompleteMessage.AddContent(ContentType.user_id, Login.GetUserID());
            Message rcvMessage = clientSocket.SendToServer(subCompleteMessage);

            if (rcvMessage.result) {
                subTopics.get(id).CheckToDo();
            }
        }
        else
            subTopics.get(id).CheckToDo();
    }

    public void SetDeadline(String newDeadline, int topicID, JTextField deadlineTextField)
    {
        if(!MainClient.withoutServerTest) {
            Message setDeadlineMessage = new Message(Task.set_deadline);
            setDeadlineMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
            setDeadlineMessage.AddContent(ContentType.topic_id, Integer.toString(topicID));
            setDeadlineMessage.AddContent(ContentType.topic_deadline, newDeadline);
            setDeadlineMessage.AddContent(ContentType.user_id, Login.GetUserID());
            Message rcvMessage = clientSocket.SendToServer(setDeadlineMessage);

            if (rcvMessage.result) {
                mainTopics.get(topicID).SetDeadline(newDeadline);
                deadlineTextField.setText(newDeadline);
            } else {
                deadlineTextField.setText(mainTopics.get(topicID).GetDeadline());
            }
        }
        else
        {
            mainTopics.get(topicID).SetDeadline(newDeadline);
            deadlineTextField.setText(newDeadline);
        }
    }

    private void SetManager()
    {
        String managerID =  memberList.get(managerComboBox.getSelectedIndex()).GetMemberID();
        System.out.println(managerID);
        if(!MainClient.withoutServerTest) {
            Message setManagerMessage = new Message(Task.set_manager);
            setManagerMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
            setManagerMessage.AddContent(ContentType.topic_id, Integer.toString(currentTopicID));
            setManagerMessage.AddContent(ContentType.topic_manager, managerID);

            setManagerMessage.AddContent(ContentType.user_id, Login.GetUserID());
            Message rcvMessage = clientSocket.SendToServer(setManagerMessage);

            if (rcvMessage.result) {
                mainTopics.get(currentTopicID).SetManager(managerID);
            } else {
                managerComboBox.setSelectedIndex(GetManagerIndexByID(mainTopics.get(currentTopicID).GetManager()));
            }
        }
        else
        {
            mainTopics.get(currentTopicID).SetManager(managerID);
        }
    }

    public void SetSubTopicName(String newName, int id, JTextField subNameModify)
    {
        if(!newName.equals(mainTopics.get(currentTopicID).GetTopicName()))
        {
            if(!MainClient.withoutServerTest) {
                Message setTopicNameMessage = new Message(Task.set_sub_name);
                setTopicNameMessage.AddContent(ContentType.room_id, RoomLogin.GetRoomID());
                setTopicNameMessage.AddContent(ContentType.topic_id, Integer.toString(currentTopicID));
                setTopicNameMessage.AddContent(ContentType.sub_id, Integer.toString(id));
                setTopicNameMessage.AddContent(ContentType.new_sub_name, newName);
                setTopicNameMessage.AddContent(ContentType.user_id, Login.GetUserID());
                Message rcvMessage = clientSocket.SendToServer(setTopicNameMessage);

                if (rcvMessage.result) {
                    subTopics.get(currentTopicID).SetSubTopicName(newName);
                } else {
                    subNameModify.setText(subTopics.get(id).GetTopicName());
                }
            }
            else
            {
                subTopics.get(currentTopicID).SetSubTopicName(newName);
            }

        }
    }

    public int GetManagerIndexByID(String managerID)
    {
        for(int i = 0; i < memberList.size(); i++)
        {
            if(memberList.get(i).GetMemberID().equals(managerID))
            {
                return i;
            }
        }
        return -1;
    }

    private void SynchronizeDisplay()
    {
        GetPanel(panelType.MainTopic).remove(mainScroll);
        DrawMainTopics();
        mainScroll.getViewport().revalidate();
        mainScroll.getViewport().repaint();
        if(subScroll != null) {
            GetPanel(panelType.SubTopic).remove(subScroll);
            DrawSubTopics(currentTopicID);
            subScroll.getViewport().revalidate();
            subScroll.getViewport().repaint();
        }
    }

    public void SetActive(boolean onOff)
    {
        if(onOff) {
            task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println(LocalTime.now() + " / Synchronize");
                    SynchronizeDisplay();
                }
            };
            mainPanel.add(GetPanel(panelType.Main));
            timer.schedule(task, MainClient.synchronizeDelay * 1000, MainClient.synchronizeDelay * 1000);
            mainScroll.getViewport().revalidate();
            mainScroll.getViewport().repaint();
        }
        else {
            mainPanel.remove(GetPanel(panelType.Main));
            task.cancel();
        }
    }
}
