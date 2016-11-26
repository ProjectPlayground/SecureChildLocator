package sirs.server.database;

public class UsedSessionKeyException extends Exception
{
    private String key;

    public UsedSessionKeyException(String key)
    {
        this.key = key;
    }

    @Override
    public String getMessage()
    {
        return key + " is already in use.";
    }
}
