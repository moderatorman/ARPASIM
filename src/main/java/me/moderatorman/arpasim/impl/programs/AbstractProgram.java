package me.moderatorman.arpasim.impl.programs;

import me.moderatorman.arpasim.util.CommandExecutionPipe;
import me.moderatorman.arpasim.util.IProgramIO;

public abstract class AbstractProgram
{
    private String name;
    private String description;
    private String usage;
    private boolean hidden = false;
    private CommandExecutionPipe executionCallback;

    public AbstractProgram(String name, String description, String usage)
    {
        this(name, description, usage, false);
    }

    public AbstractProgram(String name, String description, String usage, boolean hidden)
    {
        this.name = name;
        this.description = description;
        this.usage = usage;
    }

    public void callback(CommandExecutionPipe executionCallback)
    {
        this.executionCallback = executionCallback;
    }

    public CommandExecutionPipe getExecutionCallback()
    {
        return executionCallback;
    }

    public void execute(IProgramIO ioHandler, String[] args)
    {
        if (executionCallback != null && args != null)
            executionCallback.flush(args);
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUsage()
    {
        return usage;
    }

    public boolean isHidden()
    {
        return hidden;
    }
}
