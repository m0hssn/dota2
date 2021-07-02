package Model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

import java.util.List;

/**
 * The Team class is used to save all the shapes(buildings) on map which are related to each other as a whole
 */
public class Team {
    final List<Shape> buildings;
    final Color color;

    public Team(List<Shape> buildings, Color color){
        this.buildings = buildings;
        this.color = color;
    }

    public void receiveDamage(Building[] builds) {
        for (Building b: builds) {
            Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), b.getHp());
            buildings.get(b.getNumber()).setFill(c);
        }
    }

}
