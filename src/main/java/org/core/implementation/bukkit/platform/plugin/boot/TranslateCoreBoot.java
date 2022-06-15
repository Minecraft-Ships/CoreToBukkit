package org.core.implementation.bukkit.platform.plugin.boot;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.command.CommandRegister;
import org.core.implementation.bukkit.CoreToBukkit;
import org.core.implementation.bukkit.command.BCommand;
import org.core.implementation.bukkit.command.BCommandWrapper;
import org.core.implementation.bukkit.platform.plugin.loader.CoreBukkitPluginWrapper;
import org.core.implementation.paper.CoreToPaper;
import org.core.platform.plugin.CorePlugin;
import org.core.platform.plugin.loader.CommonLoad;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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
        }
        this.core = core;
    }

    @Override
    public void onDisable() {
        this.plugins.forEach(CorePlugin::onShutdown);
    }

    @Override
    public void onLoad() {
        Optional<Class<? extends CorePlugin>> opLauncher = TranslateCore.getStandAloneLauncher();
        if (opLauncher.isPresent()) {
            Class<? extends CorePlugin> pluginClass = opLauncher.get();
            CorePlugin plugin = CommonLoad.loadStandAlonePlugin(pluginClass);
            plugin.onConstruct(this);
            PluginManager pluginManager = Bukkit.getPluginManager();
            if (pluginManager instanceof SimplePluginManager) {
                SimplePluginManager spm = (SimplePluginManager) pluginManager;
                try {
                    CommandMap map = this.getFromField(spm, "commandMap", CommandMap.class);
                    Map<String, Plugin> lookup = this.getFromField(spm, "lookupNames", (Class<Map<String, Plugin>>) (Object) Map.class);
                    lookup.put(plugin.getPluginName(), (Plugin) plugin.getPlatformLauncher());

                    CommandRegister cmdReg = new CommandRegister();
                    plugin.onRegisterCommands(cmdReg);
                    cmdReg.getCommands().forEach(commandLauncher -> {
                        BCommandWrapper command = new BCommandWrapper(new BCommand(commandLauncher));
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
    public void onEnable() {
        this.core.init2(this);
        this.plugins.forEach(plugin -> {
            plugin.onCoreReady();
            Bukkit.getScheduler().runTask((Plugin) plugin.getPlatformLauncher(), plugin::onCoreFinishedInit);
        });
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
                CommandMap map = this.getFromField(spm, "commandMap", CommandMap.class);
                List<Plugin> spmPlugins = this.getFromField(spm, "plugins",
                        (Class<? extends List<Plugin>>) (Object) List.class);
                Map<String, Plugin> lookup = this.getFromField(spm, "lookupNames", (Class<Map<String, Plugin>>) (Object) Map.class);
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

        TranslateCore.getConsole().sendMessage(AText.ofPlain("SimplePluginManager was not used or a error occurred above. Plugins will not be treated as first party -> this may break compatibility"));
        plugins.parallelStream().forEach(plugin -> plugin.onConstruct(TranslateCoreBoot.this));
        return plugins;
    }
}
