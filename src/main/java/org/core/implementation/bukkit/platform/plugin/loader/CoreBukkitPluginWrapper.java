package org.core.implementation.bukkit.platform.plugin.loader;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.core.platform.plugin.CorePlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class CoreBukkitPluginWrapper implements org.bukkit.plugin.Plugin {

    private final CorePlugin plugin;

    public CoreBukkitPluginWrapper(CorePlugin plugin) {
        this.plugin = plugin;
    }

    public CorePlugin getPlugin() {
        return this.plugin;
    }

    @NotNull
    @Override
    public File getDataFolder() {
        return this.plugin.getConfigFolder();
    }

    @NotNull
    @Override
    public PluginDescriptionFile getDescription() {
        String yamlMap = "name: " + this.getPlugin().getPluginName() + "\n" + "version: " + this
                .getPlugin()
                .getPluginVersion()
                .asString() + "\n" + "main: " + this.getPlugin().getPlatformLauncher().getClass().getName();
        StringReader reader = new StringReader(yamlMap);
        try {
            return new PluginDescriptionFile(reader);
        } catch (InvalidDescriptionException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull PluginDescriptionFile getPluginMeta() {
        return this.getDescription();
    }

    @NotNull
    @Override
    @Deprecated
    public FileConfiguration getConfig() {
        throw new RuntimeException("Not FileConfiguration implementation");
    }

    @Nullable
    @Override
    public InputStream getResource(@NotNull String filename) {
        return this.plugin.getResource(filename).orElse(null);
    }

    @Override
    @Deprecated
    public void saveConfig() {
        throw new RuntimeException("Not FileConfiguration implementation");
    }

    @Override
    @Deprecated
    public void saveDefaultConfig() {
        throw new RuntimeException("Not FileConfiguration implementation");

    }

    @Override
    @Deprecated
    public void saveResource(@NotNull String resourcePath, boolean replace) {

    }

    @Override
    @Deprecated
    public void reloadConfig() {
        throw new RuntimeException("Not FileConfiguration implementation");
    }

    @NotNull
    @Override
    public CoreBukkitPluginLoader getPluginLoader() {
        return new CoreBukkitPluginLoader();
    }

    @NotNull
    @Override
    @Deprecated
    public Server getServer() {
        return Bukkit.getServer();
    }

    @Override
    @Deprecated
    public boolean isEnabled() {
        return true;
    }

    @Override
    @Deprecated
    public void onDisable() {
        throw new RuntimeException("Cannot be disabled");
    }

    @Override
    @Deprecated
    public void onLoad() {
        throw new RuntimeException("Cannot be reloaded");
    }

    @Override
    @Deprecated
    public void onEnable() {
        throw new RuntimeException("Cannot be reloaded");

    }

    @Override
    @Deprecated
    public boolean isNaggable() {
        return false;
    }

    @Override
    @Deprecated
    public void setNaggable(boolean canNag) {
        throw new RuntimeException("Cannot be naggable");

    }

    @Nullable
    @Override
    @Deprecated
    public ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        throw new RuntimeException("No World Generator");
    }

    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull String s, @Nullable String s1) {
        throw new RuntimeException("No Biome Provider");
    }

    @NotNull
    @Override
    public Logger getLogger() {
        return Logger.getLogger(this.plugin.getPluginName());
    }

    @NotNull
    @Override
    public String getName() {
        return this.plugin.getPluginName();
    }

    @Override
    @Deprecated
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
        return false;
    }

    @Nullable
    @Override
    @Deprecated
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String alias,
                                      @NotNull String[] args) {
        return Collections.emptyList();
    }
}
