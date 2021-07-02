package units.buildings;

import javafx.geometry.Point2D;
import units.Unit;

public abstract class Building implements Unit {
    protected boolean alive = true;
    protected double hp;
    protected final Point2D point;
    private final int unit;

    protected Building(Point2D point, int unit) {
        this.point = point;
        this.unit = unit;
    }

    public int getUnit() {
        return unit;
    }

    public abstract double getHp();

    @Override
    public Point2D getPoint() {
        return point;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public String toString() {
        return unit + "," + getHp();
    }
}
