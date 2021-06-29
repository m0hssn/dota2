package controller;

import Model.Move;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utills.HeroManagement;

import java.io.IOException;
import java.util.Objects;

import static utills.StaticData.management;

public class PageLoader {

    public static void closePage(ActionEvent actionEvent) {
        Stage newStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newStage.close();
    }

    public static void openPage(String name) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(PageLoader.class.getResource("../view/" + name + ".fxml")));
        Scene scene = new Scene(root);

        management = new HeroManagement();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key-> {
            KeyCode keyCode = key.getCode();

            switch (keyCode) {
                case W:
                    management.move(Move.UP);
                    break;
                case S:
                    management.move(Move.DOWN);
                    break;
                case A:
                    management.move(Move.LEFT);
                    break;
                case D:
                    management.move(Move.RIGHT);
                    break;
                case E:
                    management.move(Move.DiagonalUP);
                    break;
                case Z:
                    management.move(Move.DiagonalDown);
                    break;
                case Q:
                    management.move(Move.Still);
                    break;
            }
        });
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        MouseController.handle(root, stage);
        stage.setAlwaysOnTop(true);
        stage.getIcons().add(new Image("Icon.png"));
        stage.setTitle("Dota 2");
        stage.show();
    }
}
