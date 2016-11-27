package com.locator.child.secure.application;

public class WrongPasswordSizeException extends Exception
{
    private int length;

    public WrongPasswordSizeException(int length)
    {
        this.length = length;
    }

    @Override
    public String getMessage()
    {
        return "Password must have 16 characters. Yours have " + length + ".";
    }
}
