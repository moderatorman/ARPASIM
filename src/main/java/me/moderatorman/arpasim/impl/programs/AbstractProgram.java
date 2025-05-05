package me.moderatorman.arpasim.impl.programs;

import me.moderatorman.arpasim.util.IProgramIO;

public abstract class AbstractProgram
{
    private String label;
    private String description;
    private String usage;
    private boolean hidden = false;

    public AbstractProgram(String label, String description, String usage)
    {
        this(label, description, usage, false);
    }

    public AbstractProgram(String label, String description, String usage, boolean hidden)
    {
        this.label = label;
        this.description = description;
        this.usage = usage;
    }

    public void execute(IProgramIO ioHandler, String[] args) {}

    public String getLabel()
    {
        return label;
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
