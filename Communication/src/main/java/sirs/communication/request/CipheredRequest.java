package sirs.communication.request;

import java.security.Timestamp;

public class CipheredRequest
{
    private String message;
    private Timestamp timestamp;
    private byte[] messageHash;
    private byte[] timestampHash;

    public CipheredRequest(String message, Timestamp timestamp, byte[] messageHash, byte[] timestampHash)
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

    public byte[] getMessageHash()
    {
        return messageHash;
    }

    public void setMessageHash(byte[] messageHash)
    {
        this.messageHash = messageHash;
    }

    public byte[] getTimestampHash()
    {
        return timestampHash;
    }

    public void setTimestampHash(byte[] timestampHash)
    {
        this.timestampHash = timestampHash;
    }
}
