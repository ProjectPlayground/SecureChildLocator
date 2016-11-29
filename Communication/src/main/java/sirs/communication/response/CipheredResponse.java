package sirs.communication.response;

public class CipheredResponse
{
    private String message;
    private String timestamp;
    private String messageHash;
    private String timestampHash;

    public CipheredResponse(String message, String timestamp, String messageHash, String timestampHash)
    {
        this.message = message;
        this.timestamp = timestamp;
        this.messageHash = messageHash;
        this.timestampHash = timestampHash;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getMessageHash()
    {
        return messageHash;
    }

    public void setMessageHash(String messageHash)
    {
        this.messageHash = messageHash;
    }

    public String getTimestampHash()
    {
        return timestampHash;
    }

    public void setTimestampHash(String timestampHash)
    {
        this.timestampHash = timestampHash;
    }
}
