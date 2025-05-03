package me.moderatorman.arpasim.impl.managers;

import com.google.gson.Gson;
import me.moderatorman.arpasim.impl.ex.AlreadyAuthenticatedException;
import me.moderatorman.arpasim.impl.ex.UserExistsException;
import me.moderatorman.arpasim.impl.ex.UserNotFoundException;
import me.moderatorman.arpasim.impl.users.Session;
import me.moderatorman.arpasim.impl.users.User;
import me.moderatorman.arpasim.util.PasswordUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class UserManager
{
    private static final Gson gson = new Gson();
    private static ArrayList<Session> activeSessions = new ArrayList<>();

    public static void createUser(String name, String password) throws UserExistsException
    {
        User user = new User(name, password);

        File file = new File("users/" + name + ".json");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (file.exists()) throw new UserExistsException();

        try (FileWriter writer = new FileWriter(file))
        {
            writer.write(gson.toJson(user));
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static boolean authenticateUser(String name, String password, String ipAddress) throws UserNotFoundException, AlreadyAuthenticatedException
    {
        File file = new File("users/" + name + ".json");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) throw new UserNotFoundException();
        try (FileReader reader = new FileReader(file))
        {
            User user = gson.fromJson(reader, User.class);
            if (PasswordUtil.checkPassword(password, user.getHashedPassword()))
            {
                Session session = getSession(ipAddress);
                if (session.isLoggedIn()) throw new AlreadyAuthenticatedException();
                if (session != null) session.setUser(user);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    public static void createSession(String ipAddress)
    {
        activeSessions.add(new Session(null, ipAddress));
    }

    public static Session getSession(String ipAddress)
    {
        for (Session session : activeSessions)
            if (session.getIP().equals(ipAddress))
                return session;
        return null;
    }

    public static int getActiveSessionCount()
    {
        int count = 0;
        for (Session session : activeSessions)
            if (session.isActive())
                count++;
        return count;
    }
}
