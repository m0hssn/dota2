package connection;


import comp.Game;
import org.json.JSONObject;
import units.Hero.HeroType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Handler {

    public static void main(String[] args) throws IOException {
        JSONObject o = new JSONObject();
        o.put("players", "Green:Player1, Red:Player2");
        ServerSocket ss = new ServerSocket(6969);
        Socket socket = ss.accept();
        Socket socket1 = ss.accept();
        DataInputStream[] inputStreams = new DataInputStream[2];
        DataOutputStream[] outputStreams = new DataOutputStream[2];
        inputStreams[0] = new DataInputStream(socket.getInputStream());
        inputStreams[1] = new DataInputStream(socket1.getInputStream());

        outputStreams[0] = new DataOutputStream(socket.getOutputStream());
        outputStreams[1] = new DataOutputStream(socket1.getOutputStream());

        outputStreams[0].writeUTF(o.toString());
        outputStreams[1].writeUTF(o.toString());
        Game game = new Game(HeroType.Knight, HeroType.Ranger, inputStreams, outputStreams);
        game.start();
    }
}
