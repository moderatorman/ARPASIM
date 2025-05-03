package me.moderatorman.arpasim.impl.users;

public class Session
{
    private User user;
    private String ipAddress;
    private String sessionID;
    private String currentDirectory;

    public Session(User user, String ipAddress, String sessionID)
    {
        this.user = user;
        this.ipAddress = ipAddress;
        this.sessionID = sessionID;
    }

    public User getUser()
    {
        return user;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public String getSessionID()
    {
        return sessionID;
    }
}
