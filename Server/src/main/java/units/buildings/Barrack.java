package units.buildings;

import javafx.geometry.Point2D;
import units.Lane;
import units.creeps.Creep;
import units.creeps.CreepType;
import units.creeps.Melee;
import units.creeps.Ranged;

import java.util.ArrayList;
import java.util.List;

public class Barrack extends Building{
    private final CreepType creepType;
    private final Lane lane;
    public final static double MELEE_HP_MAX = 2200;
    public final static double RANGED_HP_MAX = 1500;

    public final static double EXPERIENCE = 110;

    public final static double MELEE_ARMOUR = 15;
    public final static double RANGED_ARMOUR = 9;

    public final static double MELEE_HP_REGENERATION = 5;

    public Barrack(Point2D point, int unit, CreepType creepType, Lane lane) {
        super(point, unit);
        this.creepType = creepType;
        this.lane = lane;
        if(creepType == CreepType.Melee){
            hp = MELEE_HP_MAX;
        }else {
            hp = RANGED_HP_MAX;
        }
    }

    public List<Creep> releaseCreeps() {
        List<Creep> creeps = new ArrayList<>();
        if(creepType == CreepType.Melee) {
            for (int i = 0; i < 5; i++) {
                creeps.add(new Melee(Melee.getPoint(lane, i), lane, alive));
            }
        } else {
            creeps.add(new Ranged(Ranged.getPoint(lane, 0), lane, alive));
        }
        return creeps;
    }

    public Lane getLane() {
        return lane;
    }

    @Override
    public double getExp() {
        return EXPERIENCE;
    }

    @Override
    public void getHit(double damage) {
        if(creepType == CreepType.Melee){
            hp -= (damage - MELEE_ARMOUR);
        }else {
            hp -= (damage - RANGED_ARMOUR);
        }
        alive = hp > 0;
        if(!alive) {
            hp = 0;
        }
    }

    @Override
    public void regenerate() {
        if(creepType == CreepType.Melee && alive) {
            hp += MELEE_HP_REGENERATION;
            if(hp > MELEE_HP_MAX)
                hp = MELEE_HP_MAX;
        }
    }

    @Override
    public double getHp() {
        if(creepType == CreepType.Melee) {
            return hp / MELEE_HP_MAX;
        }else {
            return hp / RANGED_HP_MAX;
        }
    }
}
