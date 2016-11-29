package sirs.communication.request;

import java.security.Timestamp;

public class CipheredRequest
{
    private String message;
    private Timestamp timestamp;
    private String messageHash;
    private String timestampHash;

    public CipheredRequest(String message, Timestamp timestamp, String messageHash, String timestampHash)
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

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp)
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
