package sirs.server.database.exceptions;

public class UserDoesntExistException extends Exception
{
    private String email;
    private int user_id;

    public UserDoesntExistException(String email)
    {
        this.email = email;
    }

    public UserDoesntExistException(int user_id)
    {
        this.user_id = user_id;
        email = null;
    }

    @Override
    public String getMessage()
    {
        if (email == null) {
            return "User doesn't exist. Unknown user id: " + user_id;
        }
        else {
            return "User doesn't exist. Unknown email: " + email;
        }
    }
}
