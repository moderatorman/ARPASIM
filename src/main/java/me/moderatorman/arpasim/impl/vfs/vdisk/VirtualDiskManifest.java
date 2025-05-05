package me.moderatorman.arpasim.impl.vfs.vdisk;

import java.util.ArrayList;

public class VirtualDiskManifest
{
    public ArrayList<VDiskManifestEntry> files;

    public static class VDiskManifestEntry
    {
        public String realName;
        public String uuid;
        public int uncompressedSize;
        public CompressionType compressionType;

        public VDiskManifestEntry(String realName, String uuid, int uncompressedSize, CompressionType compressionType)
        {
            this.realName = realName;
            this.uuid = uuid;
            this.uncompressedSize = uncompressedSize;
            this.compressionType = compressionType;
        }
    }
}
