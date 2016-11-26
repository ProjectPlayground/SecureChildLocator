package sirs.communication.request;

public abstract class Request
{
    private String type;

    public Request(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
