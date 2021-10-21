package org.core.implementation.bukkit.platform.plugin;

import org.core.platform.plugin.details.PluginVersion;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class BPluginVersion implements PluginVersion {

    private final @NotNull Supplier<String> version;

    public BPluginVersion(@NotNull Supplier<String> version) {
        this.version = version;
    }

    @Override
    public @NotNull String asString() {
        return this.version.get();
    }
}
