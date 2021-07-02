package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client
{
    private final int port = 8585;
    private final String ip = "localhost";
    private Socket socket;
    private DataOutputStream writer;
    private DataInputStream reader;
    private int connectionNumber;
    private String username;

    public String getUsername()
    {
        return username;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Client()
    {
    }

    public void connect() throws IOException
    {
        socket = new Socket(ip, port);
        writer = new DataOutputStream(socket.getOutputStream());
        reader = new DataInputStream(socket.getInputStream());
        connectionNumber = reader.readInt();
    }

    public DataOutputStream getWriter()
    {
        return writer;
    }

    public DataInputStream getReader()
    {
        return reader;
    }

    public int getNumber()
    {
        return connectionNumber;
    }
}
