package main;

import data.User;
import data.database.DataBase;

import java.net.Socket;
import java.util.*;

public class Main
{
    public static List<User> users = new ArrayList<>();
    public static Map<User, Socket> onlineUsers = new HashMap<>();
    public static Server server;

    public static void main(String[] args) throws Exception
    {
        DataBase.read();
        server = new Server();

        while (true)
        {
            server.waitForConnection();

            TaskListener listener = new TaskListener(server.getReader(), server.getWriter());

            server.setKiller(listener);

            Thread thread = new Thread(listener);

            thread.start();
        }
    }
}
