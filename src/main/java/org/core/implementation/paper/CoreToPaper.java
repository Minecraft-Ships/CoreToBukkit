package org.core.implementation.paper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.core.implementation.bukkit.CoreToBukkit;
import org.core.implementation.paper.event.PaperListener;

public class CoreToPaper extends CoreToBukkit {

    //random change

    @Override
    public void init2(JavaPlugin plugin) {
        super.init2(plugin);
        Bukkit.getPluginManager().registerEvents(new PaperListener(), plugin);

    }
}
