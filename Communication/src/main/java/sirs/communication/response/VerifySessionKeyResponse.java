package sirs.communication.response;

public class VerifySessionKeyResponse extends Response
{
    private boolean valid;

    public VerifySessionKeyResponse(boolean valid)
    {
        super("VerifySessionKeyResponse");
        this.valid = valid;
    }

    public boolean isValid()
    {
        return valid;
    }

    public void setValid(boolean valid)
    {
        this.valid = valid;
    }
}
