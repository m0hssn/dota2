package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;


public class Server
{
    private final int port = 8585;
    private ServerSocket server;
    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
    private static int connection = 0;
    private static Map<Integer, Socket> connections = new HashMap<>();
    private static Map<Integer, TaskListener> killer = new HashMap<>();

    public Server() throws IOException
    {
        server = new ServerSocket(port);
    }

    public void setKiller(TaskListener listener)
    {
        killer.put(connection, listener);
    }

    public DataInputStream getReader()
    {
        return reader;
    }

    public DataOutputStream getWriter()
    {
        return writer;
    }

    public void kill(int number) throws IOException
    {
        killer.get(number).kill();
        killer.remove(number);

        connections.get(number).close();
        connections.remove(number);
    }

    public static Map<Integer, TaskListener> getKiller()
    {
        return killer;
    }

    public void waitForConnection() throws IOException
    {
        socket = server.accept();
        reader = new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());

        connection ++;
        connections.put(connection, socket);

        writer.writeInt(connection);
    }

    public Socket getSocket()
    {
        return socket;
    }

    public Map<Integer, Socket> getConnections()
    {
        return connections;
    }
}
