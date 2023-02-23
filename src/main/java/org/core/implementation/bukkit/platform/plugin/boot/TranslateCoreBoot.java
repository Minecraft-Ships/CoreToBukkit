package org.core.implementation.bukkit.platform.plugin.boot;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.command.CommandRegister;
import org.core.command.commands.TranslateCoreCommands;
import org.core.command.commands.timings.TimingsCommand;
import org.core.implementation.bukkit.CoreToBukkit;
import org.core.implementation.bukkit.command.BCommand;
import org.core.implementation.bukkit.command.BCommandWrapper;
import org.core.implementation.bukkit.logger.BJavaLogger;
import org.core.implementation.bukkit.logger.BSLF4JLogger;
import org.core.implementation.bukkit.platform.plugin.loader.CoreBukkitPluginWrapper;
import org.core.implementation.paper.CoreToPaper;
import org.core.logger.Logger;
import org.core.platform.plugin.CorePlugin;
import org.core.platform.plugin.loader.CommonLoad;
import org.core.schedule.Scheduler;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class TranslateCoreBoot extends JavaPlugin {

    private final Collection<CorePlugin> plugins = new TreeSet<>();
    private final CoreToBukkit core;

    public TranslateCoreBoot() {
        CoreToBukkit core;
        try {
            Class.forName("com.destroystokyo.paper.event.block.BlockDestroyEvent");
            core = new CoreToPaper();
        } catch (ClassNotFoundException e) {
            core = new CoreToBukkit();
            this.getLogger().warning("Paper was not detected. ");
            this.getLogger().warning("Translate Core will be dropping support for none Paper servers due to Paper's hard-fork.");
            this.getLogger().warning("While the Paper hard-fork is not yet out, signs are showing of it coming soon.");
        }
        this.core = core;
    }

    @Override
    public void onLoad() {
        Optional<Class<? extends CorePlugin>> opLauncher = TranslateCore.getStandAloneLauncher();
        if (opLauncher.isPresent()) {
            Class<? extends CorePlugin> pluginClass = opLauncher.get();
            CorePlugin plugin = CommonLoad.loadStandAlonePlugin(pluginClass);

            Logger logger;
            try {
                Method method = JavaPlugin.class.getMethod("getSLF4JLogger");
                logger = new BSLF4JLogger(method.invoke(this));
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                logger = new BJavaLogger(this.getLogger());
            }

            plugin.onConstruct(this, logger);
            PluginManager pluginManager = Bukkit.getPluginManager();
            if (pluginManager instanceof SimplePluginManager spm) {
                try {
                    CommandMap map = this.getFromField(spm, "commandMap");
                    Map<String, Plugin> lookup = this.getFromField(spm, "lookupNames");
                    lookup.put(plugin.getPluginName(), (Plugin) plugin.getPlatformLauncher());


                    map.register("TranslateCore",
                                 new BCommandWrapper(new BCommand(new TranslateCoreCommands(new TimingsCommand()))));


                    CommandRegister cmdReg = new CommandRegister();
                    plugin.onRegisterCommands(cmdReg);
                    cmdReg.getCommands().forEach(commandLauncher -> {
                        Command command = new BCommandWrapper(new BCommand(commandLauncher));
                        map.register(commandLauncher.getName(), command);
                    });
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            this.plugins.add(plugin);
            return;
        }
        File folder = this.core.getRawPlatform().getTranslatePluginsFolder();
        this.plugins.addAll(this.loadPlugins(folder));
    }

    @Override
    public void onDisable() {
        this.plugins.forEach(CorePlugin::onShutdown);
        TranslateCore
                .getScheduleManager()
                .getSchedules()
                .parallelStream()
                .filter(sch -> sch instanceof Scheduler.Native)
                .forEach(sch -> ((Scheduler.Native) sch).cancel());

    }

    @Override
    public void onEnable() {
        this.core.init2(this);
        this.plugins.forEach(plugin -> {
            plugin.onCoreReady();
            Bukkit.getScheduler().runTask((Plugin) plugin.getPlatformLauncher(), plugin::onCoreFinishedInit);
        });
    }

    private <T> T getFromField(Object from, String field) throws NoSuchFieldException, IllegalAccessException {
        Field jField = from.getClass().getDeclaredField(field);
        jField.setAccessible(true);
        return (T) jField.get(from);
    }

    private List<CorePlugin> loadPlugins(File folder) {
        if (!folder.exists()) {
            try {
                Files.createDirectories(folder.toPath());
            } catch (IOException e) {
                return Collections.emptyList();
            }
        }
        File[] files = folder.listFiles();
        if (files == null) {
            return Collections.emptyList();
        }
        List<CorePlugin> plugins = CommonLoad.loadPlugin(this.getClassLoader(), files);
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager instanceof SimplePluginManager spm) {
            Set<CoreBukkitPluginWrapper> bukkitPlugins = plugins
                    .parallelStream()
                    .map(CoreBukkitPluginWrapper::new)
                    .collect(Collectors.toSet());
            try {
                CommandMap map = this.getFromField(spm, "commandMap");
                List<Plugin> spmPlugins = this.getFromField(spm, "plugins");
                Map<String, Plugin> lookup = this.getFromField(spm, "lookupNames");
                spmPlugins.addAll(bukkitPlugins);
                lookup.putAll(spmPlugins.stream().collect(Collectors.toMap(Plugin::getName, (plugin) -> plugin)));

                bukkitPlugins.forEach(plugin -> {
                    CommandRegister cmdReg = new CommandRegister();
                    plugin.getPlugin().onRegisterCommands(cmdReg);
                    cmdReg.getCommands().forEach(commandLauncher -> {
                        Command command = new BCommandWrapper(new BCommand(commandLauncher));
                        map.register(commandLauncher.getName(), command);
                    });
                });
                bukkitPlugins
                        .parallelStream()
                        .forEach(plugin -> plugin.getPlugin().onConstruct(plugin, new BJavaLogger(plugin.getLogger())));
                return plugins;
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        TranslateCore
                .getConsole()
                .sendMessage(AText.ofPlain(
                        "SimplePluginManager was not used or a error occurred above. Plugins will not be treated as "
                                + "first party -> this may break compatibility"));
        plugins.parallelStream().forEach(plugin -> plugin.onConstruct(TranslateCoreBoot.this));
        return plugins;
    }
}
