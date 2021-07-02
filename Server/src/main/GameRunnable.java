package main;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import comp.Game;
import org.json.JSONException;
import org.json.JSONObject;
import units.Hero.HeroType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GameRunnable implements Runnable
{
    private DataInputStream[] inputStreams;
    private DataOutputStream[] outputStreams;
    private String from, to;

    public void setPlayers(String from, String to)
    {
        this.from = from;
        this.to = to;
    }

    public GameRunnable(DataInputStream[] inputStreams, DataOutputStream[] outputStreams)
    {
        this.inputStreams = inputStreams;
        this.outputStreams = outputStreams;
    }

    @Override
    public void run()
    {
        JSONObject o = new JSONObject();
        try
        {
            o.put("players", "Green:" + to + ", Red:" + from);
            outputStreams[0].writeUTF(o.toString());
            outputStreams[1].writeUTF(o.toString());
            Game game = new Game(HeroType.Knight, HeroType.Ranger, inputStreams, outputStreams);
            game.start();
            TaskListener t1 = new TaskListener(inputStreams[0], outputStreams[0]);
            TaskListener t2 = new TaskListener(inputStreams[1], outputStreams[1]);
            new Thread(t1).start();
            new Thread(t2).start();
        }
        catch (JSONException | IOException ignored) {}
    }
}
