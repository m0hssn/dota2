package units;

import javafx.geometry.Point2D;

/**
 * An interface used for all the units in the game
 */
public interface Unit {
    /**
     *
     * @return whether the unit is alive or not
     */
    public boolean isAlive();
    /**
     *
     * @return current position of unit
     */
    public Point2D getPoint();
    /**
     * @param damage the damage received
     */
    public void getHit(double damage);
    /**
     * the hp and mana (if the unit has hp_regeneration and mana_regeneration rates)
     */
    public void regenerate();
    /**
     *
     * @return the amount of experience added to the hero which killed this unit
     */
    public double getExp();
}
