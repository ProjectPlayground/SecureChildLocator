package sirs.server.database;

public class SessionKeyDoesntExistException extends Exception
{
    private String key;

    public SessionKeyDoesntExistException(String key)
    {
        this.key = key;
    }

    @Override
    public String getMessage()
    {
        return key + " doesn't exist.";
    }
}
