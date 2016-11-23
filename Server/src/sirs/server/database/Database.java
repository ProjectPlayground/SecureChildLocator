package sirs.server.database;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Database
{
    private static final String DATABASE = "jdbc:mysql://localhost:3306/childlocator";
    private static final String USER = "root";
    private static final String PASSWORD = "sirs@childlocator16";

    private static final int MINUTES_15 = 900000; // 15 minutes in ms

    private Connection connection;

    private SecureRandom random;

    public static void main(String[] args)
    {
        try {
            Database database = new Database();
/*
            database.addUser("hc@g.com", "3", "qwerty");
            database.addUser("h@g.com", "97", "qwerty");
            database.addUser("c@g.com", "96", "qwerty");
            database.addChild("98", "h@g.com");
            database.addChild("99", "h@g.com");
            database.addChild("100", "c@g.com");

            database.addLocation(1, 1, "location7", new Date());
            Thread.sleep(4000);
            database.addLocation(1, 1, "location8", new Date());
            Thread.sleep(4000);
            database.addLocation(1, 1, "location9", new Date());
            /*
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
        random = new SecureRandom();
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

    public void addUser(String email, String phoneNumber, String passwordHash)
            throws UserAlreadyExistsException
    {
        checkAddUser(email, phoneNumber);

        try {
            String addUserStatement = "insert into users (email, phone_number, " +
                                      "password_hash) values (?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(addUserStatement);
            statement.setString(1, email);
            statement.setString(2, phoneNumber);
            statement.setString(3, passwordHash);

            statement.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addLocation(String sessionKey, String email, String location)
            throws UserDoesntExistException, SessionKeyDoesntExistException
    {
        checkLocation(sessionKey, email);

        try {
            String addLocation = "insert into location (location_date, location, session_key, email) " +
                    "values (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(addLocation);

            Timestamp timestamp = new java.sql.Timestamp(new Date().getTime());

            statement.setTimestamp(1, timestamp);
            statement.setString(2, location);
            statement.setString(3, sessionKey);
            statement.setString(4, email);

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

    public TreeMap<Date, String> getAllLocations(int userID, int childID)
            throws UserDoesntExistException, SessionKeyDoesntExistException
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
            throws UserDoesntExistException, SessionKeyDoesntExistException
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

    public String createSessionKey()
    {
        try {
            List<String> sessionKeys = new ArrayList<>();

            String getCodes = "select session_key from session_keys";
            PreparedStatement statement = connection.prepareStatement(getCodes);
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                String key = result.getString("session_key");
                sessionKeys.add(key);
            }

            String newKey = new BigInteger(130, random).toString(32);

            while (sessionKeys.contains(newKey)) {
                newKey = new BigInteger(130, random).toString(32);
            }

            return newKey;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void checkSessionKey(String key)
            throws ExpiredSessionKeyException
    {
        try {
            String getCodes = "select * from session_keys where session_key = ? limit 1";
            PreparedStatement statement = connection.prepareStatement(getCodes);
            statement.setString(1, key);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String sessionKey = result.getString("session_key");
                int tries = result.getInt("tries");
                Timestamp timestamp = result.getTimestamp("session_timestamp");

                if (tries >= 3) {
                    throw new ExpiredSessionKeyException(key);
                }

                Timestamp time = new Timestamp(new Date().getTime());

                if (time.getTime() > (timestamp.getTime() + MINUTES_15)) {
                    throw new ExpiredSessionKeyException(key);
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

    private void checkLocation(String sessionKey, String email)
            throws UserDoesntExistException, SessionKeyDoesntExistException
    {
        try {
            String checkLocation = "select * from users where email = ?";
            PreparedStatement statement = connection.prepareStatement(checkLocation);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                throw new UserDoesntExistException(email);
            }

            checkKey(sessionKey);
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
