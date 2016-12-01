package sirs.communication.request;

public class CreateSessionKeyRequest extends Request
{
    private String email;
    private String password;

    public CreateSessionKeyRequest(String email)
    {
        super("CreateSessionKeyRequest");
        this.email = email;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
