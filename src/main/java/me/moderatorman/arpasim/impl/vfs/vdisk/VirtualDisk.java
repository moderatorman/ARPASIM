package me.moderatorman.arpasim.impl.vfs.vdisk;

import com.google.common.jimfs.Jimfs;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystem;
import java.util.zip.ZipFile;

public class VirtualDisk
{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String name;
    private FileSystem fs;
    private ZipFile zip;

    public VirtualDisk(String name, FileSystem fs)
    {
        this.name = name;
        this.fs = fs;
    }

    public void save()
    {
        //TODO: compress and write all jimfs files to zip

    }

    public VirtualDiskManifest readManifest()
    {
        if (zip == null)
            return null; // vdisk zip not open

        try
        {
            try (InputStream is = zip.getInputStream(zip.getEntry("manifest.json")))
            {
                return gson.fromJson(new InputStreamReader(is), VirtualDiskManifest.class);
            }
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public void open()
    {
        try
        {
            zip = new ZipFile("vfs/" + name + ".vdisk.zip");
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    public void close()
    {
        try
        {
            zip.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
