package comp;

import org.json.JSONObject;
import units.Hero.*;
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
    private Direction direction;

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

    private final Hero hero;

    private final HeroType heroType;

    public Team(Group group, HeroType type) {
        this.group = group;
        this.heroType = type;
        if(heroType == HeroType.Knight) {
            hero = new Knight(group);
        } else if(heroType == HeroType.Ranger) {
            hero = new Ranger(group);
        } else {
            hero = null;
        }

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

    public void every60Seconds() {
        releaseCreeps();
        hero.revive();
    }

    private void releaseCreeps() {
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

    public boolean lost() {
        return !ancient.isAlive();
    }

    private void move(List<Creep> creeps, List<Unit> row, Hero hero) {
        creeps.forEach(creep -> {
            List<Unit> units = new ArrayList<>();

            row.forEach(unit -> {
                if(Creep.intersects(creep.getPoint(), unit.getPoint(), creep.getType()) && unit.isAlive()) {
                    units.add(unit);
                }
            });

            if(Creep.intersects(creep.getPoint(), hero.getPoint(), creep.getType()) && hero.isAlive()) {
                units.add(hero);
            }

            if(units.size() != 0) {
                units.get(random.nextInt(units.size())).getHit(creep.getDamage());
            } else {
                creep.moveOne();
            }
        });
    }

    public void shoot(List<Tower> towers, List<Unit> row, Hero hero) {
        towers.forEach(tower -> {
            List<Unit> units = new ArrayList<>();

            boolean b = true;
            row.forEach(unit -> {
                if(Tower.intersects(tower.getPoint(), unit.getPoint())) {
                    units.add(unit);
                }
            });

            if(Tower.intersects(tower.getPoint(), hero.getPoint())){
                units.add(hero);
                b = false;
            }

            if(units.size() != 0 || !b) {
                units.get(random.nextInt(units.size())).getHit(tower.getDamage());
            }
        });
    }

    private void moveHero(List<Unit> row, Hero other) {
        if(heroType == HeroType.Ranger) {
            List<Unit> units = new ArrayList<>();
            row.forEach(unit -> {
                if(Hero.intersects(hero.getPoint(), unit.getPoint(), heroType) && unit.isAlive()) {
                    units.add(unit);
                }
            });

            if(Hero.intersects(hero.getPoint(), other.getPoint(), heroType) && other.isAlive()) {
                units.add(other);
            }

            if(units.size() != 0) {
                if(hero.getPower(1).isActive()) {
                    units.get(random.nextInt(units.size())).getHit(hero.getPower(1).getDamage());
                }
                if(hero.getPower(2).isActive()) {
                    for (int i = 0; i < 4; i++) {
                        units.get(random.nextInt(units.size())).getHit(hero.getDamage());
                    }
                } else  {
                    units.get(random.nextInt(units.size())).getHit(hero.getDamage());
                }
            }

            if(hero.getPower(3).isActive()) {
                units.forEach(unit -> unit.getHit(hero.getPower(3).getDamage()));
            }
            if (units.size() == 0) {
                hero.move(direction);
            }else {
                units.forEach(unit -> {
                    if(!unit.isAlive()) {
                        if(unit instanceof Hero) {
                            hero.addToExp(unit.getExp() * 0.13d + 100);
                        } else {
                            hero.addToExp(unit.getExp());
                        }
                    }
                });
            }

        } else if(heroType == HeroType.Knight) {

            List<Unit> units = new ArrayList<>();
            List<Unit> power1 = new ArrayList<>();
            List<Unit> power2 = new ArrayList<>();
            List<Unit> power3 = new ArrayList<>();

            row.forEach(unit -> {
                if(Hero.intersects(hero.getPoint(), unit.getPoint(), heroType) && unit.isAlive()) {
                    units.add(unit);
                }
            });

            if(Hero.intersects(hero.getPoint(), other.getPoint(), heroType) && other.isAlive()) {
                units.add(other);
            }

            if(units.size() != 0){
                units.get(random.nextInt(units.size())).getHit(hero.getDamage());
            }

            if(hero.getPower(1).isActive()) {
                row.forEach(unit -> {
                    if(hero.getPower(1).inRange(unit.getPoint()) && unit.isAlive()) {
                        power1.add(unit);
                        unit.getHit(hero.getPower(1).getDamage());
                    }
                });
                if(hero.getPower(1).inRange(other.getPoint()) && other.isAlive()) {
                    power1.add(other);
                    other.getHit(hero.getPower(1).getDamage());
                }
            }

            if(hero.getPower(2).isActive()) {
                row.forEach(unit -> {
                    if(hero.getPower(2).inRange(unit.getPoint()) && unit.isAlive()) {
                        power2.add(unit);
                    }
                });
                if(hero.getPower(2).inRange(other.getPoint()) && other.isAlive()) {
                    power2.add(other);
                }
                if(power2.size() != 0) {
                    power2.get(random.nextInt(power2.size())).getHit(hero.getPower(2).getDamage());
                }
            }

            if (hero.getPower(3).isActive()) {
                row.forEach(unit -> {
                    if(hero.getPower(3).inRange(unit.getPoint()) && unit.isAlive()) {
                        power3.add(unit);
                    }
                });
                if(hero.getPower(3).inRange(other.getPoint()) && other.isAlive()) {
                    power3.add(other);
                }
                if(power3.size() != 0) {
                    power3.get(random.nextInt(power3.size())).getHit(hero.getPower(3).getDamage());
                }
            }

            List<Unit> u = new ArrayList<>();
            u.addAll(units);
            u.addAll(power1);
            u.addAll(power2);
            u.addAll(power3);

            if(u.size() == 0) {
                hero.move(direction);
            }else {
                u.forEach(unit -> {
                    if(!unit.isAlive()) {
                        if(unit instanceof Hero) {
                            hero.addToExp(unit.getExp() * 0.13d + 100);
                        } else {
                            hero.addToExp(unit.getExp());
                        }
                    }
                });

            }

        }
    }

    private void regenerate() {
        lowRow.forEach(Unit::regenerate);
        midRow.forEach(Unit::regenerate);
        topRow.forEach(Unit::regenerate);
    }

    public void heroHandler(JSONObject object) {
        int levelup = Integer.parseInt(object.getString("levelup"));
        for (int i = 0; i < levelup; i++) {
            hero.levelUp();
        }
        direction = Direction.valueOf(object.getString("move"));
        for (int i = 0; i < 3; i++) {
            if(object.getString("power" + i).equals("true")){
                hero.getPower(i + 1).activate();
            } else {
                hero.getPower(i + 1).deActivate();
            }
        }
    }

    public void turn(Team other) {
        move(topCreeps, other.topRow, other.hero);
        move(midCreeps, other.midRow, other.hero);
        move(lowCreeps, other.lowRow, other.hero);

        if(hero.isAlive()){
            List<Unit> units = new ArrayList<>();
            units.addAll(other.topRow);
            units.addAll(other.lowRow);
            units.addAll(other.midRow);
            units.add(other.hero);
            moveHero(units, other.hero);
        }

        shoot(topTowers, other.topRow, other.hero);
        shoot(midTowers, other.midRow, other.hero);
        shoot(lowTowers, other.lowRow, other.hero);
    }

    public void removeDeadAndRegenerate() {
        removeDead(topRow, topCreeps, topTowers);
        removeDead(midRow, midCreeps, midTowers);
        removeDead(lowRow, lowCreeps, lowTowers);
        regenerate();
        hero.turn();
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

    public static JSONObject getHeroes(Team green, Team red) {
        JSONObject object = new JSONObject();
        object.put("hero0" ,green.hero.toString());
        object.put("hero1", red.hero.toString());
        return object;
    }

    public JSONObject getHero() {
        return hero.getJson();
    }
}
