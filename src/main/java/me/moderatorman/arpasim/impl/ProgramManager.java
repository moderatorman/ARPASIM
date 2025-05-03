package me.moderatorman.arpasim.impl;

import me.moderatorman.arpasim.impl.programs.AbstractProgram;

import java.util.ArrayList;

public class ProgramManager
{
    private static final ArrayList<AbstractProgram> programs = new ArrayList<>();

    public static void registerProgram(AbstractProgram program)
    {
        programs.add(program);
    }

    public static void unregisterProgram(AbstractProgram program)
    {
        programs.remove(program);
    }

    public static AbstractProgram getProgram(String name)
    {
        for (AbstractProgram program : programs)
        {
            if (program.getName().equalsIgnoreCase(name))
            {
                return program;
            }
        }
        return null;
    }

    public static ArrayList<AbstractProgram> getAllPrograms()
    {
        return programs;
    }

    public static ArrayList<AbstractProgram> getVisiblePrograms()
    {
        ArrayList<AbstractProgram> visiblePrograms = new ArrayList<>();
        for (AbstractProgram program : programs)
        {
            if (!program.isHidden())
                visiblePrograms.add(program);
        }
        return visiblePrograms;
    }
}
