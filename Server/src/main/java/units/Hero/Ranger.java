package units.Hero;

import comp.Group;
import javafx.geometry.Point2D;
import org.decimal4j.util.DoubleRounder;
import org.json.JSONObject;

public class Ranger extends Hero {

    private static final double LEVEL_UP_HP = 38;
    private static final double LEVEL_UP_MANA = 16.8;
    private static final double LEVEL_UP_DAMAGE = 2.9;
    private static final double LEVEL_UP_ARMOR = 0.49;
    private static final double LEVEL_UP_HP_REGENERATION = 0.19;
    private static final double LEVEL_UP_MANA_REGENERATION = 0.07;

    private final Power FrostArrows = new Power() {

        public final int[] damage = {10, 17, 25, 40};
        public final int mana_cost = 12;

        @Override
        public boolean inRange(Point2D point) {
            return position.distance(point) <= 4;
        }

        @Override
        public boolean isAvailable() {
            return mana >= mana_cost && alive;
        }

        @Override
        public void turn() {
            if(mana_cost > mana || !alive) {
                active = false;
            } else if (active) {
                mana -= mana_cost;
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
            if(alive) {
                super.activate();
            }
        }
    };
    private final Power MultiArrow = new Power() {
        private final int[] mana_cost = {50, 70, 90, 110};
        private final int[] reload_duration = {26, 24, 22, 20};

        private int dur;

        @Override
        public boolean inRange(Point2D point) {
            return position.distance(point) <= 4;
        }

        @Override
        public boolean isAvailable() {
            return mana_cost[level_ - 1] <= mana && availableIn == 0 && alive;
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
            return mana_cost[level_ - 1];
        }

        @Override
        public double getDamage() {
            return 0;
        }

        @Override
        public void activate() {
            if(!active && isAvailable()) {
                super.activate();
                dur = 3;
                availableIn = reload_duration[level_ - 1];
                mana -= mana_cost[level_ - 1];
            }
        }
    };
    private final Power Marksmanship = new Power() {
        public final int[] damage = {300, 450, 550};

        @Override
        public boolean inRange(Point2D point) {
            return position.distance(point) <= 4;
        }

        @Override
        public boolean isAvailable() {
            return availableIn == 0 && level > 4;
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
            return 0;
        }

        @Override
        public double getDamage() {
            return damage[level_ - 1];
        }

        @Override
        public void activate() {
            if(!active && isAvailable()) {
                super.activate();
                availableIn = 85;
            }
        }

        @Override
        public void levelUp() {
            if(level_ < 3)
                level_++;
        }
    };

    public Ranger(Group group) {
        super(group);
        HP_MAX = 560;
        MANA_MAX = 255;
        damage = 52;
        armor = 3.33;
        hp_regeneration = 2.05;
        mana_regeneration = 0.75;
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

        FrostArrows.levelUp();
        MultiArrow.levelUp();

        if(level > 5) {
            Marksmanship.levelUp();
        }
    }

    @Override
    public String toString() {
        if(alive) {
            return previous.getX() + "," +
                    previous.getY() + "," +
                    position.getX() + "," +
                    position.getY() + "," +
                    HeroType.Ranger;
        } else {
            return previous.getX() + "," +
                    previous.getY() + "," +
                    position.getX() + "," +
                    position.getY() + "," +
                    HeroType.RangerDisabled;
        }
    }

    @Override
    public void turn() {
        FrostArrows.turn();
        Marksmanship.turn();
        MultiArrow.turn();
        if(!alive) {
            FrostArrows.deActivate();
            Marksmanship.deActivate();
            MultiArrow.deActivate();
        }
        this.regenerate();
    }

    @Override
    public JSONObject getJson() {
        JSONObject object = new JSONObject();

        object.put("level", String.valueOf(level));
        object.put("experience", String.valueOf(experience));
        object.put("mana", String.valueOf(mana));

        object.put("powerMana0", String.valueOf(FrostArrows.getMana_cost()));
        object.put("powerMana1", String.valueOf(MultiArrow.getMana_cost()));
        object.put("powerMana2", String.valueOf(Marksmanship.getMana_cost()));

        object.put("key0", String.valueOf(FrostArrows.isActive()));
        object.put("key1", String.valueOf(MultiArrow.isActive()));
        object.put("key2", String.valueOf(Marksmanship.isActive()));

        object.put("keyDisable0", String.valueOf(!FrostArrows.isAvailable()));
        object.put("keyDisable1", String.valueOf(!MultiArrow.isAvailable()));
        object.put("keyDisable2", String.valueOf(!Marksmanship.isAvailable()));


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

        object.put("power1", "FrostArrows" + '(' + FrostArrows.getMana_cost() + ')');
        object.put("power2", "MultiArrow" + '(' + MultiArrow.getMana_cost() + ')');
        object.put("power3", "Marksmanship" + '(' + Marksmanship.getMana_cost() + ')');

        return object;
    }

    @Override
    public Power getPower(int i) {
        if(i == 1) {
            return FrostArrows;
        } else if (i == 2) {
            return MultiArrow;
        } else if (i == 3) {
            return Marksmanship;
        } else {
            return null;
        }
    }
}
