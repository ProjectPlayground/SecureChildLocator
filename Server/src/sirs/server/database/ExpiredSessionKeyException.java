package sirs.server.database;

public class ExpiredSessionKeyException extends Exception
{
    private String key;

    public ExpiredSessionKeyException(String key)
    {
        this.key = key;
    }

    @Override
    public String getMessage()
    {
        return key + " is expired.";
    }
}
