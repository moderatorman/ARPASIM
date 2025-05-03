package me.moderatorman.arpasim.impl.programs;

import me.moderatorman.arpasim.impl.managers.ProgramManager;
import me.moderatorman.arpasim.util.IProgramIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class LoginBannerProgram extends AbstractProgram
{
    private static final String CONFIG_FILE = "loginbanner.properties";

    public LoginBannerProgram()
    {
        super("loginbanner", "Displays the login banner", "", true);
    }

    @Override
    public void execute(IProgramIO ioHandler, String[] args)
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy");

        String time = now.format(timeFormatter);
        String date = now.format(dateFormatter);

        // Fetch non-hidden commands from the ProgramManager
        ArrayList<AbstractProgram> visiblePrograms = ProgramManager.getVisiblePrograms();

        // Use a Set to ensure unique program names
        Set<String> uniqueProgramNames = new LinkedHashSet<>();
        for (AbstractProgram program : visiblePrograms) {
            uniqueProgramNames.add(program.getName());
        }

        // Format commands into a grid
        StringBuilder commandsGrid = new StringBuilder();
        int columnCount = 4; // Number of columns in the grid
        int currentColumn = 0;

        for (String programName : uniqueProgramNames) {
            commandsGrid.append(String.format("%-15s", programName)); // Adjust spacing as needed
            currentColumn++;
            if (currentColumn == columnCount) {
                commandsGrid.append("\n");
                currentColumn = 0;
            }
        }

        if (currentColumn != 0)
            commandsGrid.append("\n"); // Add a newline if the last row is incomplete

        int userCount = 1; //TODO: track online users

        String banner = String.format(
                "It is %s on %s in %s.\n" +
                        "There are %s local user(s) currently connected.\n\n" +
                        "May the command line live forever.\n\n" +
                        "Command, one of the following:\n%s\n" +
                        "More commands available after login Type HELP for a detailed command list.\n" +
                        "Type NEWUSER to create an account. Press control-C to interrupt any command.",
                time, date, getServerLocation(), userCount, commandsGrid
        );

        ioHandler.println(banner);
        ioHandler.flush();
    }

    private String getServerLocation()
    {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE))
        {
            properties.load(input);
        } catch (IOException e) {
            System.out.println("loginbanner.properties does not exist. Default location will be used (Mountain View, California, USA).");
        }

        String city = properties.getProperty("city", "Mountain View");
        String region = properties.getProperty("region", "California");
        String country = properties.getProperty("country", "USA");
        return String.format("%s, %s, %s", city, region, country);
    }
}