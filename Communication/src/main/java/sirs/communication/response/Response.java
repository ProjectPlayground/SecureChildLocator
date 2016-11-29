package sirs.communication.response;

public abstract class Response
{
    private String type;
    private String error;
    private String errorMessage;

    public Response(String type)
    {
        this.type = type;
        this.error = null;
        this.errorMessage = null;
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

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String message)
    {
        this.errorMessage = message;
    }
}
