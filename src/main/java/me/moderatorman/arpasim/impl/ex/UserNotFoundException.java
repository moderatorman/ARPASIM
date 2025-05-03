package me.moderatorman.arpasim.impl.ex;

public class UserNotFoundException extends Exception
{
    public UserNotFoundException()
    {
        super("User not found");
    }
}
