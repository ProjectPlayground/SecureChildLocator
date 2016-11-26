package sirs.communication.response;

public class AddUserResponse extends Response
{
    private boolean successful;

    public AddUserResponse(boolean successful)
    {
        super("AddUserResponse");
        this.successful = successful;
    }

    public boolean isSuccessful()
    {
        return successful;
    }

    public void setSuccessful(boolean successful)
    {
        this.successful = successful;
    }
}