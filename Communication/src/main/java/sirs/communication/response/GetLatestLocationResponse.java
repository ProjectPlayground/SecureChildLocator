package sirs.communication.response;

public class GetLatestLocationResponse extends Response
{
    private boolean successful;
    private String location;

    public GetLatestLocationResponse(String location)
    {
        this.successful = true;
        this.location = location;
    }

    public GetLatestLocationResponse(boolean successful)
    {
        this.successful = successful;
        this.location = null;
    }

    public boolean isSuccessful()
    {
        return successful;
    }

    public void setSuccessful(boolean successful)
    {
        this.successful = successful;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }
}
