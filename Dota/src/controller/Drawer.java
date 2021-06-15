package controller;

import Model.Creep;
import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static utills.StaticData.*;

/**
 * The Drawer class is used to show the Creeps and the Heroes on map
 */
public class Drawer extends AnimationTimer {
    private long time;
    private static final long SECOND = 1_000_000_000 /* one billion nano seconds*/;
    private List<Circle> circles;

    /**
     *  This method is going to be called in every frame while the AnimationTimer is active.
     *
     * @param now time in nano seconds
     */
    @Override
    public void handle(long now) {
        if(now - time > SECOND || time == 0){
            time = now;
            try {
                draw();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() throws IOException, InterruptedException {
        if(circles != null){
            map.getChildren().removeAll(circles);
        }

        circles = new ArrayList<>();
        Creep[] creeps = handler.read();

        for (Creep c: creeps) {
            Circle circle = new Circle(0, 0, 1 * SCALE);
            circle.setLayoutX(c.getX() * SCALE);
            circle.setLayoutY(c.getY() * SCALE);
            circle.setSmooth(true);
            circles.add(circle);
            map.getChildren().add(circle);
            if(c.getX() == c.getFinal_x() && c.getFinal_y() == c.getY()){
                FillTransition fillTransition = new FillTransition();
                fillTransition.setShape(circle);
                fillTransition.setDuration(Duration.seconds(1.1));
                fillTransition.setFromValue(new Color(0,0,0,0));
                fillTransition.setToValue(c.getColor());
                fillTransition.play();
                fillTransition.setOnFinished(event -> {
                    fillTransition.getShape().setVisible(false);
                });
            } else {
                circle.setFill(c.getColor());

                TranslateTransition translateTransition = new TranslateTransition();
                translateTransition.setNode(circle);
                translateTransition.setToX((c.getFinal_x() - c.getX()) * SCALE);
                translateTransition.setToY((c.getFinal_y() - c.getY()) * SCALE);
                translateTransition.setDuration(Duration.seconds(1.0001));
                translateTransition.play();
                translateTransition.setOnFinished(event -> {
                    translateTransition.getNode().setVisible(false);
                });
            }
        }
    }
}
