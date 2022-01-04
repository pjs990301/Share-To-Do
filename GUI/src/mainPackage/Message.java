package mainPackage;

import javax.swing.*;
import java.util.ArrayList;

public class Message {
    private Task task;
    private ArrayList<ContentType> contentType = new ArrayList<>();
    private ArrayList<String> content = new ArrayList<>();

    private String[] sendKeywords = {"task", "content"};
    private String[] receiveKeywords = {"task", "response", "content", "message"};
    private String divider = "/";
    private String colon = ":";
    private String outMessage = "not a room participant.";
    private String roomNotFouneMsg = "room can't found 404.";
    public boolean result;

    public Message(Task task)
    {
        this.task = task;
    }

    public Message(String msg)
    {
        TranslateFromString(msg);
    }

    public void AddContent(ContentType newContentType, String newContent)
    {
        contentType.add(newContentType);
        content.add(newContent);
    }

    public String GetContent(int index)
    {
        return content.get(index);
    }

    public int GetContentLength()
    {
        return content.size();
    }

    public String TranslateToString()
    {
        String msg = "";

        msg += sendKeywords[0] + colon + task.toString() + divider;

        for(int i = 0; i < contentType.size(); i++)
        {
            msg += contentType.get(i) + colon + content.get(i) + divider;
        }

        return msg;
    }

    public void TranslateFromString(String msg)
    {
        System.out.println(msg);
        task = Task.valueOf(GetContent(msg, receiveKeywords[0], divider));
        msg = ClearPriorMessage(msg, divider);

        result = Boolean.parseBoolean(GetContent(msg, receiveKeywords[1], divider));
        msg = ClearPriorMessage(msg, divider);
        if(result)
        {
            if(GetKeyword(msg).equals(receiveKeywords[3]))
            {
                contentType.add(ContentType.message);
                content.add(GetContent(msg, receiveKeywords[3], divider));
                msg = ClearPriorMessage(msg, divider);
            }
            else
            {
                while(msg.indexOf(divider) != msg.length() - 1)
                {
                    contentType.add(GetContentType(msg));
                    content.add(GetContent(msg, contentType.get(contentType.size() - 1).toString(), divider));
                    msg = ClearPriorMessage(msg, divider);
                }
            }
        }
        else
        {
            String message = GetContent(msg, receiveKeywords[3], divider);
            if(message.equals(outMessage))
            {
                MainClient.todoList.SetActive(false);
                MainClient.roomLogin.SetActive(true);
                JOptionPane.showMessageDialog(null, "You've been kicked from the room.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(message.equals(roomNotFouneMsg))
            {
                MainClient.todoList.SetActive(false);
                MainClient.roomLogin.SetActive(true);
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ContentType GetContentType(String msg)
    {
        return ContentType.valueOf(msg.substring(0, msg.indexOf(colon)));
    }

    private String GetKeyword(String msg) { return msg.substring(0, msg.indexOf(colon)); }

    private String GetContent(String msg, String keyword, String divider)
    {
        return msg.substring(msg.indexOf(keyword) + keyword.length() + 1, msg.indexOf(divider));
    }

    private String ClearPriorMessage(String msg, String divider)
    {
        return msg.substring(msg.indexOf(divider) + 1);
    }
}
