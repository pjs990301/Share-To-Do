package mainPackage;

import java.net.*;
import java.io.*;
import java.util.Scanner;


public class ClientSocket {
    private Socket clientSocket = null;
    private BufferedReader fromServer = null;
    private DataOutputStream toServer = null;

    private String ip = "183.99.22.179";
    private int port = 9999;

    public ClientSocket()
    {
        if(!MainClient.withoutServerTest) {
            try {
                Scanner serverInfoReader = new Scanner(new File("serverInfo.txt"));
                while(serverInfoReader.hasNext())
                {
                    serverInfoReader.next();
                    ip = serverInfoReader.next();

                    serverInfoReader.next();
                    port = serverInfoReader.nextInt();
                }

                clientSocket = new Socket(ip, port);
                toServer = new DataOutputStream(clientSocket.getOutputStream());
                fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (Exception e) {
                System.out.println("[Error] " + e.getMessage());
            }
        }
    }

    public void CloseConnection()
    {
        try{
            clientSocket.close();
        } catch (Exception e) {
            System.out.println("[Error] " + e.getMessage());
        }
    }

    public Message SendToServer(Message message)
    {
        String sendMsg, rcvMsg;
        try{
            sendMsg = message.TranslateToString();
            System.out.println("send: " + sendMsg);
            toServer.writeBytes(sendMsg + '\n');

            rcvMsg = fromServer.readLine();
            Message msg = new Message(rcvMsg);
            return msg;

        }catch (Exception e) {
            System.out.println("[Error] " + e.getMessage());
        }

        return null;
    }
}
