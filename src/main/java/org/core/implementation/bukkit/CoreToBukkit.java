package org.core.implementation.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.core.TranslateCore;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationStream;
import org.core.event.EventManager;
import org.core.platform.Platform;
import org.core.platform.PlatformServer;
import org.core.schedule.SchedulerBuilder;
import org.core.source.command.ConsoleSource;
import org.core.text.Text;
import org.core.world.boss.ServerBossBar;
import org.core.implementation.bukkit.configuration.YAMLConfigurationFile;
import org.core.implementation.bukkit.event.BEventManager;
import org.core.implementation.bukkit.event.BukkitListener;
import org.core.implementation.bukkit.platform.BServer;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.platform.PlatformConsole;
import org.core.implementation.bukkit.scheduler.BSchedulerBuilder;
import org.core.implementation.bukkit.text.BText;
import org.core.implementation.bukkit.world.boss.BServerBossBar;

import java.io.File;

public class CoreToBukkit extends TranslateCore.CoreImplementation {

    protected BukkitPlatform platform = new BukkitPlatform();
    protected BEventManager manager = new BEventManager();
    protected BServer server = new BServer();
    protected PlatformConsole console = new PlatformConsole();

    public CoreToBukkit() {
        init();
    }

    private void init() {
        CoreImplementation.IMPLEMENTATION = this;
        this.platform.init();

    }

    public void init2(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new BukkitListener(), plugin);
    }

    @Override
    public Platform getRawPlatform() {
        return this.platform;
    }

    @Override
    public EventManager getRawEventManager() {
        return this.manager;
    }

    @Override
    public ConsoleSource getRawConsole() {
        return this.console;
    }

    @Override
    public SchedulerBuilder createRawSchedulerBuilder() {
        return new BSchedulerBuilder();
    }

    @Override
    public ConfigurationStream.ConfigurationFile createRawConfigurationFile(File file, ConfigurationFormat type) {
        if (file==null) {
            throw new IllegalStateException("File cannot be null");
        }
        if (type==null) {
            throw new IllegalStateException("ConfigurationFormat cannot be null");
        }
        boolean check = false;
        for (String fileExt : type.getFileType()) {
            if (file.getName().endsWith(fileExt)) {
                check = true;
            }
        }
        if (!check) {
            return null;
        }
        if (type.equals(ConfigurationFormat.FORMAT_YAML)) {
            return new YAMLConfigurationFile(file);
        }
        System.err.println("ConfigurationFormat is not supported: " + type.getName());
        return null;
    }

    @Override
    public PlatformServer getRawServer() {
        return this.server;
    }

    @Override
    @Deprecated
    public Text textBuilder(String chars) {
        return new BText(chars);
    }

    @Override
    public ServerBossBar bossBuilder() {
        return new BServerBossBar();
    }
}