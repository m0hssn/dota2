package comp;

import org.json.JSONArray;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Game {
    private final Team green;
    private final Team red;
    private final ServerSocket ss;
    private final Socket socket;
    private final DataOutputStream outputStream;
    private final DataInputStream inputStream;
    public Game() throws IOException {
        green = new Team(Group.Green);
        red = new Team(Group.Red);
        ss = new ServerSocket(6969);
        socket = ss.accept();
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
    }

    public void start() throws IOException {

        long timeToTurn = 0;
        long timeToRelease = 0;

        while (!(green.lost() || red.lost())) {
            if(System.currentTimeMillis() - timeToRelease >= 59_990 || timeToRelease == 0) {
                timeToRelease = System.currentTimeMillis();

                green.releaseCreeps();
                red.releaseCreeps();
            }

            if(System.currentTimeMillis() - timeToTurn >= 999 || timeToTurn == 0) {
                timeToTurn = System.currentTimeMillis();

                green.turn(red);
                red.turn(green);
                green.removeDeadAndRegenerate();
                red.removeDeadAndRegenerate();

                JSONArray array = new JSONArray();
                array.put(green.buildingS());
                array.put(red.buildingS());
                array.put(Team.creeps(green, red));
                outputStream.writeUTF(array.toString());
            }
        }
    }
}
