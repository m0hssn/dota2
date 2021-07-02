package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import main.Main;
import main.MusicPlayer;
import main.TaskManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable
{
    @FXML
    private JFXButton musicBTN;

    private static Stage stage;
    private static String music_condition = "Music(On)";

    public static Stage getStage()
    {
        return stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        musicBTN.setText(music_condition);
        stage = PagesController.getStage();
    }

    public void menuAction(ActionEvent actionEvent) throws IOException
    {
        stage.close();
        PagesController.openPage("Main Menu");
    }

    public void quitAction(ActionEvent actionEvent) throws IOException
    {
        Main.client.getWriter().writeUTF(TaskManager.getExit(Main.client.getUsername()));

        Main.client.getSocket().close();
        Main.client.getWriter().close();
        Main.client.getReader().close();

        System.exit(0);
    }

    public void musicAction(ActionEvent actionEvent)
    {
        String condition = music_condition.substring(music_condition.indexOf("(") + 1, music_condition.indexOf(")"));

        if(condition.equals("On"))
        {
            music_condition = "Music(Off)";
            musicBTN.setText(music_condition);
            Main.music.stop();
            MusicPlayer.kill();
        }
        else
        {
            music_condition = "Music(On)";
            musicBTN.setText(music_condition);

            MusicPlayer.revive();
            MusicPlayer musicPlayer = new MusicPlayer();
            Thread thread = new Thread(musicPlayer);
            thread.start();
        }
    }
}
