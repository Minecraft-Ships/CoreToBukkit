package org.core.implementation.bukkit.world.position.block.entity.container.furnace;

import org.core.implementation.bukkit.inventory.inventories.snapshot.block.BFurnaceInventorySnapshot;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntity;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntitySnapshot;
import org.core.world.position.block.entity.container.furnace.LiveFurnaceTileEntity;

import java.util.Collection;
import java.util.Collections;

public class BFurnaceEntitySnapshot implements FurnaceTileEntitySnapshot {

    protected final FurnaceInventorySnapshot inventory;

    public BFurnaceEntitySnapshot() {
        this.inventory = new BFurnaceInventorySnapshot();
    }

    public BFurnaceEntitySnapshot(FurnaceTileEntity fte) {
        this.inventory = fte.getInventory().createSnapshot();
    }

    @Override
    public LiveFurnaceTileEntity apply(LiveFurnaceTileEntity lfte) {
        this.inventory.apply(lfte.getInventory());
        return lfte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return Collections.singletonList(BlockTypes.FURNACE);
    }

    @Override
    public FurnaceInventorySnapshot getInventory() {
        return this.inventory;
    }

    @Override
    public FurnaceTileEntitySnapshot getSnapshot() {
        return new BFurnaceEntitySnapshot(this);
    }
}

