package sirs.server.database.exceptions;

public class ChildAlreadyExistsException extends Exception
{
    private String phoneNumber;

    public ChildAlreadyExistsException(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getMessage()
    {
        return "Child already exists: phone number already in use " + phoneNumber;
    }
}
