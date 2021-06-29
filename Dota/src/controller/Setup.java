package controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;

import static utills.StaticData.SCALE;

/**
 * This class is used to make javafx properties fit your screen
 */
public class Setup {

    private static void setupCircle(Circle c) {
        c.setRadius(c.getRadius() * SCALE);
        c.setLayoutX(c.getLayoutX() * SCALE);
        c.setLayoutY(c.getLayoutY() * SCALE);
    }

    private static void setupRect(Rectangle r) {
        r.setWidth(r.getWidth() * SCALE);
        r.setHeight(r.getHeight() * SCALE);
        r.setX(r.getX() * SCALE);
        r.setY(r.getY() * SCALE);
    }

    private static void setupLine(Line l) {
        l.setStartX(l.getStartX() * SCALE);
        l.setEndX(l.getEndX() * SCALE);
        l.setStartY(l.getStartY() * SCALE);
        l.setEndY(l.getEndY() * SCALE);
    }

    public static void setupShape(Shape shape) {
        if (shape instanceof Circle) {
            setupCircle((Circle) shape);
        } else if (shape instanceof Rectangle) {
            setupRect((Rectangle) shape);
        } else if (shape instanceof Line) {
            setupLine((Line) shape);
        }
    }

    public static void setupAnchor(AnchorPane a) {
        a.setPrefHeight(a.getPrefHeight() * SCALE);
        a.setPrefWidth(a.getPrefWidth() * SCALE);
    }

    public static void setImage(ImageView image) {
        image.setFitHeight(image.getFitHeight() * SCALE);
        image.setFitWidth(image.getFitWidth() * SCALE);
    }

    public static void setupCanvas(Canvas canvas) {
        canvas.setHeight(canvas.getHeight() * SCALE);
        canvas.setWidth(canvas.getWidth() * SCALE);
    }

    public static void setupButton(ButtonBase button) {
        button.setLayoutX(button.getLayoutX() * SCALE);
        button.setLayoutY(button.getLayoutY() * SCALE);

        button.setScaleX(SCALE);
        button.setScaleY(SCALE);
        button.setScaleZ(SCALE);
    }

    public static void setupBar(ProgressBar bar) {
        bar.setLayoutX(bar.getLayoutX() * SCALE);
        bar.setLayoutY(bar.getLayoutY() * SCALE);

        bar.setPrefHeight(bar.getPrefHeight() * SCALE);
        bar.setPrefWidth(bar.getPrefWidth() * SCALE);
    }

    public static void setupLabel(Label label) {
        label.setLayoutX(label.getLayoutX() * SCALE);
        label.setLayoutY(label.getLayoutY() * SCALE);

        Font font = new Font(4 * SCALE);
        label.setFont(font);
        label.setPrefWidth(50 * SCALE);
        label.setPrefHeight(20 * SCALE);
//        label.setScaleX(SCALE);
//        label.setScaleY(SCALE);
//        label.setScaleZ(SCALE);
    }
}