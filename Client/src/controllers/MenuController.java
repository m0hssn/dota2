package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import main.Main;
import main.TaskListener;
import main.TaskManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable
{
    private static Stage stage;

    public static Stage getStage()
    {
        return stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        stage = PagesController.getStage();
    }

    public void quitAction(ActionEvent actionEvent) throws IOException
    {
        Main.client.getWriter().writeUTF(TaskManager.getExit(Main.client.getUsername()));

        Main.client.getSocket().close();
        Main.client.getWriter().close();
        Main.client.getReader().close();

        System.exit(0);
    }

    public void settingsAction(ActionEvent actionEvent) throws IOException
    {
        stage.close();
        PagesController.openPage("Settings");
    }

    public void battleAction(ActionEvent actionEvent) throws IOException
    {
        stage.close();
        PagesController.openPage("Search");
    }
}
