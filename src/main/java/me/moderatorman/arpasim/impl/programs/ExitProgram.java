package me.moderatorman.arpasim.impl.programs;

import me.moderatorman.arpasim.util.IProgramIO;

public class ExitProgram extends AbstractProgram
{
    public ExitProgram()
    {
        super("exit", "Disconnect from the server", "", false);
    }

    @Override
    public void execute(IProgramIO ioHandler, String[] args)
    {
        ioHandler.println("Come back soon! You will now be disconnected.");
        ioHandler.flush();
        ioHandler.disconnect();
        //TODO: session cleanup?
    }
}
