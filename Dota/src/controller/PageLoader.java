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
import utills.StaticData;

import java.io.IOException;
import java.util.Objects;

public class PageLoader {
    public static void closePage(ActionEvent actionEvent) {
        Stage newStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        newStage.close();
    }

    public static void openPage(String name) throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Objects.requireNonNull(PageLoader.class.getResource("../view/" + name + ".fxml")));
        Scene scene = new Scene(root);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, key->{
            KeyCode keyCode = key.getCode();
            try{
                switch (keyCode){
                    case W:
                        StaticData.handler.write(Move.UP);
                        break;
                    case S:
                        StaticData.handler.write(Move.DOWN);
                        break;
                    case A:
                        StaticData.handler.write(Move.LEFT);
                        break;
                    case D:
                        StaticData.handler.write(Move.RIGHT);
                        break;
                    case E:
                        StaticData.handler.write(Move.DiagonalUP);
                        break;
                    case Z:
                        StaticData.handler.write(Move.DiagonalDown);
                        break;
                }
            }catch (IOException e){
                e.printStackTrace();
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
