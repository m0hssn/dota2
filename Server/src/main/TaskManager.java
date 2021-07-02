package main;

public class TaskManager
{
    private final static String message = "{\"task\":\"send message\",\"message\":\"%s\"}";
    private final static String challenge = "{\"task\":\"challenge\",\"message\":\"%s\",\"number\":\"%s\"}";
    private final static String game = "{\"task\":\"game\"}";
    private final static String kill = "{\"task\":\"kill\"}";

    public static String getGame()
    {
        return game;
    }

    public static String getKill()
    {
        return kill;
    }

    public static String getTask(String message)
    {
        return String.format(TaskManager.message, message);
    }

    public static String getChallengeTask(String message, int number)
    {
        return String.format(TaskManager.challenge, message, number);
    }
}
