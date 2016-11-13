package sirs.server.database.exceptions;

public class ChildDoesntExistException extends Exception
{
    private int childID;
    private String phoneNumber;

    public ChildDoesntExistException(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public ChildDoesntExistException(int childID)
    {
        this.childID = childID;
        this.phoneNumber = null;
    }

    @Override
    public String getMessage()
    {
        if (phoneNumber == null) {
            return "Child doesn't exist. Unknown child id: " + childID;
        }
        else {
            return "Child doesn't exist. Unknown phone number: " + phoneNumber;
        }
    }
}
