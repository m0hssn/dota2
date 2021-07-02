package data.database;

import data.User;
import main.Main;

import java.sql.*;

public class DataBase
{
    private final static String tableName = "dota2database", dataBaseName = "dota2";
    private final static int port = 3306;

    public static void saveUser(User user) throws Exception
    {
        String query = "insert into " + tableName + "(name,password,email) values('%s','%s','%s')";
        query = String.format(query, user.getUsername(), user.getPassword(), user.getEmail());

        execute(query);
    }

    private static void execute(String query) throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://localhost:" + port + "/" + dataBaseName + "?user=root";
        Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();

        statement.execute(query);

        closeAll(statement, connection);
    }

    private static void closeAll(Statement statement, Connection connection) throws SQLException
    {
        statement.close();
        connection.close();
    }

    public static void read() throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://localhost:" + port + "/" + dataBaseName + "?user=root";
        Connection connection = DriverManager.getConnection(url);
        Statement statement = connection.createStatement();

        String query = "select * from " + tableName;
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next())
        {
            User user = new User(resultSet.getString("name"), resultSet.getString("password"),
                    resultSet.getString("email"));

            Main.users.add(user);
        }

        closeAll(statement, connection);
    }
}
