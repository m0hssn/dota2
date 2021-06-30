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
    boolean isAlive();
    /**
     *
     * @return current position of unit
     */
    Point2D getPoint();
    /**
     * @param damage the damage received
     */
    void getHit(double damage);
    /**
     * the hp and mana (if the unit has hp_regeneration and mana_regeneration rates)
     */
    void regenerate();
    /**
     *
     * @return the amount of experience added to the hero which killed this unit
     */
    double getExp();
}
