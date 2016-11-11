package sirs.server.database.exceptions;

public class ChildDoesntExistException extends Exception
{
    private int _child_id;
    private String _phoneNumber;

    public ChildDoesntExistException(String phoneNumber) {
        _phoneNumber = phoneNumber;
    }

    public ChildDoesntExistException(int child_id) {
        _child_id = child_id;
        _phoneNumber = null;
    }

    @Override
    public String getMessage()
    {
        if (_phoneNumber == null) {
            return "Child doesn't exist. Unknown child id: " + _child_id;
        }
        else {
            return "Child doesn't exist. Unknown phone number: " + _phoneNumber;
        }
    }
}
