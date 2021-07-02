package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import main.Main;
import main.TaskListener;
import main.TaskManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    @FXML
    private AnchorPane pane;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private Label label;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        if(SplashScreenController.isFinished)
        {
            try
            {
                SplashScreenController.show(pane);
            }
            catch (Exception e)
            {
            }
        }

        username.setStyle("-fx-text-inner-color: #ffffff;");
        password.setStyle("-fx-text-inner-color: #ffffff;");
    }

    public void signUpClick(ActionEvent actionEvent) throws IOException
    {
        PagesController.closePage(actionEvent);
        PagesController.openPage("SignUp");
    }


    public void loginClick(ActionEvent actionEvent) throws IOException
    {
        String name = username.getText(), pass = password.getText();
        boolean flag = true;

        Main.client.getWriter().writeUTF(TaskManager.getTask(name, pass));
        boolean validation = Main.client.getReader().readBoolean();

        if(validation)
        {
            flag = false;
            Main.client.setUsername(name);

            TaskListener listener = new TaskListener(Main.client.getReader());
            Thread thread = new Thread(listener);
            thread.start();

            PagesController.closePage(actionEvent);
            PagesController.openPage("Main Menu");
        }

        if(flag)
        {
            label.setText("invalid");
        }
    }

    public void quitClick(ActionEvent actionEvent) throws IOException
    {
        Main.client.getWriter().writeUTF(TaskManager.getExit(Main.client.getUsername()));

        Main.client.getSocket().close();
        Main.client.getWriter().close();
        Main.client.getReader().close();

        System.exit(0);
    }
}
