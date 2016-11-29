package sirs.communication.request;

class FinalRequest extends Request
{
    private byte[] message;
    private byte[] key;

    public FinalRequest(byte[] message, byte[] key)
    {
        super("FinalRequest");
        this.message = message;
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

    public byte[] getKey()
    {
        return key;
    }

    public void setKey(byte[] key)
    {
        this.key = key;
    }
}
