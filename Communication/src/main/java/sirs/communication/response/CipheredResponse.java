package sirs.communication.response;

public class CipheredResponse extends Response
{
    private byte[] message;
    private byte[] hash;

    public CipheredResponse()
    {
        super("CipheredResponse");
    }

    public byte[] getMessage()
    {
        return message;
    }

    public void setMessage(byte[] message)
    {
        this.message = message;
    }

    public byte[] getHash()
    {
        return hash;
    }

    public void setHash(byte[] hash)
    {
        this.hash = hash;
    }
}
