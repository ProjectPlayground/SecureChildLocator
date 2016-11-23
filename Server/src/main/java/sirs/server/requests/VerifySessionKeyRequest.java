package sirs.server.requests;

public class VerifySessionKeyRequest extends Request
{
    private String email;
    private String sessionKey;

    public VerifySessionKeyRequest(String email, String sessionKey)
    {
        this.email = email;
        this.sessionKey = sessionKey;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
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
