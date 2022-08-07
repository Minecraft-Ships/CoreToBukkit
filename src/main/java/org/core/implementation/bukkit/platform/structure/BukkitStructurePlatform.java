package org.core.implementation.bukkit.platform.structure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.structure.StructureManager;
import org.core.TranslateCore;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.implementation.bukkit.world.structure.BStructure;
import org.core.platform.plugin.Plugin;
import org.core.world.structure.Structure;
import org.core.world.structure.StructureBuilder;
import org.core.world.structure.StructureFileBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class BukkitStructurePlatform {

    private final StructureManager manager;
    private final Collection<Structure> coreStructures = new HashSet<>();

    public BukkitStructurePlatform() {
        this.manager = Bukkit.getStructureManager();
    }

    public Collection<Structure> getStructure() {
        Set<Structure> collection = this
                .manager
                .getStructures()
                .entrySet()
                .stream()
                .map(entry -> {
                    Plugin plugin = TranslateCore.getPlatform().getPlugin(entry.getKey().getNamespace()).orElse(null);
                    return new BStructure(entry.getValue(), entry.getKey().getKey(),
                            plugin, entry.getKey().getKey());
                }).collect(Collectors.toSet());
        collection.addAll(this.coreStructures);
        return collection;
    }

    public Structure register(StructureFileBuilder builder) throws IOException {
        org.bukkit.structure.Structure structure = this.manager.loadStructure(builder.getFile());
        Structure bStructure = new BStructure(structure, builder.getName(), builder.getPlugin(), builder.getKey());
        if (builder.getPlugin() != null && builder.getKey() != null) {
            this.manager.registerStructure(
                    new NamespacedKey(((org.bukkit.plugin.Plugin) builder.getPlugin().getPlatformLauncher()),
                            builder.getKey()),
                    structure);
        }
        this.coreStructures.add(bStructure);
        return bStructure;
    }

    public Structure register(StructureBuilder builder) {
        String key = builder.getId();
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        if (builder.getPlugin() == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        org.bukkit.plugin.Plugin plugin = (org.bukkit.plugin.Plugin) builder.getPlugin().getPlatformLauncher();
        NamespacedKey namespaceKey = NamespacedKey.fromString(key, plugin);
        if (namespaceKey == null) {
            throw new IllegalStateException("Namespace created was null");
        }

        if (builder.getMax() == null || builder.getMin() == null) {
            throw new IllegalArgumentException("Min and Max cannot be null");
        }
        World world = ((BWorldExtent) builder.getWorld()).getBukkitWorld();
        Location min = new Location(world, builder.getMin().getX(), builder.getMin().getY(), builder.getMin().getZ());
        Location max = new Location(world, builder.getMax().getX(), builder.getMax().getY(), builder.getMax().getZ());

        org.bukkit.structure.Structure structure = this.manager.createStructure();
        structure.fill(min, max, builder.isIncludeEntities());

        this.manager.registerStructure(namespaceKey, structure);
        Structure bStructure = new BStructure(structure, builder.getName(), builder.getPlugin(), key);
        this.coreStructures.add(bStructure);
        return bStructure;
    }
}
