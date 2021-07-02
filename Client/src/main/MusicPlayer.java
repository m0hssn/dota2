package main;


public class MusicPlayer implements Runnable
{
    private static boolean continues = true;
    private static final int time_seconds = 4 * 60 + 20;

    public static void kill()
    {
        continues = false;
    }

    public static void revive()
    {
        continues = true;
    }


    @Override
    public void run()
    {
        while (continues)
        {
            Main.music.play();

            try
            {
                Thread.sleep(1000 * time_seconds);
            }
            catch (InterruptedException e) {}
        }
    }
}
