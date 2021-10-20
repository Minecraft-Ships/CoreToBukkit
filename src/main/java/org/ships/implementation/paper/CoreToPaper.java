package org.ships.implementation.paper;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.ships.implementation.bukkit.CoreToBukkit;
import org.ships.implementation.paper.event.PaperListener;

public class CoreToPaper extends CoreToBukkit {

    //random change

    @Override
    public void init2(JavaPlugin plugin) {
        super.init2(plugin);
        Bukkit.getPluginManager().registerEvents(new PaperListener(), plugin);

    }
}
