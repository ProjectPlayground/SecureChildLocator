package sirs.server.database;

public class IncorrectPasswordException extends Exception
{
    private String email;
    private String password;

    public IncorrectPasswordException(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    @Override
    public String getMessage()
    {
        return "Incorrect password for email " + email;
    }
}
