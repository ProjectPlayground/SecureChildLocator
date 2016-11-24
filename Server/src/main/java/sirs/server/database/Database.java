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

            String sessionKey = database.createSessionKey("h@g.c", "qwerty");
            System.out.println(sessionKey + " length: " + sessionKey.length());

            database.addLocation(sessionKey, "h@g.c", "qwerty", "location1");

            database.getAllLocations("h@g.c", "qwerty", sessionKey);
            database.getLatestLocation("h@g.c", "qwerty", sessionKey);
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

    public void login(String email, String passwordHash)
            throws IncorrectPasswordException, UserDoesntExistException
    {
        try {
            String loginStatement = "select password_hash from users where email = ?";
            PreparedStatement statement = connection.prepareStatement(loginStatement);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String password = result.getString("password_hash");
                if (!password.equals(passwordHash)) {
                    throw new IncorrectPasswordException(email, passwordHash);
                }
            }
            else {
                throw new UserDoesntExistException(email);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
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

    public void addLocation(String sessionKey, String email, String password, String location)
            throws UserDoesntExistException, SessionKeyDoesntExistException,
            ExpiredSessionKeyException, IncorrectPasswordException, WrongKeyException
    {
        login(email, password);
        checkIfSessionKeyBelongsToUser(email, sessionKey);

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

    public void removeUser(String email, String password)
            throws UserDoesntExistException, IncorrectPasswordException
    {
        checkRemoveUser(email);
        login(email, password);

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

    public TreeMap<Date, String> getAllLocations(String email, String password, String sessionKey)
            throws UserDoesntExistException, SessionKeyDoesntExistException,
            ExpiredSessionKeyException, IncorrectPasswordException, WrongKeyException
    {
        login(email, password);
        checkIfSessionKeyBelongsToUser(email, sessionKey);

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

    public Map.Entry<Date, String> getLatestLocation(String email, String password, String sessionKey)
            throws UserDoesntExistException, SessionKeyDoesntExistException,
            ExpiredSessionKeyException, IncorrectPasswordException, WrongKeyException
    {
        login(email, password);
        checkIfSessionKeyBelongsToUser(email, sessionKey);

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

    public String createSessionKey(String email, String password)
            throws IncorrectPasswordException, UserDoesntExistException
    {
        login(email, password);

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

            String addKey = "insert into session_keys (session_key, email, used, session_timestamp) " +
                    "values (?, ?, ?, ?)";

            Timestamp time = new Timestamp(new Date().getTime());

            statement = connection.prepareStatement(addKey);
            statement.setString(1, newKey);
            statement.setString(2, email);
            statement.setBoolean(3, false);
            statement.setTimestamp(4, time);
            statement.execute();

            return newKey;
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /*
     * Verifies a session key when a user creates a child. It happens once.
     * It checks if it's not used already and if it's still valid
     * (they last for 15 minutes).
     * If this all checks, then we mark the session key as used and that's it.
     */
    public void verifySessionKey(String email, String password, String key)
            throws ExpiredSessionKeyException, UsedSessionKeyException,
            IncorrectPasswordException, UserDoesntExistException,
            WrongKeyException
    {
        login(email, password);

        try {
            String getCodes = "select * from session_keys where session_key = ? limit 1";
            PreparedStatement statement = connection.prepareStatement(getCodes);
            statement.setString(1, key);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String sessionKey = result.getString("session_key");
                String queryEmail = result.getString("email");
                boolean used = result.getBoolean("used");
                Timestamp timestamp = result.getTimestamp("session_timestamp");

                if (used) {
                    throw new UsedSessionKeyException(key);
                }
                else if (!queryEmail.equals(email)) {
                    throw new WrongKeyException(email, key);
                }
                else {
                    String setUsed = "update session_keys set used = ? where session_key = ?";
                    statement = connection.prepareStatement(setUsed);
                    statement.setBoolean(1, true);
                    statement.setString(2, sessionKey);
                    statement.execute();
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

    /*
     * Differs from verifySessionKey because it only checks if this session key
     * belongs to a specific user.
     * If it doesn't, then he can't use it.
     */
    private void checkIfSessionKeyBelongsToUser(String email, String sessionKey)
            throws WrongKeyException, UserDoesntExistException
    {
        try {
            String getCodes = "select * from session_keys where session_key = ? limit 1";
            PreparedStatement statement = connection.prepareStatement(getCodes);
            statement.setString(1, sessionKey);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String queryEmail = result.getString("email");
                if (!email.equals(queryEmail)) {
                    throw new WrongKeyException(email, sessionKey);
                }
            }
            else {
                throw new UserDoesntExistException(email);
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
