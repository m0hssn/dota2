package units.buildings;

import javafx.geometry.Point2D;

public class Tower extends Building {
    public static final double MAX_HP = 1500;
    public final static double ARMOUR = 10;
    public static final double DAMAGE = 90;
    public final static double EXPERIENCE = 100;

    public Tower(Point2D point, int unit) {
        super(point, unit);
        hp = MAX_HP;
    }

    @Override
    public void getHit(double damage) {
        hp -= (damage - ARMOUR);
        alive = hp > 0;
    }

    public double getDamage() {
        return DAMAGE;
    }

    @Override
    public void regenerate() {
    }

    @Override
    public double getExp() {
        return EXPERIENCE;
    }

    @Override
    public double getHp() {
        return hp / MAX_HP;
    }

    public static boolean intersects (Point2D tower, Point2D unit){
        return tower.distance(unit) <= 6;
    }
}
