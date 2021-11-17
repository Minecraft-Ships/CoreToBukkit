package org.core.implementation.bukkit.world.structure;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.core.collection.BlockSetSnapshot;
import org.core.entity.EntitySnapshot;
import org.core.implementation.bukkit.platform.structure.PaletteStructure;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.implementation.bukkit.world.position.block.details.blocks.BlockStateSnapshot;
import org.core.platform.plugin.Plugin;
import org.core.vector.type.Vector3;
import org.core.world.WorldExtent;
import org.core.world.structure.Structure;
import org.core.world.structure.StructurePlacementBuilder;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BStructure implements Structure {

    private final org.bukkit.structure.Structure structure;

    public BStructure(org.bukkit.structure.Structure structure) {
        this.structure = structure;
    }

    @Override
    public Optional<String> getId() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getName() {
        return Optional.empty();
    }

    @Override
    public Optional<Plugin> getPlugin() {
        return Optional.empty();
    }

    @Override
    public Set<EntitySnapshot<?>> getEntities() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Set<BlockSetSnapshot> getBlocks() {
        return this.structure.getPalettes().stream().map(palette -> {
            if (palette instanceof PaletteStructure) {
                return new BlockSetSnapshot(((PaletteStructure) palette).getSnapshots());
            }
            return palette.getBlocks().stream().map(BlockStateSnapshot::new).collect(Collectors.toCollection(BlockSetSnapshot::new));
        }).collect(Collectors.toSet());
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
        if (builder.getPosition()==null) {
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
