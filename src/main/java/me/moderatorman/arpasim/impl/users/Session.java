package me.moderatorman.arpasim.impl.users;

import java.util.UUID;

public class Session
{
    private User user;
    private String ipAddress;
    private String sessionID;
    private String currentDirectory = "/";
    private boolean active;

    public Session(User user, String ipAddress)
    {
        this.user = user;
        this.ipAddress = ipAddress;
        this.sessionID = UUID.nameUUIDFromBytes((ipAddress + "-" + System.currentTimeMillis()).getBytes()).toString();
        active = true; // it's active when it's brand new
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
        return sessionID.substring(0, 4) + sessionID.substring(sessionID.length() - 4);
    }

    public boolean isLoggedIn()
    {
        return user != null;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean flag)
    {
        active = flag;
    }
}
