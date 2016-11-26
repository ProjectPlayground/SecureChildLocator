package sirs.communication.response;

public class LoginResponse extends Response
{
    private boolean successful;

    public LoginResponse(boolean successful)
    {
        super("LoginResponse");
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
