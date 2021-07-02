package units.Hero;

import comp.Group;
import javafx.geometry.Point2D;
import org.json.JSONException;
import org.json.JSONObject;
import units.Lane;
import units.Movable;
import units.StaticData;
import units.Unit;


public abstract class Hero implements Unit {
    public static final double[] req = {230, 370, 480, 580, 600, 720, 750, 890, 930, 970, 1010};


    protected final Group group;
    protected Point2D position;
    protected Point2D previous;

    protected boolean alive;
    protected double mana;
    protected double hp;
    protected int level;
    protected double armor;
    protected double damage;
    protected double experience;
    protected double hp_regeneration;
    protected double mana_regeneration;

    protected double HP_MAX;
    protected double MANA_MAX;


    private byte turnDeActive;


    protected Hero(Group group) {
        this.group = group;
        alive = true;
        if(this.group == Group.Green) {
            position = StaticData.GreenAncient;
            previous = StaticData.GreenAncient;
        } else if(this.group == Group.Red) {
            position = StaticData.RedAncient;
            previous = StaticData.RedAncient;
        }
        level = 1;
    }

    public abstract void levelUp();

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public Point2D getPoint() {
        return position;
    }

    @Override
    public void regenerate() {
        if(alive) {
            hp +=hp_regeneration;
            mana += mana_regeneration;
            if(hp > HP_MAX ) {
                hp = HP_MAX;
            }
            if(mana > MANA_MAX) {
                mana = MANA_MAX;
            }
        }
    }

    public Point2D getPrevious() {
        return previous;
    }

    @Override
    public void getHit(double damage) {
        hp -= (damage - armor);
        if(hp <= 0) {
            alive = false;
            hp = 0;
            if(this.group == Group.Green) {
                position = StaticData.GreenAncient;
                previous = StaticData.GreenAncient;
            } else if(this.group == Group.Red) {
                position = StaticData.RedAncient;
                previous = StaticData.RedAncient;
            }
        }
    }


    @Override
    public double getExp() {
        return experience;
    }

    public abstract void turn();

    public double getDamage() {
        return damage;
    }

    public void addToExp(double experience) {
        this.experience += experience;
    }

    public void move(Direction dir) {
        previous = new Point2D(position.getX(), position.getY());
        switch (dir) {
            case UP:

                if(inline(position, StaticData.GreenAncient, StaticData.GreenTop1)) {
                    position = Movable.moveOne(position, StaticData.GreenTop1);
                } else if(inline(position, StaticData.GreenTop1, StaticData.TopLaneTurnPos1)) {
                    position = Movable.moveOne(position, StaticData.TopLaneTurnPos1);
                }else if(inline(position, StaticData.TopLaneTurnPos1, StaticData.TopLaneTurnPos2)) {
                    position = Movable.moveOne(position, StaticData.TopLaneTurnPos2);
                } else if(inline(position, StaticData.BottomLaneTurn, StaticData.RedBottom1)) {
                    position = Movable.moveOne(position, StaticData.RedBottom1);
                } else if(inline(position, StaticData.RedBottom1, StaticData.RedAncient)) {
                    position = Movable.moveOne(position, StaticData.RedAncient);
                }


                break;
            case DOWN:

                if(inline(position, StaticData.TopLaneTurnPos2, StaticData.TopLaneTurnPos1)) {
                    position = Movable.moveOne(position, StaticData.TopLaneTurnPos1);
                } else if(inline(position, StaticData.TopLaneTurnPos1, StaticData.GreenTop1)) {
                    position = Movable.moveOne(position, StaticData.GreenTop1);
                } else if(inline(position, StaticData.GreenTop1, StaticData.GreenAncient)) {
                    position = Movable.moveOne(position, StaticData.GreenAncient);
                } else if(inline(position, StaticData.RedAncient, StaticData.RedBottom1)){
                    position = Movable.moveOne(position, StaticData.RedBottom1);
                } else if (inline(position, StaticData.RedBottom1, StaticData.BottomLaneTurn)){
                    position = Movable.moveOne(position, StaticData.BottomLaneTurn);
                }


                break;
            case RIGHT:

                if(inline(position, StaticData.GreenAncient, StaticData.GreenBottom1)) {
                    position = Movable.moveOne(position, StaticData.GreenBottom1);
                } else if(inline(position, StaticData.GreenBottom1, StaticData.GreenTower_Bottom4)){
                    position = Movable.moveOne(position, StaticData.GreenTower_Bottom4);
                } else if (inline(position, StaticData.GreenTower_Bottom4, StaticData.BottomLaneTurn)) {
                    position = Movable.moveOne(position, StaticData.BottomLaneTurn);
                } else if(inline(position, StaticData.TopLaneTurnPos2, StaticData.RedTop1)) {
                    position = Movable.moveOne(position, StaticData.RedTop1);
                } else if(inline(position, StaticData.RedTop1, StaticData.RedAncient)) {
                    position = Movable.moveOne(position, StaticData.RedAncient);
                }


                break;
            case LEFT:

                if(inline(position, StaticData.BottomLaneTurn, StaticData.GreenTower_Bottom4)) {
                    position = Movable.moveOne(position, StaticData.GreenTower_Bottom4);
                } else if(inline(position, StaticData.GreenTower_Bottom4, StaticData.GreenBottom1)){
                    position = Movable.moveOne(position, StaticData.GreenBottom1);
                } else if(inline(position, StaticData.GreenBottom1, StaticData.GreenAncient)){
                    position = Movable.moveOne(position, StaticData.GreenAncient);
                } else if(inline(position, StaticData.RedAncient, StaticData.RedTop1)) {
                    position = Movable.moveOne(position, StaticData.RedTop1);
                } else if(inline(position, StaticData.RedTop1, StaticData.TopLaneTurnPos2)){
                    position = Movable.moveOne(position, StaticData.TopLaneTurnPos2);
                }


                break;
            case DiagonalUP:
                if (inline(position, StaticData.GreenAncient, StaticData.GreenMiddle1) ) {
                    position = Movable.moveOne(position, StaticData.GreenMiddle1);
                } else if(inline(position, StaticData.GreenMiddle1, StaticData.RedTower_Middle3)) {
                    position = Movable.moveOne(position, StaticData.RedTower_Middle3);
                } else if (inline(position, StaticData.RedTower_Middle3, StaticData.RedTower_Middle2)) {
                    position = Movable.moveOne(position, StaticData.RedTower_Middle2);
                } else if (inline(position, StaticData.RedTower_Middle2, StaticData.RedMiddle1)) {
                    position = Movable.moveOne(position, StaticData.RedMiddle1);
                } else if(inline(position,StaticData.RedMiddle1, StaticData.RedAncient)) {
                    position = Movable.moveOne(position, StaticData.RedAncient);
                }
                break;
            case DiagonalDown:
                if(inline(position, StaticData.RedAncient, StaticData.RedMiddle1)) {
                    position = Movable.moveOne(position, StaticData.RedMiddle1);
                } else if (inline(position, StaticData.RedMiddle1, StaticData.RedTower_Middle2)) {
                    position = Movable.moveOne(position, StaticData.RedTower_Middle2);
                } else if (inline(position, StaticData.RedTower_Middle2, StaticData.RedTower_Middle3)){
                    position = Movable.moveOne(position, StaticData.RedTower_Middle3);
                } else if(inline(position, StaticData.RedTower_Middle3, StaticData.GreenMiddle1)) {
                    position = Movable.moveOne(position, StaticData.GreenMiddle1);
                } else if (inline(position, StaticData.GreenMiddle1, StaticData.GreenAncient)){
                    position = Movable.moveOne(position, StaticData.GreenAncient);
                }
                break;
            case Still:
                break;
        }
    }

    public void revive() {
        if(!alive) {
            turnDeActive ++;
            if(turnDeActive == 2){
                hp = HP_MAX;
                mana = MANA_MAX;
                turnDeActive = 0;
                alive = true;
            }
        }
    }

    public abstract JSONObject getJson() throws JSONException;

    public abstract Power getPower(int i);

    public static boolean inline(Point2D point, Point2D point1, Point2D point2) {
        return Math.abs(point.distance(point1) + point.distance(point2) - point1.distance(point2)) < 0.01 && point.distance(point2) != 0;
    }

    public static boolean intersects(Point2D point, Point2D point2, HeroType type) {
        if(type == HeroType.Knight) {
            return point.distance(point2) <= 2;
        } else if(type == HeroType.Ranger) {
            return point.distance(point2) <= 4;
        } else {
            return false;
        }
    }

}
