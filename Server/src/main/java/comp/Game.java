package comp;

import org.json.JSONArray;
import org.json.JSONObject;
import units.Hero.HeroType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Game {
    private final Team green;
    private final Team red;
    private final DataInputStream[] inputStreams;
    private final DataOutputStream[] outputStreams;

    public Game(HeroType greenHero, HeroType redHero, DataInputStream[] inputStreams, DataOutputStream[] outputStreams) throws IOException {

        green = new Team(Group.Green, greenHero);
        red = new Team(Group.Red, redHero);

        this.inputStreams = inputStreams;
        this.outputStreams = outputStreams;
    }

    public void start() {
        long timeToTurn = 0;
        long timeToRelease = 0;

        while (!green.lost() && !red.lost()) {
            if(System.currentTimeMillis() - timeToRelease > 59_000 || timeToRelease == 0) {
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
        o.put("winner", "" /* enter winners name*/);
        send(o.toString());
    }

    private void read() {
        JSONObject[] objects = new JSONObject[2];
        CountDownLatch latch = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {
            final int j = i;
            new Thread(() -> {
                try {
                    objects[j] = new JSONObject(inputStreams[j].readUTF());
                    latch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        green.heroHandler(objects[0]);
        red.heroHandler(objects[1]);
    }

    private void send(String message) {
        try {
            outputStreams[0].writeUTF(message);
            outputStreams[1].writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write() {
        CountDownLatch latch = new CountDownLatch(2);
        JSONArray array = new JSONArray();
        JSONObject o = new JSONObject();
        o.put("gameOver", "false");

        array.put(o);

        array.put(green.buildingS());
        array.put(red.buildingS());

        array.put(Team.creeps(green, red));
        array.put(Team.getHeroes(green, red));

        for (int i = 0; i < 2; i++) {
            final int j = i;
            new Thread(()->{
                if(j == 0) {
                    try {
                        sendMessage(array, Group.Green);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        sendMessage(array, Group.Red);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                latch.countDown();
            }).start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void sendMessage(JSONArray array, Group group) throws IOException {
        JSONArray a = new JSONArray(array);
        if(group == Group.Green) {
            a.put(green.getHero());
            outputStreams[0].writeUTF(a.toString());
        } else if (group == Group.Red) {
            a.put(red.getHero());
            outputStreams[1].writeUTF(a.toString());
        }
    }
}
