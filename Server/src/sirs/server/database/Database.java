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

            // ONLY WORKS IF DATABASE IS EMPTY!!!

            database.addUser("h@g.c", "96", "qwerty");
            database.addUser("c@g.c", "97", "qwerty");

            String sessionKey = database.createSessionKey();
            System.out.println(sessionKey + " length: " + sessionKey.length());

            database.addLocation(sessionKey, "h@g.c", "location1");

            database.getAllLocations("h@g.c", sessionKey);
            database.getLatestLocation("h@g.c", sessionKey);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
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
            throws UserDoesntExistException, SessionKeyDoesntExistException, ExpiredSessionKeyException
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
            String removeUser = "delete from users where email = ?";
            PreparedStatement removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setString(1, email);
            removeUserStatement.execute();

            removeUser = "delete from location where email = ?";
            removeUserStatement = connection.prepareStatement(removeUser);
            removeUserStatement.setString(1, email);
            removeUserStatement.execute();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public TreeMap<Date, String> getAllLocations(String email, String sessionKey)
            throws UserDoesntExistException, SessionKeyDoesntExistException, ExpiredSessionKeyException
    {
        checkLocation(email, sessionKey);

        TreeMap<Date, String> locations = new TreeMap<>();

        try {
            String getLocations = "select location_date, location from location where email = ? and "
                    + "session_key = ? order by location_date desc";
            PreparedStatement getLocationsStatement = connection.prepareStatement(getLocations);
            getLocationsStatement.setString(1, email);
            getLocationsStatement.setString(2, sessionKey);
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

    public Map.Entry<Date, String> getLatestLocation(String email, String sessionKey)
            throws UserDoesntExistException, SessionKeyDoesntExistException, ExpiredSessionKeyException
    {
        checkLocation(email, sessionKey);

        try {
            Map.Entry<Date, String> locationEntry;

            String getLatestLocation = "select location_date, location from location where email = ? and "
                                     + "session_key = ? order by location_date desc limit 1";
            PreparedStatement statement = connection.prepareStatement(getLatestLocation);
            statement.setString(1, email);
            statement.setString(2, sessionKey);
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

            String newKey = new BigInteger(100, random).toString(32);

            while (sessionKeys.contains(newKey)) {
                newKey = new BigInteger(100, random).toString(32);
            }

            return newKey;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
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

    private void checkLocation(String email, String sessionKey)
            throws UserDoesntExistException, SessionKeyDoesntExistException, ExpiredSessionKeyException
    {
        try {
            String checkLocation = "select * from users where email = ?";
            PreparedStatement statement = connection.prepareStatement(checkLocation);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (!result.next()) {
                throw new UserDoesntExistException(email);
            }

            checkSessionKey(sessionKey);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkRemoveUser(String email)
            throws UserDoesntExistException
    {
        try {
            String checkUser = "select * from users where email = ?";
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
