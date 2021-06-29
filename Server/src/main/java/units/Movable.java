package units;


import javafx.geometry.Point2D;

import static units.StaticData.*;

/**
 * The class Movable is used for movements of movable units such as creeps and heroes
 */
public abstract class Movable implements Unit {
    /**
     * A 2DPoint which represents where the unit is
     */
    private Point2D point;
    /**
     * A 2DPoint which saves the previous position of the unit
     */
    private Point2D previousPos;
    /**
     * The lane which the unit is moving on
     */
    protected Lane lane;

    /**
     * For units to reach their destination from their beginning each creep has to make 4 turns
     */
    private boolean first_turn = false;
    private boolean second_turn = false;
    private boolean third_turn = false;
    private boolean fourth_turn = false;

    /**
     * Creates a new instance of Movable
     *
     * @param point A 2DPoint which is where the unit begins its movement form
     * @param lane The lane which the unit is moving on
     */
    public Movable(Point2D point, Lane lane) {
        this.point = point;
        this.previousPos = point;
        this.lane = lane;
    }

    /**
     * @return the current position of the unit in x axis
     */
    public double getX() {
        return point.getX();
    }

    /**
     *
     * @return the current position of the unit in y axis
     */
    public double getY() {
        return point.getY();
    }

    /**
     * @return the previous position of the unit in x axis
     */
    public double getPreviousX() {
        return previousPos.getX();
    }

    /**
     * @return the previous position of the unit in y axis
     */
    public double getPreviousY() {
        return previousPos.getY();
    }

    /**
     *
     * @return the current position in a 2DPoint
     */
    @Override
    public Point2D getPoint() {
        return point;
    }

    /**
     * A method used to move the unit 1.5 Pixels on the board based upon the path taken
     */
    public void moveOne() {
        previousPos = new Point2D(point.getX(), point.getY());
        switch (lane) {
            case TOP_GREEN:
                moveOne(TopLaneTurnPos1, TopLaneTurnPos2, RedTop1, RedAncient);
                break;
            case TOP_RED:
                moveOne(TopLaneTurnPos2, TopLaneTurnPos1, GreenTop1, GreenAncient);
                break;
            case MIDDLE_GREEN:
                moveOne(RedTower_Middle3, RedTower_Middle2, RedMiddle1, RedAncient);
                break;
            case MIDDLE_RED:
                moveOne(RedTower_Middle2, RedTower_Middle3, GreenMiddle1, GreenAncient);
                break;
            case LOW_GREEN:
                moveOne(GreenTower_Bottom4, BottomLaneTurn, RedBottom1, RedAncient);
                break;
            case LOW_RED:
                moveOne(BottomLaneTurn, GreenTower_Bottom4, GreenBottom1, GreenAncient);
                break;
        }
    }

    /**
     * A method used to move the unit 1.5 Pixels closer to the points given
     *
     *
     * @param first A 2DPoint which contains the data where the first turn is taken
     * @param second A 2DPoint which contains the data where the second turn is taken
     * @param third A 2DPoint which contains the data where the third turn is taken
     * @param last A 2DPoint which contains the data where the fourth and final turn is taken
     */
    protected void moveOne(Point2D first, Point2D second, Point2D third, Point2D last) {
        if(!first_turn) {
            point = moveOne(point, first);
            first_turn = point.distance(first) == 0;
        }else if(!second_turn) {
            point = moveOne(point, second);
            second_turn = point.distance(second) == 0;
        }else if(!third_turn) {
            point = moveOne(point, third);
            third_turn = point.distance(third) == 0;
        } else if(!fourth_turn) {
            point = moveOne(point, last);
            fourth_turn = point.distance(last) == 0;
        }
    }

    /**
     *
     * @param point The current position of the unit
     * @param destination The point where the unit must get close to
     * @return The position of the unit moved 1.5 pixels closer to the destination
     */
    public static Point2D moveOne(Point2D point,Point2D destination) {
        Point2D vector = destination.subtract(point).normalize();
        vector = vector.multiply(1.5);
        point = point.add(vector);
        if(point.distance(destination) < 1) {
            point = destination;
        }
        return point;
    }

    public Lane getLane() {
        return lane;
    }

    @Override
    public String toString() {
        return getPreviousX() + "," + getPreviousY() + "," + getX() + "," + getY();
    }

}
