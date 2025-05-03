package me.moderatorman.arpasim.impl.programs;

import me.moderatorman.arpasim.impl.ex.UserExistsException;
import me.moderatorman.arpasim.impl.managers.UserManager;
import me.moderatorman.arpasim.util.IProgramIO;

public class NewUserProgram extends AbstractProgram
{
    public NewUserProgram()
    {
        super("newuser", "Create a new user account", "NEWUSER <username> <password>");
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
            UserManager.createUser(args[0], args[1]);
            ioHandler.println("User " + args[0] + " created successfully!");
        } catch (UserExistsException e) {
            ioHandler.println("A user with that name already exists! Please try something else.");
        }
    }
}
