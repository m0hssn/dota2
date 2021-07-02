package connection;

import Model.Building;
import Model.Creep;
import Model.Hero;
import controllers.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utills.StaticData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Handler {
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private Creep[] creeps;
    private Hero[] heroes;

    public Handler(DataOutputStream outputStream, DataInputStream inputStream) throws IOException, JSONException {
        this.outputStream = outputStream;
        this.inputStream = inputStream;

        JSONObject object = new JSONObject(inputStream.readUTF());

        StaticData.nameTag.setText(object.getString("players"));
    }

    public void turn() throws Exception  {
        StaticData.management.acquire();

        outputStream.writeUTF(StaticData.management.get().toString());

        JSONArray array = new JSONArray(inputStream.readUTF());
        JSONObject object = (JSONObject) array.get(0);

        StaticData.gameOver = object.getString("gameOver").equals("true");

        if(!StaticData.gameOver) {
            buildings((JSONObject) array.get(1), (JSONObject) array.get(2));
            creeps = creeps((JSONObject) array.get(3));
            heroes = heroes((JSONObject) array.get(4));

            StaticData.management.set((JSONObject) array.get(5));

            StaticData.management.release();
        }else {
            StaticData.winner = object.getString("winner");
        }
    }

    public Creep[] getCreeps() {
        return creeps;
    }

    public Hero[] getHeroes() {
        return heroes;
    }

    private static void buildings(JSONObject green, JSONObject red) throws JSONException {
        Map.Green.receiveDamage(getBuildings(green));
        Map.Red.receiveDamage(getBuildings(red));
    }

    private static Building[] getBuildings(JSONObject object) throws JSONException {
        int num = Integer.parseInt(object.getString("number"));
        Building[] buildings = new Building[num];
        for (int i = 0; i < num; i++) {
            buildings[i] = Building.parse(object.getString("b" + i));
        }
        return buildings;
    }

    private static Creep[] creeps(JSONObject object) throws JSONException {
        int num = Integer.parseInt(object.getString("number"));
        Creep[] creeps = new Creep[num];
        for (int i = 0; i < num; i++) {
            creeps[i] = Creep.parse(object.getString("creep" + i));
        }
        return creeps;
    }

    private static Hero[] heroes(JSONObject object) throws JSONException {
        Hero[] heroes = new Hero[2];
        for (int i = 0; i < 2; i++) {
            heroes[i] = Hero.parse(object.getString("hero" + i));
        }
        return heroes;
    }
}
