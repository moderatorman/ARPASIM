package me.moderatorman.arpasim.impl;

import me.moderatorman.arpasim.impl.programs.AbstractProgram;
import me.moderatorman.arpasim.impl.users.Badges;
import me.moderatorman.arpasim.impl.users.Session;
import me.moderatorman.arpasim.impl.users.User;
import me.moderatorman.arpasim.util.LuaExtensionHook;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class LuaLoader
{
    public static void loadExtensions(File luaExtensionsFolder, LuaExtensionHook luaExtensionHook)
    {
        boolean luaFolderSet = luaExtensionsFolder != null;
        File scriptFolder = luaFolderSet ? luaExtensionsFolder : new File("extensions");
        if (scriptFolder.exists() && scriptFolder.isDirectory())
        {
            File[] luaFiles = scriptFolder.listFiles((dir, name) -> name.endsWith(".lua"));
            if (luaFiles != null)
            {
                int successfulLoadCount = 0;
                for (File luaFile : luaFiles)
                {
                    try
                    {
                        Globals globals = JsePlatform.standardGlobals();

                        boolean hasLuaHook = luaExtensionHook != null;
                        HashMap<String, Object> hookExtensions = hasLuaHook ? luaExtensionHook.getExtendedGlobals() : new HashMap<>();

                        for (String key : hookExtensions.keySet())
                        {
                            Object value = hookExtensions.get(key);
                            globals.set(key, CoerceJavaToLua.coerce(value));
                        }

                        // Expose Java classes to Lua scripts
                        globals.set("HashMap", CoerceJavaToLua.coerce(HashMap.class));
                        globals.set("ArrayList", CoerceJavaToLua.coerce(ArrayList.class));
                        globals.set("AbstractProgram", CoerceJavaToLua.coerce(AbstractProgram.class));
                        globals.set("ProgramManager", CoerceJavaToLua.coerce(ProgramManager.class));
                        globals.set("User", CoerceJavaToLua.coerce(User.class));
                        globals.set("Session", CoerceJavaToLua.coerce(Session.class));
                        globals.set("Badges", CoerceJavaToLua.coerce(Badges.class));

                        LuaValue chunk = globals.loadfile(luaFile.getAbsolutePath());
                        chunk.call();
                        successfulLoadCount++;
                    } catch (Exception e) {
                        System.err.println("Failed to load Lua script: " + luaFile.getName());
                        e.printStackTrace(System.err);
                        System.exit(1);
                    }
                }

                System.out.println("Successfully loaded " + successfulLoadCount + " Lua script extension(s)!");
            }
        }
    }
}
