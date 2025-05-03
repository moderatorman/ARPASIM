package me.moderatorman.arpasim.impl.users;

import me.moderatorman.arpasim.util.PasswordUtil;

import java.util.ArrayList;

public class User
{
    private String name;
    private String hashedPassword;
    private ArrayList<Badge> badges = new ArrayList<>();

    public User(String name, String rawPassword)
    {
        this.name = name;
        this.hashedPassword = PasswordUtil.hash(rawPassword);
    }

    public String getName()
    {
        return name;
    }

    public String getHashedPassword()
    {
        return hashedPassword;
    }

    public ArrayList<Badge> getBadges()
    {
        return badges;
    }
}
