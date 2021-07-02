package main;

import connection.Handler;
import controllers.*;
import javafx.application.Platform;
import org.json.JSONException;
import org.json.JSONObject;
import utills.StaticData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class TaskListener implements Runnable
{
    private DataInputStream reader;
    private JSONObject mapper;
    private static boolean continues = true;

    private static void kill()
    {
        continues = false;
    }

    public TaskListener(DataInputStream reader)
    {
        this.reader = reader;
    }

    @Override
    public void run()
    {
        while (continues)
        {
            try
            {
                String message = reader.readUTF();
                mapper = new JSONObject(message);
                doTask();
            }
            catch (IOException | JSONException e) {}
        }
    }

    private void doTask() throws JSONException, IOException
    {
        String task = mapper.getString("task");

        switch (task)
        {
            case "send message" : show();
            break;

            case "challenge" : challenge();
            break;

            case "kill" : kill();
            break;

            case "game" : playGame();
            break;
        }
    }

    private void playGame()
    {
        Platform.runLater(() ->
        {
            try
            {
                try
                {
                    SearchController.getStage().close();
                }
                catch (Exception e) {}

                try
                {
                    MenuController.getStage().close();
                }
                catch (Exception e) {}

                SettingsController.getStage().close();
            }
            catch (Exception e) {}
        });

        kill();


        Platform.runLater(() ->
        {
            try
            {
//                PagesController.closePage();
//                Main.client.getWriter().writeUTF(TaskManager.getKill());
                Main.client.getWriter().writeUTF(TaskManager.getKill());
                StaticData.gameOver = false;
                MapLoader.openPage("Map");
                try {
                    StaticData.handler = new Handler(Main.client.getWriter(), Main.client.getReader());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Drawer drawer = new Drawer();
                drawer.start();

            }
            catch (IOException e) {}
        });
    }

    private void challenge() throws JSONException
    {
        String message = mapper.getString("message");
        int number = mapper.getInt("number");

        Platform.runLater(() ->
        {
            try
            {
                NotificationController.setReq(message);
                NotificationController.setNumber(number);
                PagesController.openPage("Notification");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        });
    }

    private void show() throws JSONException
    {
        String message = mapper.getString("message");

        Platform.runLater(() -> SearchController.staticLogger.setText(message));
    }
}
