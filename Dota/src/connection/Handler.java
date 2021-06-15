package connection;

import Model.Building;
import Model.Creep;
import Model.Move;
import controller.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class Handler {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    /**
     * Used to establish an input stream and an output stream
     *
     * @param IP The servers ip
     * @param port The port to connect to
     * @throws IOException because server may not answer
     */
    public Handler(String IP, int port) throws IOException {
        socket = new Socket(IP, port);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * This function is used to read info about units and
     *
     * @return Creep array which contains x, y and color of creeps
     * @throws IOException because server may be disconnected
     */
    public Creep[] read() throws IOException, InterruptedException {
        JSONArray array = new JSONArray(inputStream.readUTF());
        JSONObject green = array.getJSONObject(0);
        JSONObject red = array.getJSONObject(1);
        int numG = green.getInt("number");
        int numR = red.getInt("number");

        if(numR !=0 || numG != 0) {
            new Thread(()-> {

                if(numG != 0) {
                    Building[] greens = new Building[numG];
                    for (int i = 0; i < numG; i++) {
                        greens[i] = Building.parse(green.getString("b" + i));
                    }
                    Map.Green.receiveDamage(greens);
                }
                if(numR != 0) {
                    Building[] reds = new Building[numR];
                    for (int i = 0; i < numR; i++) {
                        reds[i] = Building.parse(red.getString("b" + i));
                    }
                    Map.Red.receiveDamage(reds);
                }
            }).start();
        }

        JSONObject creepsData = array.getJSONObject(2);
        int num = creepsData.getInt("number");
        Creep[] creeps = new Creep[num];
        CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            for (int i = 0; i < num; i++) {
                creeps[i] = Creep.parse(creepsData.getString("creep" + i));
            }
            latch.countDown();
        }).start();

        latch.await();
        return creeps;
    }

    /**
     * @param move command sent to server about the movement of hero
     * @throws IOException because server may be disconnected
     */
    public void write(Move move) throws IOException {
        JSONObject object = new JSONObject();
        object.put("move", move);
        outputStream.writeUTF(object.toString());
    }


    /**
     * This function is used to disconnect from the server
     *
     * @throws IOException because server may be disconnected
     */
    public void close() throws IOException {
        JSONObject object = new JSONObject();
        object.put("command", "exit");
        outputStream.writeUTF(object.toString());
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
