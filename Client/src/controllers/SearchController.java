package controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import main.Main;
import main.TaskManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable
{
    private static Stage stage;

    public static Stage getStage()
    {
        return stage;
    }

    @FXML
    private JFXTextField textField;

    @FXML
    private Label logLBL;

    public static Label staticLogger;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        stage = PagesController.getStage();
        staticLogger = logLBL;
    }

    public void menuAction(ActionEvent actionEvent) throws IOException
    {
        stage.close();
        PagesController.openPage("Main Menu");
    }

    public void searchAction(ActionEvent actionEvent) throws IOException
    {
        String userName = textField.getText();

        if(userName.equals(Main.client.getUsername()))
        {
            staticLogger.setText("Why are you searching yourself ?");
        }
        else
        {
            Main.client.getWriter().writeUTF(TaskManager.searchTask(userName));
        }
    }
}
