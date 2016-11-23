package sirs.communication.response;

public class RemoveUserResponse extends Response
{
    private boolean successful;

    public RemoveUserResponse(boolean successful)
    {
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
