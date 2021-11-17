package org.core.implementation.bukkit.platform.structure;

import org.bukkit.block.BlockState;
import org.bukkit.structure.Palette;
import org.core.implementation.bukkit.world.position.block.details.blocks.BlockStateSnapshot;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.BlockPosition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PaletteStructure implements Palette {

    private final List<BlockSnapshot<? extends BlockPosition>> blocks = new ArrayList<>();

    public PaletteStructure(Collection<? extends BlockSnapshot<? extends BlockPosition>> collection) {
        this.blocks.addAll(collection);
    }

    public List<BlockSnapshot<? extends BlockPosition>> getSnapshots() {
        return this.blocks;
    }

    @Override
    public @NotNull List<BlockState> getBlocks() {
        return this.blocks.stream().map(snapshot -> {
            BlockStateSnapshot bSnapshot = (BlockStateSnapshot) snapshot;
            return bSnapshot.getBukkitBlockState();
        }).collect(Collectors.toList());
    }

    @Override
    public int getBlockCount() {
        return this.blocks.size();
    }
}
