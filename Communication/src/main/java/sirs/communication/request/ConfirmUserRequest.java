package sirs.communication.request;

public class ConfirmUserRequest extends Request
{
    private String email;
    private String password;
    private String code;

    public ConfirmUserRequest(String email, String password, String code)
    {
        super("ConfirmUserRequest");
        this.email = email;
        this.password = password;
        this.code = code;
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

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}

