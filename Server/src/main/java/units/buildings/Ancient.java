package units.buildings;

import javafx.geometry.Point2D;

public class Ancient extends Building{
    public static final double MAX_HP = 4500;
    public final static double ARMOUR = 13;
    public final static double HP_REGENERATION = 12;

    public Ancient(Point2D point, int unit) {
        super(point, unit);
        hp = MAX_HP;
    }

    @Override
    public void getHit(double damage) {
        hp -= (damage - ARMOUR);
        alive = hp > 0 ;
        if(!alive) {
            hp = 0;
        }
    }

    @Override
    public void regenerate() {
        hp += HP_REGENERATION;
        if(hp > MAX_HP)
            hp = MAX_HP;
    }

    @Override
    public double getExp() {
        return 0;
    }

    @Override
    public double getHp() {
        return hp/MAX_HP;
    }
}
