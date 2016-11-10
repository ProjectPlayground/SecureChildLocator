package sirs.server.database.exceptions;

public class UserDoesntExistException extends Exception
{
    private String _email;

    public UserDoesntExistException(String email) {
        _email = email;
    }

    @Override
    public String getMessage()
    {
        return "User doesn't exist. Unknown email: " + _email;
    }
}
