package sirs.communication.request;

import org.joda.time.DateTime;

public class CipheredRequest
{
    private String message;
    private DateTime dateTime;
    private String messageHash;
    private String dateTimeHash;

    public CipheredRequest(String message, DateTime dateTime, String messageHash, String dateTimeHash)
    {
        this.message = message;
        this.dateTime = dateTime;
        this.messageHash = messageHash;
        this.dateTimeHash = dateTimeHash;
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

    public void setDateTime(DateTime dateTime)
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

    public String getDateTimeHash()
    {
        return dateTimeHash;
    }

    public void setDateTimeHash(String dateTimeHash)
    {
        this.dateTimeHash = dateTimeHash;
    }
}
