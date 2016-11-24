package sirs.communication.request;

public class AddLocationRequest extends Request
{
    private String sessionKey;
    private String email;
    private String location;
    private String password;

    public AddLocationRequest(String sessionKey, String email, String location, String password)
    {
        super("AddLocationRequest");
        this.sessionKey = sessionKey;
        this.email = email;
        this.location = location;
    }

    public String getSessionKey()
    {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey)
    {
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

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

}
