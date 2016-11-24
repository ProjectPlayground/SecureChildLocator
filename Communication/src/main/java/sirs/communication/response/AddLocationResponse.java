package sirs.communication.response;

public class AddLocationResponse extends Response
{
    private boolean successful;

    public AddLocationResponse(boolean successful)
    {
        super("AddLocationResponse");
        this.successful = successful;
    }

    public boolean issuccessful()
    {
        return successful;
    }

    public void setsuccessful(boolean successful)
    {
        this.successful = successful;
    }
}
