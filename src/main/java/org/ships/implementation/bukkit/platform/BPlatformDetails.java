package org.ships.implementation.bukkit.platform;

import org.bukkit.Bukkit;
import org.core.platform.PlatformDetails;
import org.core.platform.plugin.details.CorePluginVersion;

public class BPlatformDetails implements PlatformDetails {
    @Override
    public String getName() {
        return "Bukkit";
    }

    @Override
    public String getIdName() {
        return "bukkit";
    }

    @Override
    public CorePluginVersion getVersion() {
        String versionString = Bukkit.getBukkitVersion();
        throw new RuntimeException("NotImplementedYet - Version: " + versionString);
    }
}
