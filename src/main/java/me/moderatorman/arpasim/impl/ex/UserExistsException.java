package me.moderatorman.arpasim.impl.ex;

public class UserExistsException extends Exception
{
    public UserExistsException()
    {
        super("User already exists");
    }
}
