package sirs.server.database.exceptions;

public class UserAlreadyExistsException extends Exception
{
    private String _email;
    private String _phoneNumber;

    public UserAlreadyExistsException(String email, String phoneNumber) {
        _email = email;
        _phoneNumber = phoneNumber;
    }

    @Override
    public String getMessage()
    {
        if (_email != null) {
            return "Email already in use: " + _email;
        }
        if (_phoneNumber != null) {
            return "Phone number already in use: " + _phoneNumber;
        }
        return null;
    }
}
