package sirs.communication.response;

public class CipheredResponse
{
    private byte[] message;
    private byte[] timestamp;
    private byte[] messageHash;
    private byte[] timestampHash;

    public CipheredResponse(byte[] message, byte[] timestamp, byte[] messageHash, byte[] timestampHash)
    {
        this.message = message;
        this.timestamp = timestamp;
        this.messageHash = messageHash;
        this.timestampHash = timestampHash;
    }

    public byte[] getMessage()
    {
        return message;
    }

    public void setMessage(byte[] message)
    {
        this.message = message;
    }

    public byte[] getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp)
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
