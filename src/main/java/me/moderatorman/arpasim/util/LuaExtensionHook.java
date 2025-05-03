package me.moderatorman.arpasim.util;

import java.util.HashMap;

public interface LuaExtensionHook
{
    HashMap<String, Object> getExtendedGlobals();
}
