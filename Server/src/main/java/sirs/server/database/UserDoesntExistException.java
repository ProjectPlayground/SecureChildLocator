package sirs.server.database;

public class UserDoesntExistException extends Exception
{
    private String email;
    private int userID;

    public UserDoesntExistException(String email)
    {
        this.email = email;
    }

    public UserDoesntExistException(int userID)
    {
        this.userID = userID;
        this.email = null;
    }

    @Override
    public String getMessage()
    {
        if (email == null) {
            return "User doesn't exist. Unknown user id: " + userID;
        }
        else {
            return "User doesn't exist. Unknown email: " + email;
        }
    }
}
