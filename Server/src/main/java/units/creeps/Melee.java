package units.creeps;

import comp.Group;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import units.StaticData;
import units.Lane;

public class Melee extends Creep {

    public final static double HP_MAX = 550;
    public final static double EXPERIENCE = 60;
    public final static double ARMOUR = 2;
    public final static double HP_REGENERATION = 0.5;
    public final static double DAMAGE = 21;
    private final static Color GREEN_MELEE = new Color(0.2188679245283019,
            0.1245283018867925,
            0.8264150943396226, 1);

    public static final Color RED_MELEE = new Color(0.7188679245283019,
            0.3245283018867925,
            0.1264150943396226, 1);

    public Melee(Point2D point, Lane lane, boolean barrack) {
        super(point, lane);
        if(barrack) {
            hp = HP_MAX;
        } else {
            hp = HP_MAX - 20;
        }
    }

    @Override
    public double getDamage() {
        return DAMAGE;
    }

    @Override
    public void getHit(double damage) {
        hp -= (damage - ARMOUR);
        alive = hp > 0;
    }

    @Override
    public void regenerate() {
        hp += HP_REGENERATION;
        if(hp > HP_MAX) {
            hp = HP_MAX;
        }
    }


    @Override
    public double getExp() {
        return EXPERIENCE;
    }

    @Override
    public CreepType getType() {
        return CreepType.Melee;
    }

    public static Point2D getPoint(Lane lane, int i) {
        double x = 0;
        double y = 0;
        switch (lane) {
            case TOP_GREEN:
                x = StaticData.GreenTop1.getX() ;
                y = StaticData.GreenTop1.getY() + 2 * i;
                break;
            case MIDDLE_GREEN:
                Point2D v = StaticData.RedTower_Middle3.subtract(StaticData.GreenMiddle1).normalize();
                x = StaticData.GreenMiddle1.getX() + 2 * i * v.getX();
                y = StaticData.GreenMiddle1.getY() + 2 * i * v.getY();
                break;
            case LOW_GREEN:
                x = StaticData.GreenBottom1.getX() + 2 * i;
                y = StaticData.GreenBottom1.getY();
                break;
            case TOP_RED:
                x = StaticData.RedTop1.getX() + 2 * i;
                y = StaticData.RedTop1.getY();
                break;
            case MIDDLE_RED:
                Point2D ve = StaticData.RedMiddle1.subtract(StaticData.RedTower_Middle2).normalize();
                x = StaticData.RedMiddle1.getX() + 2 * i * ve.getX();
                y = StaticData.RedMiddle1.getY() + 2 * i * ve.getY();
                break;
            case LOW_RED:
                x = StaticData.RedBottom1.getX();
                y = StaticData.RedBottom1.getY() + 2 * i;
                break;
        }
        return new Point2D(x, y);
    }

    @Override
    public String toString() {
        if (group == Group.Green) {
            return super.toString() + "," + GREEN_MELEE;
        }
        return super.toString() + "," + RED_MELEE;
    }
}
