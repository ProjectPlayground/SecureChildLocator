package sirs.server.database;

public class SessionKeyDoesntExistException extends Exception
{
    private int childID;
    private String phoneNumber;

    public SessionKeyDoesntExistException(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public SessionKeyDoesntExistException(int childID)
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
