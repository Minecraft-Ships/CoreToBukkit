package org.ships.implementation.bukkit.platform.plugin.boot;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.core.command.CommandRegister;
import org.core.platform.plugin.CorePlugin;
import org.core.platform.plugin.loader.CommonLoad;
import org.ships.implementation.bukkit.CoreToBukkit;
import org.ships.implementation.bukkit.command.BCommand;
import org.ships.implementation.bukkit.command.BCommandWrapper;
import org.ships.implementation.bukkit.platform.plugin.loader.CoreBukkitPluginWrapper;
import org.ships.implementation.paper.CoreToPaper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class TranslateCoreBoot extends JavaPlugin {

    private final SortedSet<CorePlugin> plugins = new TreeSet<>();
    private final CoreToBukkit core;

    public TranslateCoreBoot() {
        CoreToBukkit core;
        try {
            Class.forName("com.destroystokyo.paper.event.block.BlockDestroyEvent");
            core = new CoreToPaper();
        } catch (ClassNotFoundException e) {
            core = new CoreToBukkit();
        }
        this.core = core;
    }

    @Override
    public void onLoad() {
        File folder = core.getRawPlatform().getTranslatePluginsFolder();
        plugins.addAll(loadPlugins(folder));
    }

    @Override
    public void onEnable() {
        core.init2(this);
        plugins.forEach(org.core.platform.plugin.Plugin::onCoreReady);
    }

    private <T> T getFromField(Object from, String field, Class<T> type) throws NoSuchFieldException, IllegalAccessException {
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
        if (files==null) {
            return Collections.emptyList();
        }
        List<CorePlugin> plugins = CommonLoad.loadPlugin(this.getClassLoader(), files);
        PluginManager pluginManager = Bukkit.getPluginManager();
        if (pluginManager instanceof SimplePluginManager) {
            Set<CoreBukkitPluginWrapper> bukkitPlugins = plugins.parallelStream().map(CoreBukkitPluginWrapper::new).collect(Collectors.toSet());
            SimplePluginManager spm = (SimplePluginManager) pluginManager;
            try {
                CommandMap map = getFromField(spm, "commandMap", CommandMap.class);
                List<Plugin> spmPlugins = getFromField(spm, "plugins", (Class<? extends List<Plugin>>) (Object) List.class);
                Map<String, Plugin> lookup = getFromField(spm, "lookupNames", (Class<Map<String, Plugin>>) (Object) Map.class);
                spmPlugins.addAll(bukkitPlugins);
                lookup.putAll(spmPlugins.stream().collect(Collectors.toMap(Plugin::getName, (plugin) -> plugin)));

                bukkitPlugins.forEach(plugin -> {
                    CommandRegister cmdReg = new CommandRegister();
                    plugin.getPlugin().onRegisterCommands(cmdReg);
                    cmdReg.getCommands().forEach(commandLauncher -> {
                        BCommandWrapper command = new BCommandWrapper(new BCommand(commandLauncher));
                        map.register(commandLauncher.getName(), command);
                    });
                });
                bukkitPlugins.parallelStream().forEach(plugin ->
                        plugin.getPlugin().onConstruct(plugin)
                );
                return plugins;
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        System.err.println("SimplePluginManager was not used or a error occurred above. Plugins will not be treated as first party -> this may break compatibility");
        plugins.parallelStream().forEach(plugin -> plugin.onConstruct(TranslateCoreBoot.this));
        return plugins;
    }
}
