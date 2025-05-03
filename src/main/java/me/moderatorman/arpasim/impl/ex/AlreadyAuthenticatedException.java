package me.moderatorman.arpasim.impl.ex;

public class AlreadyAuthenticatedException extends Exception
{
    public AlreadyAuthenticatedException()
    {
        super("Already authenticated");
    }
}
