package controller;

import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * The MouseController class is used to move the Stage on the screen when
 * dragged and dropped
 */
public class MouseController {
    private static double x = 0;
    private static double y = 0;

    public static void handle(Parent root, Stage stage) {
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
    }
}

