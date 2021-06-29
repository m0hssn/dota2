package utills;

import Model.Move;
import javafx.scene.control.ToggleButton;
import org.decimal4j.util.DoubleRounder;
import org.json.JSONObject;

public class HeroManagement {
    private static final double[] req = {230, 370, 480, 580, 600, 720, 750, 890, 930, 970, 1010};

    private boolean canWrite;
    private Move move;

    private double experience;

    private byte level;

    private byte levelup;

    private double mana;

    private final boolean[] keysStatus;

    private final boolean[] keyDiasble;

    private final double[] powerMana;

    private boolean canLevelUp;

    public HeroManagement() {
        canWrite = true;
        move = Move.Still;
        keysStatus = new boolean[3];
        powerMana = new double[3];
        keyDiasble = new boolean[3];
        level = 1;
    }

    public void move(Move move) {
        if(canWrite) {
            this.move = move;
        }
    }

    public void acquire() {
        canWrite = false;
    }

    public void release() {
        canWrite = true;
    }

    public void set(JSONObject object) {
        levelup = 0;
        level = Byte.parseByte(object.getString("level"));
        experience = Double.parseDouble(object.getString("experience"));
        mana = Double.parseDouble(object.getString("mana"));

        for (int i = 0; i < 3; i++) {
            powerMana[i] = Double.parseDouble(object.getString("powerMana" + i));
            keysStatus[i] = object.getString("key" + i).equals("true");
            keyDiasble[i] = object.getString("keyDisable" + i).equals("true");
        }

        canLevelUp = object.getString("canLevelUp").equals("true");
        StaticData.levelup.setDisable(!canLevelUp);

        StaticData.label.setText(object.getString("label"));

        StaticData.manaHero.setProgress(Double.parseDouble(object.getString("mana%")));
        StaticData.hpHero.setProgress(Double.parseDouble(object.getString("hp%")));
        if(Double.parseDouble(object.getString("hp%")) == 0) {
            move = Move.Still;
        }

        StaticData.power1.setDisable(keyDiasble[0]);
        StaticData.power2.setDisable(keyDiasble[1]);
        StaticData.power3.setDisable(keyDiasble[2]);

        StaticData.power1.setSelected(keysStatus[0]);
        StaticData.power2.setSelected(keysStatus[1]);
        StaticData.power3.setSelected(keysStatus[2]);

        StaticData.power1.setText(object.getString("power1"));
        StaticData.power2.setText(object.getString("power2"));
        StaticData.power3.setText(object.getString("power3"));
    }

    public JSONObject get() {
        JSONObject object = new JSONObject();
        object.put("move", move.toString());
        object.put("levelup", String.valueOf(levelup));
        for (int i = 0; i < 3; i++) {
            object.put("power" + i, String.valueOf(keysStatus[i]));
        }
        return object;
    }

    public void setKey(ToggleButton button) {
        if(canWrite) {
            keysStatus[Integer.parseInt(button.getId().charAt(5) + "") - 1] = button.isSelected();

            if(button.isSelected()) {
                mana -= powerMana[Integer.parseInt(button.getId().charAt(5) + "") - 1];
                button.setDisable(true);
                if(powerMana[0] > mana) {
                    StaticData.power1.setDisable(true);
                }
                if(powerMana[1] > mana) {
                    StaticData.power2.setDisable(true);
                }
                if(powerMana[2] > mana) {
                    StaticData.power3.setDisable(true);
                }
            }
            setLabel();
        } else {
            button.setSelected(!button.isSelected());
        }
    }

    public void levelUp() {
        if(canWrite && canLevelUp) {
            level++;
            levelup++;
            experience -= req[level - 2];
            setLabel();
        }
    }

    private void setLabel() {
        if(level != 12) {
            StaticData.label.setText("experience = " + experience + "/" + req[level - 1] + "\nlevel = " + level + "\nmana = " + DoubleRounder.round(mana, 2));
            StaticData.levelup.setDisable(experience < req[level - 1]);
        } else {
            StaticData.label.setText("level = " + 12 + "\nmana = " + DoubleRounder.round(mana, 2));
        }
    }
}
