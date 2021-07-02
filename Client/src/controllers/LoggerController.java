package controllers;

import connection.Handler;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import main.Main;
import main.TaskManager;
import org.json.JSONException;
import utills.StaticData;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoggerController implements Initializable
{
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
    }

    public void acceptAction(ActionEvent actionEvent) throws IOException, JSONException, InterruptedException
    {
        PagesController.closePage(actionEvent);
        Main.client.getWriter().writeUTF(TaskManager.getKill());

        StaticData.gameOver = false;
        MapLoader.openPage("Map");
        StaticData.handler = new Handler(Main.client.getWriter(), Main.client.getReader());
        Drawer drawer = new Drawer();
        drawer.start();
    }
}
