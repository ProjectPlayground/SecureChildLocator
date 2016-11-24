package sirs.communication.response;

public abstract class Response
{
    private String type;
    private String error;
    private String message;

    public Response(String type)
    {
        this.type = type;
        this.error = null;
        this.message = null;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getError()
    {
        return error;
    }

    public void setError(String error)
    {
        this.error = error;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
