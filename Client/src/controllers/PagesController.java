package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class PagesController
{
    private static Stage stage;

    public static void closePage(ActionEvent actionEvent)
    {
        Stage newStage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        newStage.close();
    }

    public static void openPage(String name) throws IOException
    {
        stage = new Stage();
        Parent root = FXMLLoader.load(PagesController.class.getResource("../pages/" + name + ".fxml"));

        stage.setScene(new Scene(root));

        stage.initStyle(StageStyle.TRANSPARENT);
        MouseController.handle(root, stage);

        stage.show();
    }

    public static Stage getStage()
    {
        return stage;
    }
}