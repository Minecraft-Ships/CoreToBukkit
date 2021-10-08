package org.ships.implementation.bukkit.platform;

import org.core.command.CommandRegister;
import org.core.config.ConfigurationStream;
import org.core.platform.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.ships.implementation.bukkit.configuration.YAMLConfigurationFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;

public class BPlugin implements Plugin {

    protected org.bukkit.plugin.Plugin plugin;

    public BPlugin(org.bukkit.plugin.Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getPluginName() {
        return this.plugin.getName();
    }

    @Override
    public String getPluginId() {
        return this.plugin.getName().toLowerCase().replaceAll(" ", "_");
    }

    @Override
    public @NotNull String getPluginVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    @Deprecated
    public void onRegisterCommands(@NotNull CommandRegister register) {

    }

    @Override
    public void registerPlugin() {

    }

    @Override
    public void onCoreReady() {

    }

    @Override
    public org.bukkit.plugin.@NotNull Plugin getPlatformLauncher() {
        return this.plugin;
    }

    @Override
    public Optional<ConfigurationStream.ConfigurationFile> createConfig(String configName, File file) {
        InputStream stream = this.plugin.getResource(configName);
        if (stream == null) {
            System.err.println("Request for '" + configName + "' could not be found");
            return Optional.empty();
        }
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            Files.copy(stream, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return Optional.of(new YAMLConfigurationFile(file));
    }

}
