package org.core.implementation.bukkit.platform;

import org.bukkit.Bukkit;
import org.core.platform.PlatformDetails;
import org.core.platform.plugin.details.CorePluginVersion;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class BPlatformDetails implements PlatformDetails {

    //git-Paper-370 (MC: 1.19.3)

    private String type;
    private Integer version;

    public BPlatformDetails() {
        String versionString = Bukkit.getVersion();
        String[] versionSplit = versionString.split("-");
        this.type = versionSplit[1];
        if (this.type.equals("Paper")) {
            this.version = Integer.parseInt(versionSplit[2].substring(0, versionSplit[2].indexOf(" ")));
            return;
        }
        try {
            this.version = Integer.parseInt(versionSplit[0]);
        } catch (Throwable e) {
            System.err.println(
                    "Unknown version number for implementation: Report this on dev.bukkit asking for support for "
                            + this.type);
        }
    }

    @Override
    public String getName() {
        return this.type;
    }

    @Override
    public String getIdName() {
        return this.type.toLowerCase();
    }

    @Override
    public @NotNull CorePluginVersion getVersion() {
        String versionString = Bukkit.getBukkitVersion();
        String version = versionString.substring(0, versionString.indexOf("-"));
        String[] splitVersion = version.split(Pattern.quote("."));
        int major = 0;
        int minor = 0;
        int patch = 0;
        if (splitVersion.length >= 1) {
            major = Integer.parseInt(splitVersion[0]);
        }
        if (splitVersion.length >= 2) {
            minor = Integer.parseInt(splitVersion[1]);
        }
        if (splitVersion.length >= 3) {
            patch = Integer.parseInt(splitVersion[2]);
        }
        return new CorePluginVersion(major, minor, patch, this.type, this.version);
    }
}
