package me.moderatorman.arpasim.impl.programs;

import me.moderatorman.arpasim.impl.managers.ProgramManager;
import me.moderatorman.arpasim.util.IProgramIO;

public class HelpProgram extends AbstractProgram
{
    public HelpProgram()
    {
        super("help", "Access a full list of available commands", "");
    }

    @Override
    public void execute(IProgramIO ioHandler, String[] args)
    {
        ioHandler.println("Available commands (including hidden):");
        for (AbstractProgram program : ProgramManager.getAllPrograms())
        {
            ioHandler.println(" - " + program.getName() + ": " + program.getDescription());
        }
        ioHandler.flush();
    }
}
