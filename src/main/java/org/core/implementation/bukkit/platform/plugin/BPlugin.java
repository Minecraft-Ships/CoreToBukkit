package org.core.implementation.bukkit.platform.plugin;

import org.core.platform.plugin.Plugin;
import org.core.platform.plugin.details.PluginVersion;
import org.jetbrains.annotations.NotNull;

public class BPlugin implements Plugin {

    protected final @NotNull org.bukkit.plugin.Plugin plugin;

    public BPlugin(@NotNull org.bukkit.plugin.Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getPluginName() {
        return this.plugin.getName();
    }

    @Override
    public @NotNull String getPluginId() {
        return this.plugin.getName().toLowerCase().replaceAll(" ", "_");
    }

    @Override
    public @NotNull PluginVersion getPluginVersion() {
        return new BPluginVersion(() -> this.plugin.getDescription().getVersion());
    }

    @Override
    public org.bukkit.plugin.@NotNull Plugin getPlatformLauncher() {
        return this.plugin;
    }
}
