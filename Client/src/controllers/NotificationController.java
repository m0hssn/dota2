package controllers;

import connection.Handler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;
import main.TaskManager;
import org.json.JSONException;
import utills.StaticData;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NotificationController implements Initializable
{
    @FXML
    private Label messageLBL;
    private Stage stage;

    private static String req;
    private static String from;
    private static int number;

    public static void setNumber(int number)
    {
        NotificationController.number = number;
    }

    public static void setReq(String req)
    {
        NotificationController.req = req;
        from = req.substring(req.indexOf("-") + 1, req.lastIndexOf("-"));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        stage = PagesController.getStage();
        messageLBL.setText(req);
    }

    public void rejectAction(ActionEvent actionEvent)
    {
        stage.close();
    }

    public void acceptAction(ActionEvent actionEvent) throws IOException, JSONException, InterruptedException
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

        PagesController.closePage(actionEvent);

        Main.client.getWriter().writeUTF(TaskManager.getGame(from, Main.client.getUsername(), number));


        StaticData.gameOver = false;
        MapLoader.openPage("Map");
        StaticData.handler = new Handler(Main.client.getWriter(), Main.client.getReader());
        Drawer drawer = new Drawer();
        drawer.start();
    }
}
