package org.core.implementation.bukkit.platform.details;

import org.core.platform.PlatformDetails;
import org.core.platform.plugin.details.CorePluginVersion;
import org.jetbrains.annotations.NotNull;

public class BukkitTranslatePlatformDetails implements PlatformDetails {
    @Override
    public @NotNull String getName() {
        return "BukkitToCore";
    }

    @Override
    public @NotNull String getIdName() {
        return "bukkit_to_core";
    }

    @Override
    public @NotNull CorePluginVersion getVersion() {
        return new CorePluginVersion(1, 0, 0);
    }
}
