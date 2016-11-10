package sirs.server.database;

import sirs.server.database.exceptions.ChildAlreadyExistsException;
import sirs.server.database.exceptions.ChildDoesntExistException;
import sirs.server.database.exceptions.UserAlreadyExistsException;
import sirs.server.database.exceptions.UserDoesntExistException;

import java.sql.*;
import java.util.List;

public class Database
{
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String database = "jdbc:mysql://localhost:3306/childlocator?user=root&password=sirs@childlocator16";

    private static final String removeUserStatement = //"delete from location where user_id_fk = (select user_id from users where email = ? limit 1);" +
                                                      //"delete from children where user_id_fk = (select user_id from users where email = ? limit 1);" +
                                                      "delete from users where user_id = (select user_id from users where email = ?)";

    //private static final String removeChildStatement = "delete from location where user_id_fk = ? and child_id = ?"

    private Connection connection = null;

    public static void main(String[] args) {
        try {
            Database database = new Database();

            //database.addUser("h", "c", "h@g.com", "96", "qwerty");
            //database.addUser("c", "v", "c@g.com", "97", "qwerty");
            //database.addChild("hh", "cc", "98", "h@g.com");
            //database.addChild("hhh", "ccc", "99", "h@g.com");
            //database.addChild("ccc", "c", "100", "c@g.com");

            // database.removeUser("h@g.com");
            database.removeChild("99", "c@g.com");
        }
        catch (Exception e) {
            e.getMessage();
        }
    }

    public Database()
    {
        connection = null;
        connectToDatabase();
    }

    private void connectToDatabase()
    {
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(database);
        }
        catch (SQLException exception) {
            System.out.println("Error trying to connect to database.");
            System.exit(1);
        }
        catch (ClassNotFoundException exception) {
            System.out.println("Error loading mysql driver.");
            System.exit(1);
        }
    }

    public void addUser(String firstName, String lastName, String email, String phoneNumber, String passwordHash)
            throws UserAlreadyExistsException
    {
        // checkAddUser(email, phoneNumber);

        try {
            String addUserStatement = "insert into users (first_name, last_name, email, phone_number, " +
                                      "password_hash) values (?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(addUserStatement);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setString(4, phoneNumber);
            statement.setString(5, passwordHash);

            statement.execute();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public void addChild(String firstName, String lastName, String phoneNumber, String email)
            throws ChildAlreadyExistsException
    {
        // checkAddChild(phoneNumber);

        try {
            String addChildStatement = "insert into children (first_name, last_name, phone_number, user_id_fk)" +
                                       "select ?, ?, ?, user_id from users where email = ? limit 1";

            PreparedStatement statement = connection.prepareStatement(addChildStatement);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, phoneNumber);
            statement.setString(4, email);

            statement.execute();
        }
        catch (SQLException e) {
            e.getMessage();
        }
    }

    public void removeUser(String email)
            throws UserDoesntExistException
    {
        // checkRemoveUser(email);

        try {
            String removeUserStatement = "delete from users where user_id in (" +
                                              "select temp.user_id from (" +
                                                   "select user_id from users where email = ?) as temp)";
            PreparedStatement statement = connection.prepareStatement(removeUserStatement);
            statement.setString(1, email);
            statement.execute();

            removeUserStatement = "delete from children where user_id_fk = (select user_id from users where email = ? limit 1)";
            statement = connection.prepareStatement(removeUserStatement);
            statement.setString(1, email);
            statement.execute();

            removeUserStatement = "delete from location where user_id_fk = (select user_id from users where email = ? limit 1)";
            statement = connection.prepareStatement(removeUserStatement);
            statement.setString(1, email);
            statement.execute();
        }
        catch (SQLException e) {
            e.getMessage();
        }
    }

    public void removeChild(String phoneNumber, String email)
            throws ChildDoesntExistException
    {
        // checkRemoveChild(phoneNumber, email);

        int child_id;
        int user_id;

        try {
            PreparedStatement statement = connection.prepareStatement("select user_id from users where email = ? limit 1");
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            user_id = result.getInt("user_id");

            statement = connection.prepareStatement("select child_id from children where user_id_fk = ? limit 1");
            statement.setInt(1, user_id);
            result = statement.executeQuery();
            child_id = result.getInt("child_id");

            statement = connection.prepareStatement("delete from children where child_id = ?");
            statement.setInt(1, child_id);
            statement.execute();

            statement = connection.prepareStatement("delete from location where child_id_fk = ? and user_id_fk = ?");
            statement.setInt(1, child_id);
            statement.setInt(2, user_id);
            statement.execute();
        }
        catch (SQLException e) {
            e.getMessage();
        }

    }

    // tries to return the latest 10, all if less than 10
    public List<String> getLatestLocations(String phoneNumber, String email) {
        return null;
    }

    public String getLatestLocation(String phoneNumber, String email) {
        return null;
    }

}
