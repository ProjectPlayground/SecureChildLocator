package sirs.communication.response;

import java.util.List;

public class GetLocationsResponse extends Response
{
    private boolean successful;
    private List<String> location;

    public GetLocationsResponse(List<String> locations)
    {
        super("GetLocationsResponse");
        this.successful = true;
        this.location = locations;
    }

    public GetLocationsResponse(boolean successful)
    {
        super("GetLocationsResponse");
        this.successful = successful;
    }

    public boolean isSuccessful()
    {
        return successful;
    }

    public void setSuccessful(boolean successful)
    {
        this.successful = successful;
    }

    public List<String> getLocation()
    {
        return location;
    }

    public void setLocation(List<String> location)
    {
        this.location = location;
    }
}
