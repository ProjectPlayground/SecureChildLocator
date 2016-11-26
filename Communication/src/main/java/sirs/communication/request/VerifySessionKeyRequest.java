package sirs.communication.request;

public class VerifySessionKeyRequest extends Request
{
    private String email;
    private String password;
    private String sessionKey;

    public VerifySessionKeyRequest(String email, String password, String sessionKey)
    {
        super("VerifySessionKeyRequest");
        this.email = email;
        this.password = password;
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
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
