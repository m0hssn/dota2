package comp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import units.Hero.HeroType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Game {
    private final Team green;
    private final Team red;
    private final DataInputStream[] inputStreams;
    private final DataOutputStream[] outputStreams;

    public Game(HeroType greenHero, HeroType redHero, DataInputStream[] inputStreams, DataOutputStream[] outputStreams) {

        green = new Team(Group.Green, greenHero);
        red = new Team(Group.Red, redHero);

        this.inputStreams = inputStreams;
        this.outputStreams = outputStreams;
    }

    public void start() throws JSONException {
        long timeToTurn = 0;
        long timeToRelease = 0;

        while (!green.lost() && !red.lost()) {
            if(System.currentTimeMillis() - timeToRelease > 60_000 || timeToRelease == 0) {
                timeToRelease = System.currentTimeMillis();
                green.every60Seconds();
                red.every60Seconds();
            }

            if (System.currentTimeMillis() - timeToTurn > 1_000 || timeToTurn == 0) {
                timeToTurn = System.currentTimeMillis();
                read();
                green.turn(red);
                red.turn(green);
                green.removeDeadAndRegenerate();
                red.removeDeadAndRegenerate();
                write();
            }
        }
        JSONObject o = new JSONObject();
        o.put("gameOver", "true");
        if(red.lost()){
            o.put("winner", "green");
        } else {
            o.put("winner", "red");
        }
        JSONArray a = new JSONArray();
        a.put(o);
        send(a.toString());
    }

    private void read() throws JSONException {
        JSONObject[] objects = new JSONObject[2];
        for (int i = 0; i < 2; i++) {

            try {
                String s = inputStreams[i].readUTF();
                objects[i] = new JSONObject(s);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }

        green.heroHandler(objects[0]);
        red.heroHandler(objects[1]);
    }

    private void send(String message) {
        try {
            inputStreams[0].readUTF();
            inputStreams[1].readUTF();
            outputStreams[0].writeUTF(message);
            outputStreams[1].writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write() throws JSONException {
        JSONArray array = new JSONArray();
        JSONObject o = new JSONObject();
        o.put("gameOver", "false");

        array.put(o);

        array.put(green.buildingS());
        array.put(red.buildingS());

        array.put(Team.creeps(green, red));
        array.put(Team.getHeroes(green, red));

        for (int i = 0; i < 2; i++) {

            if(i == 0) {
                try {
                    sendMessage(array, Group.Green);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    sendMessage(array, Group.Red);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    private void sendMessage(JSONArray array, Group group) throws IOException, JSONException {
        JSONArray a = new JSONArray(array.toString());
        if(group == Group.Green) {
            a.put(green.getHero());
            outputStreams[0].writeUTF(a.toString());
        } else if (group == Group.Red) {
            a.put(red.getHero());
            outputStreams[1].writeUTF(a.toString());
        }
    }
}
