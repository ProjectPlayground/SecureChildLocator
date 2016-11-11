package sirs.server.database;

import sirs.server.database.exceptions.ChildAlreadyExistsException;
import sirs.server.database.exceptions.ChildDoesntExistException;
import sirs.server.database.exceptions.UserAlreadyExistsException;
import sirs.server.database.exceptions.UserDoesntExistException;

import java.sql.*;
import java.util.Date;
import java.util.*;

public class Database
{
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String database = "jdbc:mysql://localhost:3306/childlocator?user=root&password=sirs@childlocator16";

    private Connection connection = null;

    public static void main(String[] args) {
        try {
            Database database = new Database();

/*
            database.addUser("h", "c", "h@g.com", "96", "qwerty");
            database.addUser("c", "v", "c@g.com", "97", "qwerty");
            database.addChild("hh", "cc", "98", "h@g.com");
            database.addChild("hhh", "ccc", "99", "h@g.com");
            database.addChild("ccc", "c", "100", "c@g.com");

            database.addLocation(1, 1, "location7", new Date());
            Thread.sleep(4000);
            database.addLocation(1, 1, "location8", new Date());
            Thread.sleep(4000);
            database.addLocation(1, 1, "location9", new Date());
*/
            // database.getAllLocations(1, 1);
            // database.getLatestLocation(1, 1);

            // database.removeUser("h@g.com");
            // database.removeChild("100", "c@g.com");
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

    public void addLocation(int child_id, int user_id, String location, Date date) {
        // checkAddLocation

        try {
            String addLocation = "insert into location (location_date, location, user_id_fk, child_id_fk) " +
                    "values (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(addLocation);

            Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            statement.setTimestamp(1, timestamp);
            statement.setString(2, location);
            statement.setInt(3, user_id);
            statement.setInt(4, child_id);

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
            int user_id = -1;

            String removeUser = "select user_id from users where email = ? limit 1";
            PreparedStatement removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setString(1, email);
            ResultSet result = removeUserStatement.executeQuery();

            while (result.next()) {
                user_id = Integer.parseInt(result.getString(1));
            }

            if (user_id == -1) {
                System.out.println("User not found");
                System.exit(1);
            }

            removeUser = "delete from location where user_id_fk = ?";
            removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setInt(1, user_id);
            removeUserStatement.execute();

            removeUser = "delete from children where user_id_fk = ?";
            removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setInt(1, user_id);
            removeUserStatement.execute();

            removeUser = "delete from users where user_id = ?";
            removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setInt(1, user_id);
            removeUserStatement.execute();
        }
        catch (SQLException e) {
            e.getMessage();
        }
    }

    public void removeChild(String phoneNumber, String email)
            throws ChildDoesntExistException
    {
        // checkRemoveChild(phoneNumber, email);

        try {
            int child_id = -1;
            int user_id = -1;

            PreparedStatement statement = connection.prepareStatement("select user_id from users where email = ? limit 1");
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                user_id = Integer.parseInt(result.getString(1));
            }

            if (user_id == -1) {
                System.out.println("User not found");
                System.exit(1);
            }

            statement = connection.prepareStatement("select child_id from children where user_id_fk = ? and phone_number = ?");
            statement.setInt(1, user_id);
            statement.setString(2, phoneNumber);
            result = statement.executeQuery();

            while (result.next()) {
                child_id = Integer.parseInt(result.getString(1));
            }

            if (child_id == -1) {
                System.out.println("Child not found");
                System.exit(1);
            }

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

    public TreeMap<Date, String> getAllLocations(int user_id, int child_id) {
        TreeMap<Date, String> locations = new TreeMap<>();

        try {
            String getLocations = "select location, location_date from location where user_id_fk = ? and child_id_fk = ?";
            PreparedStatement getLocationsStatement = connection.prepareStatement(getLocations);
            getLocationsStatement.setInt(1, user_id);
            getLocationsStatement.setInt(2, child_id);
            ResultSet result = getLocationsStatement.executeQuery();

            while (result.next()) {
                String location = result.getString("location");
                Date locationDate = result.getTimestamp("location_date");

                locations.put(locationDate, location);
            }

            Set set = locations.entrySet();
            Iterator it = set.iterator();

            while (it.hasNext()) {
                Map.Entry tm = (Map.Entry) it.next();
                System.out.println("Location date: " + tm.getKey() + " Location: " + tm.getValue());
            }

        }
        catch (SQLException e) {
            e.getMessage();
        }

        return locations;
    }

    public Map.Entry<Date, String> getLatestLocation(int user_id, int child_id) {
        // checkLocation(user_id, child_id);
        try {
            Map.Entry<Date, String> locationEntry;

            String getLatestLocation = "select location_date, location from location where child_id_fk = ? and "
                                     + "user_id_fk = ? order by location_date desc limit 1";
            PreparedStatement statement = connection.prepareStatement(getLatestLocation);
            statement.setInt(1, child_id);
            statement.setInt(2, user_id);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String location = result.getString("location");
                Date locationDate = result.getTimestamp("location_date");

                locationEntry = new AbstractMap.SimpleEntry<>(locationDate, location);

                System.out.println(locationEntry);

                return locationEntry;
            }
        }
        catch (SQLException e) {
            e.getMessage();
        }

        return null;
    }

}
