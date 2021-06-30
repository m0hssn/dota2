package units.creeps;

import comp.Group;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import units.StaticData;
import units.Lane;

public class Ranged extends Creep {

    public final static double HP_MAX = 300;
    public final static double EXPERIENCE = 70;
    public final static double HP_REGENERATION = 2;
    public final static double DAMAGE = 23;
    public static final Color GREEN_RANGED = new Color(0.5188679245283019,
            0.1245283018867925,
            0.8564150943396226, 1);
    public static final Color RED_RANGED = new Color(0.7188679245283019,
            0.5245283018867925,
            0.1564150943396226, 1);

    public Ranged(Point2D point, Lane lane, boolean barrack) {
        super(point, lane, barrack);
        if(barrack) {
            hp = HP_MAX;
        } else {
            hp = HP_MAX - 20;
        }
    }

    @Override
    public double getDamage() {
        if(barrack) {
            return DAMAGE;
        } else {
            return DAMAGE - 3;
        }
    }

    @Override
    public void getHit(double damage) {
        hp -= (damage);
        alive = hp > 0;
    }

    @Override
    public void regenerate() {
        hp += HP_REGENERATION;
        if(barrack){
            if(hp > HP_MAX) {
                hp = HP_MAX;
            }
        } else {
            if(hp > HP_MAX - 20) {
                hp = HP_MAX - 20;
            }
        }
    }

    @Override
    public double getExp() {
        return EXPERIENCE;
    }

    @Override
    public CreepType getType() {
        return CreepType.Ranged;
    }

    public static Point2D getPoint(Lane lane, int i) {
        double x = 0;
        double y = 0;
        switch (lane) {
            case TOP_GREEN:
                x = StaticData.GreenTop2.getX();
                y = StaticData.GreenTop2.getY() + 2 * i;
                break;
            case MIDDLE_GREEN:
                x = StaticData.GreenMiddle2.getX() + i;
                y = StaticData.GreenMiddle2.getY() + i;
                break;
            case LOW_GREEN:
                x = StaticData.GreenBottom2.getX() + 2 * i;
                y = StaticData.GreenBottom2.getY();
                break;
            case TOP_RED:
                x = StaticData.RedTop2.getX() + 2 * i;
                y = StaticData.RedTop2.getY();
                break;
            case MIDDLE_RED:
                x = StaticData.RedMiddle2.getX() + i;
                y = StaticData.RedMiddle2.getY() + i;
                break;
            case LOW_RED:
                x = StaticData.RedBottom2.getX();
                y = StaticData.RedBottom2.getY() + 2 * i;
                break;
        }
        return new Point2D(x, y);
    }

    @Override
    public String toString() {
        if(group == Group.Green){
            return super.toString() + "," + GREEN_RANGED;
        }
        return super.toString() + "," + RED_RANGED;
    }
}
