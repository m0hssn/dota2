package main;

import controllers.PagesController;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class Main extends Application
{
    public static Client client;
    public static MediaPlayer music;

    public static void main(String[] args) throws IOException
    {
        play();

        client = new Client();
        client.connect();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        PagesController.openPage("Login");
    }

    // play music
    private static void play()
    {
        String bip = "gameSong.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        music = new MediaPlayer(hit);

        MusicPlayer musicPlayer = new MusicPlayer();
        Thread thread = new Thread(musicPlayer);
        thread.start();
    }
}
