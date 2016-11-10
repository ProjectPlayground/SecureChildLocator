package sirs.server.database.exceptions;

public class ChildAlreadyExistsException extends Exception
{
    private String _phoneNumber;

    public ChildAlreadyExistsException(String phoneNumber) {
        _phoneNumber = phoneNumber;
    }

    @Override
    public String getMessage()
    {
        return "Child already exists: phone number already in use " + _phoneNumber;
    }
}
