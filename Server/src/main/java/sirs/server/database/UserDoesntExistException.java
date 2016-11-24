package sirs.server.database;

public class UserDoesntExistException extends Exception
{
    private String email;

    public UserDoesntExistException(String email)
    {
        this.email = email;
    }

    @Override
    public String getMessage()
    {
        return "User doesn't exist. Unknown email: " + email;
    }
}
