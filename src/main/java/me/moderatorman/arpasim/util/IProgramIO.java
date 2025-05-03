package me.moderatorman.arpasim.util;

public interface IProgramIO
{
    void print(String msg);
    void println(String msg);
    void flush();

    String getIP();
    void disconnect();
}
