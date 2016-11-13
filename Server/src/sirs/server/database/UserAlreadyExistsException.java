package sirs.server.database;

public class UserAlreadyExistsException extends Exception
{
    private String email;
    private String phoneNumber;

    public UserAlreadyExistsException(String email, String phoneNumber)
    {
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getMessage()
    {
        if (email != null) {
            return "Email already in use: " + email;
        }
        if (phoneNumber != null) {
            return "Phone number already in use: " + phoneNumber;
        }
        return null;
    }
}
