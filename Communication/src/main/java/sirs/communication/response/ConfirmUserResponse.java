package sirs.communication.response;

public class ConfirmUserResponse extends Response
{
    private boolean successful;

    public ConfirmUserResponse(boolean successful)
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

