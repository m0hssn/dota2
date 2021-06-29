package units.Hero;

import javafx.geometry.Point2D;

public abstract class Power {

    protected int level_;
    protected int availableIn;
    protected final int effects;

    protected boolean active;
    protected Power(int effects) {
        this.effects = effects;
        level_ = 1;
        active = false;
    }

    public void levelUp() {
        if(level_ < 4)
            level_++;
    }

    public abstract boolean inRange( Point2D point);

    public abstract boolean isAvailable();

    public abstract void turn();

    public abstract int getMana_cost();

    public void activate() {
        if(this.isAvailable() && !this.active) {
            active = true;
        }
    }

    public void deActivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public abstract double getDamage();

    public int getEffects() {
        return effects;
    }
}
