package org.core.implementation.bukkit.platform.structure;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.structure.StructureManager;
import org.core.implementation.bukkit.world.structure.BStructure;
import org.core.world.structure.Structure;
import org.core.world.structure.StructureBuilder;

import java.util.stream.Collectors;

public class BukkitStructurePlatform {

    private final StructureManager manager;

    public BukkitStructurePlatform() {
        this.manager = Bukkit.getStructureManager();
    }

    public Structure register(StructureBuilder builder) {
        String key = builder.getId();
        if (key==null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (builder.getPlugin()==null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        org.bukkit.plugin.Plugin plugin = (org.bukkit.plugin.Plugin) builder.getPlugin().getPlatformLauncher();
        NamespacedKey namespaceKey = NamespacedKey.fromString(key, plugin);
        if (namespaceKey==null) {
            throw new IllegalStateException("Namespace created was null");
        }

        org.bukkit.structure.Structure structure = this.manager.createStructure();
        structure
                .getPalettes()
                .addAll(builder
                        .getBlocks()
                        .stream()
                        .map(PaletteStructure::new)
                        .collect(Collectors.toList()));

        this.manager.registerStructure(namespaceKey, structure);
        return new BStructure(structure);
    }
}
