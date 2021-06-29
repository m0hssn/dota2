package units.Hero;

import comp.Group;
import javafx.geometry.Point2D;
import org.decimal4j.util.DoubleRounder;
import org.json.JSONObject;

public class Knight extends Hero {

    private static final double LEVEL_UP_HP = 68;
    private static final double LEVEL_UP_MANA = 20.4;
    private static final double LEVEL_UP_DAMAGE = 3.4;
    private static final double LEVEL_UP_ARMOR = 0.34;
    private static final double LEVEL_UP_HP_REGENERATION = 0.34;
    private static final double LEVEL_UP_MANA_REGENERATION = 0.085;

    public final Power BreathFire = new Power() {
        public final int[] damage = {90, 170, 240, 300};
        public final int[] mana_cost = {90, 100, 110, 120};
        public final int[] reload_duration = {14, 13, 12, 11};

        @Override
        public boolean inRange(Point2D point) {
            return position.distance(point) <=4;
        }

        @Override
        public boolean isAvailable() {
            return mana >= mana_cost[level_ - 1] && availableIn == 0 && alive;
        }

        @Override
        public void turn() {
            active = false;
            if(availableIn != 0){
                availableIn--;
            }
        }

        @Override
        public int getMana_cost() {
            return mana_cost[level_ - 1];
        }

        @Override
        public double getDamage() {
            return damage[level_ - 1];
        }


        @Override
        public void activate() {
            if(!active && isAvailable()) {
                super.activate();
                availableIn = reload_duration[level_ - 1];
                mana -= mana_cost[level_ - 1];
            }
        }
    };
    public final Power DragonTail = new Power() {
        public final int[] damage = {70, 100, 130, 160};
        public final int[] mana_cost = {70, 80, 90, 100};
        public final int[] reload_duration = {16, 14, 12, 10};

        @Override
        public boolean inRange(Point2D point) {
            return position.distance(point) <= 2;
        }

        @Override
        public boolean isAvailable() {
            return mana_cost[level_ - 1] <= mana && availableIn == 0 && alive;
        }

        @Override
        public void turn() {
            active = false;
            if(availableIn != 0){
                availableIn--;
            }
        }

        @Override
        public int getMana_cost() {
            return mana_cost[level_ - 1];
        }

        @Override
        public double getDamage() {
            return damage[level_ - 1];
        }

        @Override
        public void activate() {
            if(!active && isAvailable()) {
                super.activate();
                availableIn = reload_duration[level_ - 1];
                mana -= mana_cost[level_ - 1];
            }
        }
    };
    public final Power ElderDragonForm = new Power() {
        public final int[] damage = {30, 60, 80};
        public final int[] duration = {20, 30, 40};
        public final int mana_cost = 50;
        public final int reload_duration = 115;

        private int dur;

        @Override
        public boolean inRange(Point2D point) {
            return position.distance(point) <= 4;
        }

        @Override
        public boolean isAvailable() {
            return mana_cost <= mana && availableIn == 0 && level > 4 && alive;
        }

        @Override
        public void turn() {
            if(dur == 0) {
                active = false;
            } else {
                dur--;
            }
            if(availableIn != 0) {
                availableIn--;
            }
        }

        @Override
        public int getMana_cost() {
            return mana_cost;
        }

        @Override
        public double getDamage() {
            return damage[level_ - 1];
        }

        @Override
        public void activate() {
            if(!active && isAvailable()) {
                super.activate();
                dur = duration[level_ - 1];
                availableIn = reload_duration;
                mana -= mana_cost;
            }
        }

        @Override
        public void levelUp() {
            if(level_ < 3)
                level_++;
        }
    };

    public Knight(Group group) {
        super(group);
        HP_MAX = 620;
        MANA_MAX = 291;
        damage = 55;
        armor = 3.17;
        hp_regeneration = 2.1;
        mana_regeneration = 0.9;
        mana = MANA_MAX;
        hp = HP_MAX;
    }


    @Override
    public void levelUp() {
        level++;
        mana_regeneration += LEVEL_UP_MANA_REGENERATION;
        hp_regeneration += LEVEL_UP_HP_REGENERATION;
        armor += LEVEL_UP_ARMOR;
        damage += LEVEL_UP_DAMAGE;
        HP_MAX += LEVEL_UP_HP;
        MANA_MAX += LEVEL_UP_MANA;

        experience -= req[level - 2];

        BreathFire.levelUp();
        DragonTail.levelUp();

        if(level > 5) {
            ElderDragonForm.levelUp();
        }
    }

    @Override
    public String toString() {
        if(alive) {
            return previous.getX() + "," +
                    previous.getY() + "," +
                    position.getX() + "," +
                    position.getY() + "," +
                    HeroType.Knight;
        } else {
            return previous.getX() + "," +
                    previous.getY() + "," +
                    position.getX() + "," +
                    position.getY() + "," +
                    HeroType.KnightDisabled;
        }
    }

    @Override
    public Power getPower(int i) {
        if(i == 1) {
            return BreathFire;
        } else if (i == 2) {
            return DragonTail;
        } else if (i == 3) {
            return ElderDragonForm;
        } else {
            return null;
        }
    }

    @Override
    public void turn() {
        ElderDragonForm.turn();
        BreathFire.turn();
        DragonTail.turn();
        if(!alive) {
            ElderDragonForm.deActivate();
            BreathFire.deActivate();
            DragonTail.deActivate();
        }
        this.regenerate();
    }

    @Override
    public JSONObject getJson() {
        JSONObject object = new JSONObject();

        object.put("level", String.valueOf(level));
        object.put("experience", String.valueOf(experience));
        object.put("mana", String.valueOf(mana));

        object.put("powerMana0", String.valueOf(BreathFire.getMana_cost()));
        object.put("powerMana1", String.valueOf(DragonTail.getMana_cost()));
        object.put("powerMana2", String.valueOf(ElderDragonForm.getMana_cost()));

        object.put("key0", String.valueOf(BreathFire.isActive()));
        object.put("key1", String.valueOf(DragonTail.isActive()));
        object.put("key2", String.valueOf(ElderDragonForm.isActive()));

        object.put("keyDisable0", String.valueOf(!BreathFire.isAvailable()));
        object.put("keyDisable1", String.valueOf(!DragonTail.isAvailable()));
        object.put("keyDisable2", String.valueOf(!ElderDragonForm.isAvailable()));


        if(level != 12) {
            object.put("canLevelUp", String.valueOf(experience >= req[level - 1]));
            object.put("label",
                    "experience = " + DoubleRounder.round(experience, 2) + "/" + req[level - 1] + "\nlevel = " + level + "\nmana = " + DoubleRounder.round(mana, 2));
        } else {
            object.put("canLevelUp", "false");
            object.put("label", "level = " + level + "\nmana = " + DoubleRounder.round(mana, 2));
        }
        object.put("mana%", String.valueOf(mana/ MANA_MAX));
        object.put("hp%", String.valueOf(hp/ HP_MAX));

        object.put("power1", "BreathFire" + '(' + BreathFire.getMana_cost() + ')');
        object.put("power2", "DragonTail" + '(' + DragonTail.getMana_cost() + ')');
        object.put("power3", "ElderDragonForm" + '(' + ElderDragonForm.getMana_cost() + ')');

        return object;
    }
}
