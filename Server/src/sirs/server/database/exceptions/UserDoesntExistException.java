package sirs.server.database.exceptions;

public class UserDoesntExistException extends Exception
{
    private String _email;
    private int _user_id;

    public UserDoesntExistException(String email) {
        _email = email;
    }

    public UserDoesntExistException(int user_id) {
        _user_id = user_id;
        _email = null;
    }

    @Override
    public String getMessage()
    {
        if (_email == null) {
            return "User doesn't exist. Unknown user id: " + _user_id;
        }
        else {
            return "User doesn't exist. Unknown email: " + _email;
        }
    }
}
