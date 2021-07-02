package main;

import data.User;
import data.database.DataBase;
import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.MessagingException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ConcurrentModificationException;

public class TaskListener implements Runnable
{
    private DataInputStream listener;
    private DataOutputStream writer;
    private JSONObject mapper;
    private boolean continues = true;

    public TaskListener(DataInputStream listener, DataOutputStream writer)
    {
        this.listener = listener;
        this.writer = writer;
    }

    public void kill()
    {
        continues = false;
    }

    @Override
    public void run()
    {
        while (continues)
        {
            try
            {
                String jsonTask = listener.readUTF();
                mapper = new JSONObject(jsonTask);
                doTask();
            }
            catch (Exception e){}
        }
    }

    private void doTask() throws Exception
    {
        String task = mapper.getString("task");

        switch (task)
        {
            case "check username" : checkUsername();
            break;

            case "check email" : checkEmail();
            break;

            case "login" : checkLogin();
            break;

            case "signup" : signUp();
            break;

            case "search" : search();
            break;

            case "exit" : exit();
            break;

            case "game" : playGame();
            break;

            case "kill" : kill();
            break;
        }
    }

    private void playGame() throws JSONException, IOException
    {
        // from -> user who requested, to -> user who accepted
        String from = mapper.getString("from"), to = mapper.getString("to");

        DataOutputStream fromWriter = null;
        DataInputStream fromReader = null;

        for (User user : Main.onlineUsers.keySet())
        {
            if(user.getUsername().equals(from))
            {
                fromWriter = new DataOutputStream(Main.onlineUsers.get(user).getOutputStream());
                fromReader = new DataInputStream(Main.onlineUsers.get(user).getInputStream());

                fromWriter.writeUTF(TaskManager.getGame());

                writer.writeUTF(TaskManager.getKill());
            }
        }

        kill();


        DataInputStream[] inputStreams = new DataInputStream[2];
        DataOutputStream[] outputStreams = new DataOutputStream[2];

        inputStreams[0] = listener;
        outputStreams[0] = writer;
        inputStreams[1] = fromReader;
        outputStreams[1] = fromWriter;

        GameRunnable runnable = new GameRunnable(inputStreams, outputStreams);
        runnable.setPlayers(from, to);
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void exit() throws JSONException, IOException
    {
        String username = mapper.getString("username");

        try
        {
            Main.onlineUsers.keySet().removeIf(user -> user.getUsername().equals(username));
        }
        catch (ConcurrentModificationException e) {}

        Main.server.kill(mapper.getInt("number"));

        listener.close();
        writer.close();
    }

    private void checkEmail() throws JSONException, IOException
    {
        String email = mapper.getString("email");
        boolean flag = true;

        for (User user : Main.users)
        {
            if(user.getEmail().equals(email))
            {
                writer.writeBoolean(true);
                flag = false;
            }
        }

        if(flag)
        {
            writer.writeBoolean(false);
        }
    }

    private void checkUsername() throws JSONException, IOException, NullPointerException
    {
        String username = mapper.getString("username");
        boolean flag = true;

        for (User user : Main.users)
        {
            if(user.getUsername().equals(username))
            {
                writer.writeBoolean(true);
            }
        }

        if(flag)
        {
            writer.writeBoolean(false);
        }
    }

    private void checkLogin() throws JSONException, IOException, NullPointerException
    {
        String username = mapper.getString("username"), password = mapper.getString("password");
        boolean flag = true;

        for (User user : Main.users)
        {
            if(user.getUsername().equals(username) && user.getPassword().equals(password) &&
                    Main.onlineUsers.keySet().stream().filter(
                            user1 -> user1.getUsername().equals(user.getUsername())).count() == 0)
            {
                writer.writeBoolean(true);
                flag = false;

                Main.onlineUsers.put(user,
                        (Main.server.getConnections().get(Integer.parseInt(mapper.getString("number")))));
            }
        }

        if(flag)
        {
            writer.writeBoolean(false);
        }
    }

    private void signUp() throws Exception
    {
        User user = new User(mapper.getString("username"), mapper.getString("password"),
                mapper.getString("email"));

        Main.users.add(user);
        DataBase.saveUser(user);
        sendMail(user);
    }

    private void sendMail(User user) throws MessagingException
    {
        String message = "Welcome to dota 2 game!\n" +
                "this game was created by Mehrab Kalantari, Mohsen Sadeghi, Ali Honarmandi.\n" +
                "enjoy the game!";

        GoogleMail.Send(user.getEmail(),
                "Sign Up Successful", message);
    }

    private void search() throws JSONException, IOException, MessagingException
    {
        String from = mapper.getString("from");
        String to = mapper.getString("to");
        int number = mapper.getInt("number");

        if(Main.users.stream().filter(user -> user.getUsername().equals(to)).count() == 0)
        {
            writer.writeUTF(TaskManager.getTask("this user does not exits"));
        }

        else if(Main.onlineUsers.keySet().stream().filter(user -> user.getUsername().equals(to)).count() == 0)
        {
            writer.writeUTF(TaskManager.getTask("player is not online now"));

            User toUser = null;

            for (User user : Main.users)
            {
                if(user.getUsername().equals(to))
                {
                    toUser = user;
                }
            }

            GoogleMail.Send(toUser.getEmail(),
                    "Challenge To Battle", "you were challenged to battle by player \"" +
                    from + "\"");
        }

        else
        {

            writer.writeUTF(TaskManager.getTask("request sent to player"));

            for (User user : Main.onlineUsers.keySet())
            {
                if(user.getUsername().equals(to))
                {
                    Socket playerSocket = Main.onlineUsers.get(user);
                    new DataOutputStream(playerSocket.getOutputStream()).writeUTF(TaskManager.getChallengeTask(
                            "player -" + from + "- challenged you to battle !!", number));
                }
            }
        }
    }
}
