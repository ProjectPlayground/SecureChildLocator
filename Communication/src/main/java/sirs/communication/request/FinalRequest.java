package sirs.communication.request;

public class FinalRequest
{
    private String message;
    private String key;

    public FinalRequest(String message, String key)
    {
        this.message = message;
        this.key = key;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
