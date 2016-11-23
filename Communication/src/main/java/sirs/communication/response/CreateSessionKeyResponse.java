package sirs.communication.response;

public class CreateSessionKeyResponse extends Response
{
    private boolean successful;
    private String sessionKey;

    public CreateSessionKeyResponse(boolean successful)
    {
        this.successful = successful;
        sessionKey = null;
    }

    public CreateSessionKeyResponse(String sessionKey)
    {
        this.successful = true;
        this.sessionKey = sessionKey;
    }

    public boolean isSuccessful()
    {
        return successful;
    }

    public void setSuccessful(boolean successful)
    {
        this.successful = successful;
    }

    public String getSessionKey()
    {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey)
    {
        this.sessionKey = sessionKey;
    }
}
