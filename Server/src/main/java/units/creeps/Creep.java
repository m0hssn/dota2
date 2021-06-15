package units.creeps;

import comp.Group;
import javafx.geometry.Point2D;
import units.Lane;
import units.Movable;

public abstract class Creep extends Movable {
    protected boolean alive = true;
    protected double hp;
    protected final Group group;

    public Creep(Point2D point, Lane lane) {
        super(point, lane);
        alive = true;
        if(lane.toString().contains("RED")) {
            group = Group.Red;
        }else {
            group = Group.Green;
        }
    }

    public double getHp() {
        return hp;
    }

    public abstract double getDamage();


    public abstract CreepType getType();


    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public String toString() {
        return getPreviousX() + "," + getPreviousY() + "," + getX() + "," + getY();
    }

    public static boolean intersects(Point2D creep, Point2D unit, CreepType creepType) {
        if(creepType == CreepType.Melee)
            return creep.distance(unit) <= 2;
        else if(creepType == CreepType.Ranged)
            return creep.distance(unit) <= 4;
        else
            return false;
    }
}
