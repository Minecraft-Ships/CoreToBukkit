package org.ships.implementation.bukkit;

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
import org.ships.implementation.bukkit.configuration.YAMLConfigurationFile;
import org.ships.implementation.bukkit.event.BEventManager;
import org.ships.implementation.bukkit.event.BukkitListener;
import org.ships.implementation.bukkit.platform.BServer;
import org.ships.implementation.bukkit.platform.BukkitPlatform;
import org.ships.implementation.bukkit.platform.PlatformConsole;
import org.ships.implementation.bukkit.scheduler.BSchedulerBuilder;
import org.ships.implementation.bukkit.text.BText;
import org.ships.implementation.bukkit.world.boss.BServerBossBar;

import java.io.File;

public class CoreToBukkit extends TranslateCore.CoreImplementation {

    protected BukkitPlatform platform = new BukkitPlatform();
    protected BEventManager manager = new BEventManager();
    protected BServer server = new BServer();
    protected PlatformConsole console = new PlatformConsole();

    public CoreToBukkit(JavaPlugin plugin) {
        init(plugin);
    }

    private void init(JavaPlugin plugin) {
        CoreImplementation.IMPLEMENTATION = this;
        Bukkit.getPluginManager().registerEvents(new BukkitListener(), plugin);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, getRawServer().getTPSExecutor(), 0, 1);
        this.platform.init();
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
        if (file == null) {
            throw new IllegalStateException("File cannot be null");
        }
        if (type == null) {
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
