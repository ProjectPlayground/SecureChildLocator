package sirs.server.database.exceptions;

public class ChildDoesntExistException extends Exception
{
    private int child_id;
    private String phoneNumber;

    public ChildDoesntExistException(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public ChildDoesntExistException(int child_id)
    {
        this.child_id = child_id;
        this.phoneNumber = null;
    }

    @Override
    public String getMessage()
    {
        if (phoneNumber == null) {
            return "Child doesn't exist. Unknown child id: " +_child_id;
        }
        else {
            return "Child doesn't exist. Unknown phone number: " + phoneNumber;
        }
    }
}
