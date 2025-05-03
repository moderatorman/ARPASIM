package me.moderatorman.arpasim.impl.programs;

import me.moderatorman.arpasim.impl.ex.UserNotFoundException;
import me.moderatorman.arpasim.impl.managers.UserManager;
import me.moderatorman.arpasim.util.IProgramIO;

public class LoginProgram extends AbstractProgram
{
    public LoginProgram()
    {
        super("login", "Login to your user account", "LOGIN <username> <password>");
    }

    @Override
    public void execute(IProgramIO ioHandler, String[] args)
    {
        if (args.length != 2)
        {
            ioHandler.println("Usage: " + getUsage());
            return;
        }

        try
        {
            if (UserManager.authenticateUser(args[0], args[1], ioHandler.getIP()))
                ioHandler.println("Login successful! Welcome, " + args[0] + "!");
            else ioHandler.println("Login failed (invalid password)!");
        } catch (UserNotFoundException e) {
            ioHandler.println("User not found (names are case-sensitive!). Please try again.");
        }
    }
}
