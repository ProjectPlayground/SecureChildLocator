package sirs.server.database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.ResultSet;

import java.util.Properties;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.AbstractMap;
import java.util.Date;

public class Database
{
    private static final String DATABASE = "jdbc:mysql://localhost:3306/childlocator";
    private static final String USER = "root";
    private static final String PASSWORD = "sirs@childlocator16";

    private Connection connection;

    public static void main(String[] args)
    {
        try {
            Database database = new Database();
            /*
            database.addUser("h", "c", "hc@g.com", "3", "qwerty");
            database.addUser("c", "v", "h@g.com", "97", "qwerty");
            database.addUser("c", "v", "c@g.com", "96", "qwerty");
            database.addChild("hh", "cc", "98", "h@g.com");
            database.addChild("hhh", "ccc", "99", "h@g.com");
            database.addChild("ccc", "c", "100", "c@g.com");

            database.addLocation(1, 1, "location7", new Date());
            Thread.sleep(4000);
            database.addLocation(1, 1, "location8", new Date());
            Thread.sleep(4000);
            database.addLocation(1, 1, "location9", new Date());

            database.getAllLocations(10, 10);
            database.getLatestLocation(10, 1);

            database.removeUser("t@g.com");
            database.removeChild("1", "c@g.com");
            database.removeChild("100", "t@g.com");
            */
        }
        catch (Exception e) {
            System.out.println(e.getMessage());;
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
            Properties properties = new Properties();

            properties.setProperty("user", USER);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("useSSL", "true");
            properties.setProperty("autoReconnect", "true");

            connection = DriverManager.getConnection(DATABASE, properties);
        }
        catch (SQLException exception) {
            System.out.println("Error trying to connect to database.");
            System.exit(1);
        }
    }

    public void addUser(String firstName, String lastName, String email, String phoneNumber, String passwordHash)
            throws UserAlreadyExistsException
    {
        checkAddUser(email, phoneNumber);

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
            System.out.println(e.getMessage());
        }
    }

    public void addChild(String firstName, String lastName, String phoneNumber, String email)
            throws ChildAlreadyExistsException, UserDoesntExistException
    {
        checkAddChild(email, phoneNumber);

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
            System.out.println(e.getMessage());
        }
    }

    public void addLocation(int childID, int userID, String location, Date date)
            throws UserDoesntExistException, ChildDoesntExistException
    {
        checkLocation(childID, userID);

        try {
            String addLocation = "insert into location (location_date, location, user_id_fk, child_id_fk) " +
                    "values (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(addLocation);

            Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            statement.setTimestamp(1, timestamp);
            statement.setString(2, location);
            statement.setInt(3, userID);
            statement.setInt(4, childID);

            statement.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUser(String email)
            throws UserDoesntExistException
    {
        checkRemoveUser(email);

        try {
            int userID = -1;

            String removeUser = "select user_id from users where email = ? limit 1";
            PreparedStatement removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setString(1, email);
            ResultSet result = removeUserStatement.executeQuery();

            while (result.next()) {
                userID = Integer.parseInt(result.getString(1));
            }

            if (userID == -1) {
                throw new UserDoesntExistException(email);
            }

            removeUser = "delete from location where user_id_fk = ?";
            removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setInt(1, userID);
            removeUserStatement.execute();

            removeUser = "delete from children where user_id_fk = ?";
            removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setInt(1, userID);
            removeUserStatement.execute();

            removeUser = "delete from users where user_id = ?";
            removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setInt(1, userID);
            removeUserStatement.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeChild(String phoneNumber, String email)
            throws ChildDoesntExistException, UserDoesntExistException
    {
        checkRemoveChild(phoneNumber, email);

        try {
            int childID = -1;
            int userID = -1;

            PreparedStatement statement = connection.prepareStatement("select user_id from users where email = ? limit 1");
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                userID = Integer.parseInt(result.getString(1));
            }

            if (userID == -1) {
                System.out.println("User not found");
                System.exit(1);
            }

            statement = connection.prepareStatement("select child_id from children where user_id_fk = ? and phone_number = ?");
            statement.setInt(1, userID);
            statement.setString(2, phoneNumber);
            result = statement.executeQuery();

            while (result.next()) {
                childID = Integer.parseInt(result.getString(1));
            }

            if (childID == -1) {
                System.out.println("Child not found");
                System.exit(1);
            }

            statement = connection.prepareStatement("delete from children where child_id = ?");
            statement.setInt(1, childID);
            statement.execute();

            statement = connection.prepareStatement("delete from location where child_id_fk = ? and user_id_fk = ?");
            statement.setInt(1, childID);
            statement.setInt(2, userID);
            statement.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public TreeMap<Date, String> getAllLocations(int userID, int childID)
            throws UserDoesntExistException, ChildDoesntExistException
    {
        checkLocation(childID, userID);

        TreeMap<Date, String> locations = new TreeMap<>();

        try {
            String getLocations = "select location, location_date from location where user_id_fk = ? and child_id_fk = ?";
            PreparedStatement getLocationsStatement = connection.prepareStatement(getLocations);
            getLocationsStatement.setInt(1, userID);
            getLocationsStatement.setInt(2, childID);
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
            System.out.println(e.getMessage());
        }

        return locations;
    }

    public Map.Entry<Date, String> getLatestLocation(int userID, int childID)
            throws UserDoesntExistException, ChildDoesntExistException
    {
        checkLocation(childID, userID);

        try {
            Map.Entry<Date, String> locationEntry;

            String getLatestLocation = "select location_date, location from location where child_id_fk = ? and "
                                     + "user_id_fk = ? order by location_date desc limit 1";
            PreparedStatement statement = connection.prepareStatement(getLatestLocation);
            statement.setInt(1, childID);
            statement.setInt(2, userID);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String location = result.getString("location");
                Date locationDate = result.getTimestamp("location_date");

                locationEntry = new AbstractMap.SimpleEntry<>(locationDate, location);

                System.out.println(locationEntry);

                return locationEntry;
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private void checkAddUser(String email, String phoneNumber)
            throws UserAlreadyExistsException
    {
        try {
            String checkUser = "select * from users where email = ? or phone_number = ?";
            PreparedStatement checkUserStatement = connection.prepareStatement(checkUser);
            checkUserStatement.setString(1, email);
            checkUserStatement.setString(2, phoneNumber);
            ResultSet result = checkUserStatement.executeQuery();

            if (result.next()) {
                String phoneNumberQuery = result.getString("phone_number");
                String emailQuery = result.getString("email");

                if (phoneNumberQuery.equals(phoneNumber)) {
                    throw new UserAlreadyExistsException(null, phoneNumber);
                }
                else if (emailQuery.equals(email)) {
                    throw new UserAlreadyExistsException(email, null);
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkAddChild(String email, String phoneNumber)
            throws ChildAlreadyExistsException, UserDoesntExistException
    {
        try {
            int userID = -1;

            String checkChild = "select user_id from users where email = ? limit 1";
            PreparedStatement statement = connection.prepareStatement(checkChild);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                userID = Integer.parseInt(result.getString(1));
            }

            if (userID == -1) {
                throw new UserDoesntExistException(email);
            }

            checkChild = "select phone_number from children where phone_number = ? and user_id_fk = ?";
            statement = connection.prepareStatement(checkChild);
            statement.setString(1, phoneNumber);
            statement.setInt(2, userID);
            result = statement.executeQuery();

            if (result.next()) {
                String phoneNumberQuery = result.getString("phone_number");

                if (phoneNumberQuery.equals(phoneNumber)) {
                    throw new ChildAlreadyExistsException(phoneNumber);
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkLocation(int childID, int userID)
            throws UserDoesntExistException, ChildDoesntExistException
    {
        try {
            String checkLocation = "select * from children where child_id = ?";
            PreparedStatement statement = connection.prepareStatement(checkLocation);
            statement.setInt(1, childID);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                throw new ChildDoesntExistException(childID);
            }

            checkLocation = "select * from users where user_id = ?";
            statement = connection.prepareStatement(checkLocation);
            statement.setInt(1, userID);
            result = statement.executeQuery();

            if (!result.next()) {
                throw new UserDoesntExistException(userID);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkRemoveChild(String phoneNumber, String email)
            throws ChildDoesntExistException, UserDoesntExistException
    {
        try {
            int userID = -1;

            String checkChild = "select user_id from users where email = ? limit 1";
            PreparedStatement statement = connection.prepareStatement(checkChild);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                userID = Integer.parseInt(result.getString(1));
            }

            if (userID == -1) {
                throw new UserDoesntExistException(email);
            }

            checkChild = "select * from children where user_id_fk = ? and phone_number = ?";
            statement = connection.prepareStatement(checkChild);
            statement.setInt(1, userID);
            statement.setString(2, phoneNumber);
            result = statement.executeQuery();

            if (!result.next()) {
                throw new ChildDoesntExistException(phoneNumber);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkRemoveUser(String email)
            throws UserDoesntExistException
    {
        try {
            String checkUser = "select user_id from users where email = ?";
            PreparedStatement statement = connection.prepareStatement(checkUser);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                throw new UserDoesntExistException(email);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
