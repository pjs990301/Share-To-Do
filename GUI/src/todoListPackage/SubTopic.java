package todoListPackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SubTopic extends JPanel {
    private String topicName;
    private int topicID;
    private boolean checked = false;

    private Color backgroundColor = new Color(255, 255, 255);
    private JTextField topicNameModify;
    private JButton checkButton;

    private ImageIcon circleImg = new ImageIcon("./ImgSrc/circle.png");
    private ImageIcon checkImg = new ImageIcon("./ImgSrc/check.png");

    private TodoList todoList;

    private int checkBoxSize = 50;
    private int interval = 10;

    public SubTopic(TodoList todoList, String topicName, boolean completed, int width, int height, int upperBound, int index)
    {
        this.todoList = todoList;
        setLayout(null);
        setBounds(interval, upperBound + 2 * interval + (height + interval) * index, width, height);
        setBackground(backgroundColor);
        setBorder(new LineBorder(new Color(0, 0, 0)));

        this.topicName = topicName;
        topicID = index;

        topicNameModify = new JTextField(topicName);
        topicNameModify.setBounds(checkBoxSize + 3 * interval, interval / 2, width, height - interval);
        topicNameModify.setAlignmentY(SwingConstants.CENTER);
        Font topicFont = new Font("Consolas", Font.PLAIN, 23);
        topicNameModify.setFont(topicFont);
        topicNameModify.setBorder(null);

        topicNameModify.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetSubTopicName();
            }
        });

        checkButton = new JButton(circleImg);
        checkButton.setBounds(interval / 2, interval / 2, checkBoxSize, checkBoxSize);
        checkButton.setBorderPainted(false);
        checkButton.setContentAreaFilled(false);
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoList.SetSubComplete(checked, topicID);
            }
        });

        add(checkButton);
        add(topicNameModify);
    }

    public int GetTopicID()
    {
        return topicID;
    }

    private void SetSubTopicName()
    {
        String newSubName = topicNameModify.getText();
        if(!newSubName.equals(topicName))
        {
            todoList.SetSubTopicName(newSubName, topicID, topicNameModify);
        }
    }

    public void SetSubTopicName(String newName)
    {
        topicName = newName;
    }

    public void CheckToDo()
    {
        checked = !checked;
        if(checked)
            checkButton.setIcon(checkImg);
        else
            checkButton.setIcon(circleImg);

    }
    public String GetTopicName()
    {
        return topicName;
    }
}
