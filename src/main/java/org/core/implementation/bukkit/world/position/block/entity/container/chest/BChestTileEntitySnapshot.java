package org.core.implementation.bukkit.world.position.block.entity.container.chest;

import org.core.implementation.bukkit.inventory.inventories.snapshot.block.BChestInventorySnapshot;
import org.core.inventory.inventories.general.block.ChestInventory;
import org.core.inventory.inventories.snapshots.block.ChestInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.container.chest.ChestTileEntity;
import org.core.world.position.block.entity.container.chest.ChestTileEntitySnapshot;
import org.core.world.position.block.entity.container.chest.LiveChestTileEntity;

import java.util.Arrays;
import java.util.Collection;

public class BChestTileEntitySnapshot implements ChestTileEntitySnapshot {

    protected final ChestInventorySnapshot inventory;

    public BChestTileEntitySnapshot() {
        this.inventory = new BChestInventorySnapshot();
    }

    public BChestTileEntitySnapshot(ChestTileEntity cte) {
        this.inventory = cte.getInventory().createSnapshot();
    }

    @Override
    public LiveChestTileEntity apply(LiveChestTileEntity lcte) {
        this.inventory.apply(lcte.getInventory());
        return lcte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return Arrays.asList(BlockTypes.CHEST, BlockTypes.TRAPPED_CHEST);
    }

    @Override
    public ChestInventory getInventory() {
        return this.inventory;
    }

    @Override
    public ChestTileEntitySnapshot getSnapshot() {
        return new BChestTileEntitySnapshot(this);
    }
}
