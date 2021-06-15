package controller;

import Model.Team;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import utills.StaticData;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static controller.Setup.*;

/**
 * The Map class is used to Initialize the Map.fxml instances
 */
public class Map implements Initializable {
    @FXML
    AnchorPane map;
    @FXML
    ImageView minimap;
    @FXML
    Canvas canvas;
    //Green team Buildings
    public static Team Green;

    @FXML
    Rectangle BaseGreen,
            GreenMiddle1, GreenMiddle2,
            GreenBottom1, GreenBottom2,
            GreenTop1, GreenTop2;
    @FXML
    Circle AncientGreen,
            GreenTower_Bottom1, GreenTower_Bottom2, GreenTower_Bottom3, GreenTower_Bottom4,
            GreenTower_Top1, GreenTower_Top2, GreenTower_Top3, GreenTower_Top4,
            GreenTower_Middle1, GreenTower_Middle2, GreenTower_Middle3;

    //Red team Buildings
    public static Team Red;

    @FXML
    Rectangle BaseRed,
            RedMiddle1, RedMiddle2,
            RedBottom1, RedBottom2,
            RedTop1, RedTop2;
    @FXML
    Circle AncientRed,
            RedTower_Top1, RedTower_Top2, RedTower_Top3, RedTower_Top4,
            RedTower_Bottom1, RedTower_Bottom2, RedTower_Bottom3, RedTower_Bottom4,
            RedTower_Middle1, RedTower_Middle2, RedTower_Middle3;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Shape> greenTowers =
                Arrays.asList(AncientGreen,
                        GreenTower_Bottom1, GreenTower_Bottom2, GreenTower_Bottom3, GreenTower_Bottom4,
                        GreenTower_Top1, GreenTower_Top2, GreenTower_Top3, GreenTower_Top4,
                        GreenTower_Middle1, GreenTower_Middle2, GreenTower_Middle3,
                        GreenMiddle1, GreenMiddle2,
                        GreenTop1, GreenTop2,
                        GreenBottom1, GreenBottom2,
                        BaseGreen);
        List<Shape> redTowers =
                Arrays.asList(AncientRed,
                        RedTower_Bottom1, RedTower_Bottom2, RedTower_Bottom3, RedTower_Bottom4,
                        RedTower_Top1, RedTower_Top2, RedTower_Top3, RedTower_Top4,
                        RedTower_Middle1, RedTower_Middle2, RedTower_Middle3,
                        RedMiddle1, RedMiddle2,
                        RedTop1, RedTop2,
                        RedBottom1, RedBottom2,
                        BaseRed);
        setImage(minimap);
        setupAnchor(map);
        setupCanvas(canvas);
        greenTowers.forEach(Setup::setupShape);
        redTowers.forEach(Setup::setupShape);
        StaticData.map = map;
        StaticData.gc = canvas.getGraphicsContext2D();
        Red = new Team(redTowers, Color.RED);
        Green = new Team(greenTowers, Color.GREEN);
    }
}
