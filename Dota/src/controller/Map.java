package controller;

import Model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
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
    ImageView background;
    @FXML
    ToggleButton power1, power2, power3;

    @FXML
    Label label, nameTag;

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

    @FXML
    ProgressBar hp, mana;

    @FXML
    Button levelup;

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
        setImage(background);
        setupAnchor(map);
        setupCanvas(canvas);
        setupButton(power1);
        setupButton(power2);
        setupButton(power3);
        setupButton(levelup);
        setupBar(hp);
        setupBar(mana);
        setupLabel(label);

        greenTowers.forEach(Setup::setupShape);
        redTowers.forEach(Setup::setupShape);

        Red = new Team(redTowers, Color.RED);
        Green = new Team(greenTowers, Color.GREEN);

        hp.setProgress(1);
        hp.setStyle("-fx-accent: green");
        mana.setProgress(1);

        label.setStyle("-fx-background-color: lightgrey;-fx-background-radius: 2;-fx-font-weight: bold");

        nameTag.setStyle("-fx-background: transparent");
        label.setText("experience = 0/230\nlevel = 1");

        nameTag.setText("Green:player1, Red:player2");

        StaticData.map = map;
        StaticData.gc = canvas.getGraphicsContext2D();
        StaticData.hpHero = hp;
        StaticData.manaHero = mana;
        StaticData.power1 = power1;
        StaticData.power2 = power2;
        StaticData.power3 = power3;
        StaticData.label = label;
        StaticData.levelup = levelup;
        StaticData.nameTag = nameTag;

        StaticData.gameOver = false;

    }

    public void levelUp(ActionEvent event) {
        StaticData.management.levelUp();
    }

    public void power(ActionEvent event) {
        ToggleButton t = (ToggleButton) event.getSource();
        StaticData.management.setKey(t);
    }
}
