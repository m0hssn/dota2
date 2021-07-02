package controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashScreenController implements Initializable
{
    public static boolean isFinished = true;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
    }

    public static void show(AnchorPane pane) throws IOException
    {
        SplashScreenController.isFinished = false;

        AnchorPane anchorPane = FXMLLoader.load(SplashScreenController.class.
                getResource("../pages/SplashScreen.fxml"));

        pane.getChildren().setAll(anchorPane);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(4), anchorPane);

        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(4), anchorPane);

        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);

        fadeIn.play();

        fadeIn.setOnFinished((event ->
        {
            fadeOut.play();
        }));


        fadeOut.setOnFinished((event ->
        {
            try
            {
                AnchorPane anchorPane2 = FXMLLoader.load(SplashScreenController.class.
                        getResource("../pages/Login.fxml"));

                pane.getChildren().setAll(anchorPane2);
            }
            catch (IOException e)
            {
            }

        }));
    }
}
