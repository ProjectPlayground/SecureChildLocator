package sirs.communication.request;

public class GetLocationsRequest extends Request
{
    private String email;
    private String password;
    private String sessionKey;

    public GetLocationsRequest(String email, String password, String sessionKey)
    {
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
