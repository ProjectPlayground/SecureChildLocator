package sirs.server.database.exceptions;

public class ChildDoesntExistException extends Exception
{
    private String _phoneNumber;

    public ChildDoesntExistException(String phoneNumber) {
        _phoneNumber = phoneNumber;
    }

    @Override
    public String getMessage()
    {
        return "Child doesn't exist. Unknown phone number: " + _phoneNumber;
    }
}
