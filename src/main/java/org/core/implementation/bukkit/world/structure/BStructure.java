package org.core.implementation.bukkit.world.structure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.util.BlockVector;
import org.core.TranslateCore;
import org.core.collection.BlockSetDetails;
import org.core.entity.EntitySnapshot;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.implementation.bukkit.world.position.block.details.blocks.BBlockDetails;
import org.core.platform.plugin.Plugin;
import org.core.vector.type.Vector3;
import org.core.world.WorldExtent;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.structure.Structure;
import org.core.world.structure.StructurePlacementBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BStructure implements Structure {

    public final @Nullable String name;
    private final @NotNull org.bukkit.structure.Structure structure;
    private final @Nullable String key;
    private final @Nullable Plugin plugin;

    public BStructure(@NotNull org.bukkit.structure.Structure structure, @Nullable String name, @Nullable Plugin plugin,
            @Nullable String key) {
        this.structure = structure;
        this.name = name;
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    public Optional<String> getId() {
        return Optional.ofNullable(this.key);
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    @Override
    public Optional<Plugin> getPlugin() {
        return Optional.ofNullable(this.plugin);
    }

    @Override
    public Set<EntitySnapshot<?>> getEntities() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Set<BlockSetDetails> getBlocks() {
        return this
                .structure
                .getPalettes()
                .stream()
                .map(palette -> palette
                        .getBlocks()
                        .stream()
                        .map(state -> new BBlockDetails(state.getBlockData(),
                                ((BukkitPlatform) TranslateCore.getPlatform())
                                        .createTileEntityInstance(state)
                                        .map(TileEntity::getSnapshot)
                                        .orElse(null)))
                        .collect(Collectors.toCollection(BlockSetDetails::new)))
                .collect(Collectors.toSet());
    }

    @Override
    public Vector3<Integer> getSize() {
        BlockVector vector = this.structure.getSize();
        return Vector3.valueOf(vector.getBlockX(), vector.getBlockY(), vector.getBlockZ());
    }

    @Override
    public void fillBetween(WorldExtent world, Vector3<Integer> start, Vector3<Integer> end) {
        World bukkitWorld = ((BWorldExtent) world).getBukkitWorld();
        Location pos1 = new Location(bukkitWorld, start.getX(), start.getY(), start.getZ());
        Location pos2 = new Location(bukkitWorld, end.getX(), end.getY(), end.getZ());
        this.structure.fill(pos1, pos2, true);
    }

    @Override
    public void place(StructurePlacementBuilder builder) {
        if (builder.getPosition() == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        World bukkitWorld = ((BWorldExtent) builder.getPosition().getWorld()).getBukkitWorld();
        Location loc = new Location(bukkitWorld, builder.getPosition().getX(), builder.getPosition().getY(),
                builder.getPosition().getZ());
        this.structure.place(loc, true, StructureRotation.NONE, Mirror.NONE, 0, 1, new SecureRandom());
    }

    @Override
    public void serialize(File file) throws IOException {
        Bukkit.getStructureManager().saveStructure(file, this.structure);
    }
}
