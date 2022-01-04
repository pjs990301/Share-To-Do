package todoListPackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainTopic extends JButton {
    private int topicID;
    private String topicName;
    private String deadline;
    private String manager;

    private Color backgroundColor = new Color(211, 211, 211);
    private JLabel topicNameLabel;
    private JButton checkButton;
    private JTextField deadlineTextField;

    private ImageIcon circleImg = new ImageIcon("./ImgSrc/circle.png");
    private ImageIcon checkImg = new ImageIcon("./ImgSrc/check.png");

    private int deadlineWidth = 150;
    private int checkBoxSize = 50;
    private int interval = 10;

    private boolean checked = false;
    private TodoList todoList;

    public MainTopic(TodoList todoList, String topicName, boolean completed, String deadline, String manager, int width, int height, int index)
    {
        topicID = index;
        this.todoList = todoList;
        setLayout(null);
        setBounds(interval, 2 * interval + (height + interval) * index, width, height);
        setBackground(backgroundColor);
        setBorder(new LineBorder(new Color(0, 0, 0)));

        this.topicName = topicName;
        this.deadline = deadline;
        this.manager = manager;
        checked = completed;

        topicNameLabel = new JLabel(topicName);
        topicNameLabel.setBounds(checkBoxSize + 2 * interval, 0, width, height);
        topicNameLabel.setVerticalAlignment(SwingConstants.CENTER);
        Font topicFont = new Font("Consolas", Font.PLAIN, 23);
        topicNameLabel.setFont(topicFont);

        checkButton = new JButton(circleImg);
        checkButton.setBounds(interval / 2, interval / 2, checkBoxSize, checkBoxSize);
        checkButton.setBorderPainted(false);
        checkButton.setContentAreaFilled(false);
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoList.SetMainComplete(checked, topicID);
            }
        });

        deadlineTextField = new JTextField(deadline);
        deadlineTextField.setBounds(getWidth() - deadlineWidth - interval / 2, interval / 2, deadlineWidth, height - interval);
        deadlineTextField.setHorizontalAlignment(SwingConstants.CENTER);
        deadlineTextField.setFont(new Font("Consolas", Font.BOLD, 20));
        deadlineTextField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetDeadline();
            }
        });

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OpenSubtopic();
            }
        });
        SetCheck();

        add(checkButton);
        add(topicNameLabel);
        add(deadlineTextField);
    }

    public void SetDeadline()
    {
        String newDeadline = "";
        if(deadlineTextField.getText().substring(0, 1).equals("~"))
            newDeadline = deadlineTextField.getText().substring(1);
        else
            newDeadline = deadlineTextField.getText();

        newDeadline = todoList.DateFormat(newDeadline);

        if(newDeadline != null)
        {
            todoList.SetDeadline(newDeadline, topicID, deadlineTextField);
        }
        else
            deadlineTextField.setText(deadline);
    }

    public int GetID()
    {
        return topicID;
    }

    public void SetTopicName(String newName)
    {
        topicName = newName;
    }
    public String GetTopicName()
    {
        return topicName;
    }
    public String GetDeadline()
    {
        return deadline;
    }
    public String GetManager()
    {
        return manager;
    }
    public void SetDeadline(String newDeadline)
    {
        deadline = newDeadline;
    }

    private void OpenSubtopic()
    {
        todoList.SetCurrentTopicID(topicID);
        todoList.DrawSubTopics(topicID);
    }

    public void CheckToDo()
    {
        checked = !checked;
        if(checked)
            checkButton.setIcon(checkImg);
        else
            checkButton.setIcon(circleImg);
    }

    private void SetCheck()
    {
        if(checked)
            checkButton.setIcon(checkImg);
        else
            checkButton.setIcon(circleImg);
    }

    public void SetManager(String newManager)
    {
        manager = newManager;
    }
}
