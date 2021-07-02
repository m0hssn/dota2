package main;

public class TaskManager {
    private static final String signUp = "{\"task\":\"signup\",\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\"}";
    private static final String checkUsername = "{\"task\":\"check username\",\"username\":\"%s\"}";
    private static final String login = "{\"task\":\"login\",\"username\":\"%s\",\"password\":\"%s\"," +
            "\"number\":\"" + Main.client.getNumber() + "\"}";
    private final static String search = "{\"task\":\"search\",\"to\":\"%s\",\"from\":\"%s\",\"number\":\"%s\"}";
    private static final String checkEmail = "{\"task\":\"check email\",\"email\":\"%s\"}";
    private static final String exit = "{\"task\":\"exit\",\"username\":\"%s\",\"number\":\"%s\"}";
    private static final String game = "{\"task\":\"game\",\"from\":\"%s\",\"to\":\"%s\",\"from number\":\"%s\"" +
            ",\"to number\":\"%s\"}";
    private static final String kill = "{\"task\":\"kill\",\"number\":\"%s\"}";

    public static String getKill()
    {
        return String.format(kill, Main.client.getNumber());
    }

    public static String getGame(String from, String to, int fromNumber)
    {
        return String.format(game, from, to, fromNumber, Main.client.getNumber());
    }

    public static String getExit(String username)
    {
        return String.format(exit, username, Main.client.getNumber());
    }

    public static String getCheckEmail(String email)
    {
        return String.format(checkEmail, email);
    }

    public static String getTask(String username)
    {
        return String.format(checkUsername, username);
    }

    public static String getTask(String username, String password)
    {
        return String.format(login, username, password);
    }

    public static String getTask(String username, String password, String email)
    {
        return String.format(signUp, username, password, email);
    }

    public static String searchTask(String username)
    {
        return String.format(search, username, Main.client.getUsername(), Main.client.getNumber());
    }
}
