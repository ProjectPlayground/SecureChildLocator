package sirs.communication.request;

class CipheredRequest extends Request
{
    private byte[] message;
    private byte[] hash;
    private byte[] key;

    public CipheredRequest(byte[] message, byte[] hash, byte[] key)
    {
        super("CipheredRequest");
        this.message = message;
        this.hash = hash;
        this.key = key;
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

    public byte[] getKey()
    {
        return key;
    }

    public void setKey(byte[] key)
    {
        this.key = key;
    }
}
