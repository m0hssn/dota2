package controllers;

import main.*;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable
{
    @FXML
    private Label label;

    @FXML
    private JFXTextField username, email;

    @FXML
    private JFXPasswordField password;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        username.setStyle("-fx-text-inner-color: #ffffff;");
        password.setStyle("-fx-text-inner-color: #ffffff;");
        email.setStyle("-fx-text-inner-color: #ffffff;");
    }

    public void signUpClick(ActionEvent actionEvent) throws Exception
    {
        boolean flag = true;

        String userName = username.getText();
        String pass = password.getText();
        String mail = email.getText();

        Main.client.getWriter().writeUTF(TaskManager.getTask(userName));
        boolean exists = Main.client.getReader().readBoolean();


        if(exists)
        {
            label.setText("this username is used");
            flag = false;
        }

        Main.client.getWriter().writeUTF(TaskManager.getCheckEmail(mail));
        boolean duplicateEmail = Main.client.getReader().readBoolean();

        if(duplicateEmail)
        {
            label.setText("this email is used");
            flag = false;
        }

        if(!userName.matches("\\w*") || userName.length() < 4)
        {
            label.setText("invalid username");
            flag = false;
        }

        if(userName.length() > 20)
        {
            label.setText("username is too long");
            flag = false;
        }

        if(pass.contains(" "))
        {
            label.setText("invalid password");
            flag = false;
        }

        if(pass.length() < 8)
        {
            label.setText("password is too short");
            flag = false;
        }

        if(!mail.matches("(\\w|[\\.])*@gmail.com"))
        {
            label.setText("invalid email");
            flag = false;
        }

        if(flag)
        {
            Main.client.getWriter().writeUTF(TaskManager.getTask(userName, pass, mail));

            Thread.sleep(500);

            PagesController.closePage(actionEvent);
            PagesController.openPage("Login");
        }
    }

    public void backClick(ActionEvent actionEvent) throws IOException
    {
        PagesController.closePage(actionEvent);
        PagesController.openPage("Login");
    }
}
