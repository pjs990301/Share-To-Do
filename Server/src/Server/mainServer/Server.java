package Server.mainServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static String ip = "183.99.22.179";
    private static int numPort = 9999;
 
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(numPort);
        Socket socket = null;
        try {
            while (true) {
                socket = listener.accept();
                new ServerThread(socket).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            socket.close();
            listener.close();
        }
    }
}

