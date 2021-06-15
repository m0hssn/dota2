package comp;


import org.json.JSONObject;
import units.Lane;
import units.Unit;
import units.buildings.Ancient;
import units.buildings.Barrack;
import units.buildings.Building;
import units.buildings.Tower;
import units.creeps.Creep;
import units.creeps.CreepType;

import java.util.*;

import static units.StaticData.*;

public class Team {
    private final Group group;

    private final Ancient ancient;

    private final List<Barrack> barracks = new ArrayList<>();

    private final List<Tower> topTowers = new ArrayList<>();
    private final List<Tower> midTowers = new ArrayList<>();
    private final List<Tower> lowTowers = new ArrayList<>();

    private final List<Unit> topRow = new ArrayList<>();
    private final List<Unit> midRow = new ArrayList<>();
    private final List<Unit> lowRow = new ArrayList<>();

    private final List<Creep> topCreeps = new ArrayList<>();
    private final List<Creep> midCreeps = new ArrayList<>();
    private final List<Creep> lowCreeps = new ArrayList<>();

    private final Random random = new Random();


    public Team(Group group) {
        this.group = group;

        List<Barrack> topBarrack = new ArrayList<>();
        List<Barrack> midBarrack = new ArrayList<>();
        List<Barrack> lowBarrack = new ArrayList<>();

        if(group == Group.Green) {
            ancient = new Ancient(GreenAncient, 0);

            Collections.addAll(lowTowers,
                    new Tower(GreenTower_Bottom1, 1),
                    new Tower(GreenTower_Bottom2, 2),
                    new Tower(GreenTower_Bottom3, 3),
                    new Tower(GreenTower_Bottom4, 4));

            Collections.addAll(topTowers,
                    new Tower(GreenTower_Top1, 5),
                    new Tower(GreenTower_Top2, 6),
                    new Tower(GreenTower_Top3, 7),
                    new Tower(GreenTower_Top4, 8));

            Collections.addAll(midTowers,
                    new Tower(GreenTower_Middle1, 9),
                    new Tower(GreenTower_Middle2, 10),
                    new Tower(GreenTower_Middle3, 11));

            Collections.addAll(midBarrack,
                    new Barrack(GreenMiddle1, 12, CreepType.Melee, Lane.MIDDLE_GREEN),
                    new Barrack(GreenMiddle2, 13, CreepType.Ranged, Lane.MIDDLE_GREEN));

            Collections.addAll(topBarrack,
                    new Barrack(GreenTop1, 14, CreepType.Melee, Lane.TOP_GREEN),
                    new Barrack(GreenTop2, 15, CreepType.Ranged, Lane.TOP_GREEN));

            Collections.addAll(lowBarrack,
                    new Barrack(GreenBottom1, 16, CreepType.Melee, Lane.LOW_GREEN),
                    new Barrack(GreenBottom2, 17, CreepType.Ranged, Lane.LOW_GREEN));
        } else {
            ancient = new Ancient(RedAncient, 0);


            Collections.addAll(lowTowers,
                    new Tower(RedTower_Bottom1, 1),
                    new Tower(RedTower_Bottom2, 2),
                    new Tower(RedTower_Bottom3, 3),
                    new Tower(RedTower_Bottom4, 4));

            Collections.addAll(topTowers,
                    new Tower(RedTower_Top1, 5),
                    new Tower(RedTower_Top2, 6),
                    new Tower(RedTower_Top3, 7),
                    new Tower(RedTower_Top4, 8));

            Collections.addAll(midTowers,
                    new Tower(RedTower_Middle1, 9),
                    new Tower(RedTower_Middle2, 10),
                    new Tower(RedTower_Middle3, 11));

            Collections.addAll(midBarrack,
                    new Barrack(RedMiddle1, 12, CreepType.Melee, Lane.MIDDLE_RED),
                    new Barrack(RedMiddle2, 13, CreepType.Ranged, Lane.MIDDLE_RED));

            Collections.addAll(topBarrack,
                    new Barrack(RedTop1, 14, CreepType.Melee, Lane.TOP_RED),
                    new Barrack(RedTop2, 15, CreepType.Ranged, Lane.TOP_RED));

            Collections.addAll(lowBarrack,
                    new Barrack(RedBottom1, 16, CreepType.Melee, Lane.LOW_RED),
                    new Barrack(RedBottom2, 17, CreepType.Ranged, Lane.LOW_RED));
        }

        topRow.add(ancient);
        topRow.addAll(topTowers);
        topRow.addAll(topBarrack);

        midRow.add(ancient);
        midRow.addAll(midTowers);
        midRow.addAll(midBarrack);

        lowRow.add(ancient);
        lowRow.addAll(lowTowers);
        lowRow.addAll(lowBarrack);

        barracks.addAll(topBarrack);
        barracks.addAll(lowBarrack);
        barracks.addAll(midBarrack);

    }


    public void releaseCreeps() {
        barracks.forEach(barrack -> {
            List<Creep> creeps = barrack.releaseCreeps();
            Lane lane = barrack.getLane();
            if (lane.toString().contains("TOP")) {
                topCreeps.addAll(creeps);
                topRow.addAll(creeps);
            } else if(lane.toString().contains("LOW")) {
                lowCreeps.addAll(creeps);
                lowRow.addAll(creeps);
            } else {
                midCreeps.addAll(creeps);
                midRow.addAll(creeps);
            }
        });
    }

    public void removeDeadAndRegenerate() {
        removeDead(topRow, topCreeps, topTowers);
        removeDead(midRow, midCreeps, midTowers);
        removeDead(lowRow, lowCreeps, lowTowers);
        regenerate();
    }

    public boolean lost() {
        return !ancient.isAlive();
    }

    private void removeDead(List<Unit> units, List<Creep> creeps, List<Tower> towers) {
        List<Unit> dead = new ArrayList<>();

        units.forEach(unit -> {
            if(!unit.isAlive()) {
                dead.add(unit);
            }
        });

        units.removeAll(dead);
        creeps.removeAll(dead);
        towers.removeAll(dead);
    }

    public void turn(Team other) {
        move(topCreeps, other.topRow);
        move(midCreeps, other.midRow);
        move(lowCreeps, other.lowRow);

        shoot(topTowers, other.topRow);
        shoot(midTowers, other.midRow);
        shoot(lowTowers, other.lowRow);
    }

    private void regenerate() {
        lowRow.forEach(Unit::regenerate);
        midRow.forEach(Unit::regenerate);
        topRow.forEach(Unit::regenerate);
    }

    private void move(List<Creep> creeps, List<Unit> row) {
        creeps.forEach(creep -> {
            List<Unit> units = new ArrayList<>();

            row.forEach(unit -> {
                if(Creep.intersects(creep.getPoint(), unit.getPoint(), creep.getType())) {
                    units.add(unit);
                }
            });

            if(units.size() != 0) {
                units.get(random.nextInt(units.size())).getHit(creep.getDamage());
            } else {
                creep.moveOne();
            }

        });
    }

    public void shoot(List<Tower> towers, List<Unit> row) {
        towers.forEach(tower -> {
            List<Unit> units = new ArrayList<>();

            row.forEach(unit -> {
                if(Tower.intersects(tower.getPoint(), unit.getPoint())) {
                    units.add(unit);
                }
            });

            if(units.size() != 0) {
                units.get(random.nextInt(units.size())).getHit(tower.getDamage());
            }
        });
    }

    public JSONObject buildingS() {
        final Int i = new Int();
        JSONObject object = new JSONObject();

        topRow.forEach(unit -> {
            if(unit instanceof Building) {
                object.put("b" + i, unit.toString());
                i.add();
            }
        });

        midRow.forEach(unit -> {
            if(unit instanceof Building) {
                object.put("b" + i, unit.toString());
                i.add();
            }
        });

        lowRow.forEach(unit -> {
            if(unit instanceof Building) {
                object.put("b" + i, unit.toString());
                i.add();
            }
        });

        object.put("number", i);
        return object;
    }

    public static JSONObject creeps(Team green, Team red) {
        Int i = new Int();
        JSONObject object = new JSONObject();

        putCreeps(green.topCreeps, object, i);
        putCreeps(green.midCreeps, object, i);
        putCreeps(green.lowCreeps, object, i);


        putCreeps(red.topCreeps, object, i);
        putCreeps(red.midCreeps, object, i);
        putCreeps(red.lowCreeps, object, i);

        object.put("number", i);

        return object;
    }

    private static void putCreeps(List<Creep> creeps, JSONObject object, Int i) {
        creeps.forEach(creep -> {
            object.put("creep" + i, creep.toString());
            i.add();
        });
    }

}
