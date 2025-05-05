package me.moderatorman.arpasim.impl.programs;

import io.netty.buffer.Unpooled;
import me.moderatorman.arpasim.util.IProgramIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoreProgram extends AbstractProgram
{
    public MoreProgram()
    {
        super("more", "print contents of file", "MORE <file>", false);
    }

    @Override
    public void execute(IProgramIO ioHandler, String[] args)
    {
        if (args.length != 1)
        {
            ioHandler.println("Usage: " + getUsage());
            return;
        }

        ioHandler.writeAndFlush(Unpooled.wrappedBuffer(new byte[] {
                (byte) 255, (byte) 251, (byte) 1,  // IAC WILL ECHO
                (byte) 255, (byte) 251, (byte) 3   // IAC WILL SUPPRESS-GO-AHEAD
        }));

        String fileName = args[0];
        List<String> lines = new ArrayList<>();

        // Read from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                lines.add(line);
            }
        } catch (IOException e) {
            ioHandler.println("Error reading file: " + e.getMessage());
            return;
        }

        int currentLine = 0;
        int pageSize = 10; // Number of initial lines to display TODO: get this from the terminal size

        while (true)
        {
            // Clear the screen and display the current page
            ioHandler.print("\033[H\033[2J"); // ANSI escape sequence to clear the screen
            for (int i = 0; i < pageSize && currentLine + i < lines.size(); i++)
            {
                ioHandler.println(lines.get(currentLine + i));
            }
            ioHandler.println("-- Press UP/DOWN to scroll, Q to quit --");
            ioHandler.flush();

            // Wait for user input
            System.out.println("About to wait for input from MoreProgram...");
            String input = ioHandler.readInput();
            System.out.println("MoreProgram received input: " + input);

            if (input.equalsIgnoreCase("q"))
            {
                break;
            }
            else if (input.equals("B")) // Down arrow
            {
                if (currentLine + pageSize < lines.size())
                    currentLine += 1;
            }
            else if (input.equals("A")) // Up arrow
            {
                if (currentLine - pageSize >= 0)
                    currentLine -= 1;
            } else {
                System.out.println("Received invalid character input on MoreProgram: " + input);
            }
        }
    }
}
