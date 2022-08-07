package org.core.implementation.bukkit.platform.plugin.loader;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Deprecated
public class CoreBukkitPluginLoader implements PluginLoader {

    @Override
    public @NotNull Plugin loadPlugin(@NotNull File file) throws InvalidPluginException, UnknownDependencyException {
        throw new RuntimeException("Will not be implemented");
    }

    @Override
    public @NotNull PluginDescriptionFile getPluginDescription(@NotNull File file) throws InvalidDescriptionException {
        throw new RuntimeException("Will not be implemented");
    }

    @Override
    public @NotNull Pattern[] getPluginFileFilters() {
        return new Pattern[0];
    }

    @Override
    public @NotNull Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(
            @NotNull Listener listener, @NotNull Plugin plugin) {
        return new HashMap<>();
    }

    @Override
    public void enablePlugin(@NotNull Plugin plugin) {

    }

    @Override
    public void disablePlugin(@NotNull Plugin plugin) {

    }
}
