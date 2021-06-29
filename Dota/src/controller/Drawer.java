package controller;

import Model.Hero;
import Model.HeroType;
import Model.Unit;
import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.util.Duration;
import utills.StaticData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utills.StaticData.*;

public class Drawer extends AnimationTimer {
    private static final long SECOND = 1_000_000_000 ;//one billion nano seconds
    long time;
    private List<Circle> circles;

    @Override
    public void handle(long now) {
        if(now - time > SECOND || time == 0) {
            time = now;
            try {
                gc.clearRect(0, 0, 200 * SCALE, 200 * SCALE);
                StaticData.handler.turn();
                if(gameOver) {
                    showText(winner + " won");
                    this.stop();
                } else {
                    draw();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showText("disconnected");
            }
        }
    }


    private void draw() {
        if(circles != null){
            map.getChildren().removeAll(circles);
        }
        circles = new ArrayList<>();

        Arrays.stream(StaticData.handler.getCreeps()).forEach(creep -> {
            Circle circle = new Circle(0, 0, 1 * SCALE);
            circle.setLayoutX(creep.getX() * SCALE);
            circle.setLayoutY(creep.getY() * SCALE);
            circles.add(circle);
            map.getChildren().add(circle);
            if (creep.getX() == creep.getFinal_x() && creep.getFinal_y() == creep.getY()) {
                fillTransition(circle, creep.getColor());
            } else {
                circle.setFill(creep.getColor());
                translateTransition(circle, creep);
            }
        });

        for (Hero hero : StaticData.handler.getHeroes()) {
            if(hero.getType() == HeroType.Knight) {
                Circle circle = new Circle(0, 0, 1.5 * SCALE);
                circle.setLayoutX(hero.getX() * SCALE);
                circle.setLayoutY(hero.getY() * SCALE);
                circle.setFill(new Color(1, 0.8, 0.1, 1));
                circles.add(circle);
                map.getChildren().add(circle);
                translateTransition(circle, hero);
            } else if(hero.getType() == HeroType.KnightDisabled) {
                Circle circle = new Circle(0, 0, 1.5 * SCALE);
                circle.setLayoutX(hero.getX() * SCALE);
                circle.setLayoutY(hero.getY() * SCALE);
                circle.setFill(new Color(0.35, 0.36, 0.41, 1));
                circles.add(circle);
                map.getChildren().add(circle);
            } else if(hero.getType() == HeroType.Ranger) {
                Circle circle = new Circle(0, 0, 1.5 * SCALE);
                circle.setLayoutX(hero.getX() * SCALE);
                circle.setLayoutY(hero.getY() * SCALE);
                circle.setFill(new Color(0.25, 0.85, 1, 1));
                circles.add(circle);
                map.getChildren().add(circle);
                translateTransition(circle, hero);

            } else if(hero.getType() == HeroType.RangerDisabled) {
                Circle circle = new Circle(0, 0, 1.5 * SCALE);
                circle.setLayoutX(hero.getX() * SCALE);
                circle.setLayoutY(hero.getY() * SCALE);
                circle.setFill(new Color(0.51, 0.37, 0.58, 1));
                circles.add(circle);
                map.getChildren().add(circle);
            }
        }
    }

    private static void translateTransition(Node node, Unit unit) {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setNode(node);
        translateTransition.setToX((unit.getFinal_x() - unit.getX()) * SCALE);
        translateTransition.setToY((unit.getFinal_y() - unit.getY()) * SCALE);
        translateTransition.setDuration(Duration.seconds(1));
        translateTransition.setOnFinished(event -> translateTransition.getNode().setVisible(false));
        translateTransition.play();
    }

    private static void fillTransition(Shape shape, Color color) {
        FillTransition fillTransition = new FillTransition();
        fillTransition.setShape(shape);
        fillTransition.setDuration(Duration.seconds(1));
        fillTransition.setFromValue(new Color(0, 0, 0, 0));
        fillTransition.setToValue(color);
        fillTransition.setOnFinished(event -> fillTransition.getShape().setVisible(false));
        fillTransition.play();
    }

    private void showText(String s) {
        label.setText(s);

        power1.setDisable(true);
        power2.setDisable(true);
        power3.setDisable(true);
        levelup.setDisable(true);
        hpHero.setDisable(true);
        manaHero.setDisable(true);

        gc.setFont(new Font(10 * SCALE));
        gc.setFill(Color.DARKRED);
        gc.fillRect(0,82 * SCALE, 200 * SCALE, 30* SCALE);
        gc.setFill(Color.WHITE);
        gc.fillText(s, 80 * SCALE, 100 * SCALE);
    }
}
