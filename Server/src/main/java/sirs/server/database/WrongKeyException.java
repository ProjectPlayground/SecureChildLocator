package sirs.server.database;

public class WrongKeyException extends Exception
{
    private String email;
    private String key;

    public WrongKeyException(String email, String key)
    {
        this.email = email;
        this.key = key;
    }

    @Override
    public String getMessage()
    {
        return "The key " + key + " doesn't belong to the email " + email + ".";
    }
}
