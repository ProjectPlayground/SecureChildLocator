package sirs.communication.response;

import org.joda.time.DateTime;

public class CipheredResponse
{
    private String message;
    private DateTime dateTime;
    private String messageHash;
    private String timestampHash;

    public CipheredResponse(String message, DateTime dateTime, String messageHash, String timestampHash)
    {
        this.message = message;
        this.dateTime = dateTime;
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

    public DateTime getDateTime()
    {
        return dateTime;
    }

    public void setTimestamp(DateTime dateTime)
    {
        this.dateTime = dateTime;
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
