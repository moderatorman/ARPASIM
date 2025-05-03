package me.moderatorman.arpasim.impl.users;

import java.util.UUID;

public class Session
{
    private User user;
    private String ipAddress;
    private String sessionID;
    private String currentDirectory = "/";

    public Session(User user, String ipAddress)
    {
        this.user = user;
        this.ipAddress = ipAddress;
        this.sessionID = UUID.fromString(ipAddress + "-" + System.currentTimeMillis()).toString();
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getIP()
    {
        return ipAddress;
    }

    public String getID()
    {
        return sessionID;
    }

    public boolean isLoggedIn()
    {
        return user != null;
    }
}
