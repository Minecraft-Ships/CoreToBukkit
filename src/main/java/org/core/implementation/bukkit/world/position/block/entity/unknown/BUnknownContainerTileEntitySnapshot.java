package org.core.implementation.bukkit.world.position.block.entity.unknown;

import org.core.implementation.bukkit.inventory.inventories.snapshot.block.BUnknownBlockAttachedInventorySnapshot;
import org.core.inventory.inventories.general.block.UnknownBlockAttachedInventory;
import org.core.inventory.inventories.snapshots.block.UnknownBlockAttachedInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.entity.container.unknown.LiveUnknownContainerTileEntity;
import org.core.world.position.block.entity.container.unknown.UnknownContainerTileEntitySnapshot;
import org.core.world.position.block.entity.container.unknown.UnknownContainerTiledEntity;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Arrays;
import java.util.Collection;

public class BUnknownContainerTileEntitySnapshot implements UnknownContainerTileEntitySnapshot {

    protected final UnknownBlockAttachedInventorySnapshot inventory;

    public BUnknownContainerTileEntitySnapshot(SyncBlockPosition position, BlockType... types) {
        this.inventory = new BUnknownBlockAttachedInventorySnapshot(position, types);
    }

    public BUnknownContainerTileEntitySnapshot(UnknownContainerTiledEntity entity) {
        this.inventory = entity.getInventory().createSnapshot();
    }

    @Override
    public LiveUnknownContainerTileEntity apply(LiveUnknownContainerTileEntity lucte) {
        this.inventory.apply(lucte.getInventory());
        return lucte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return Arrays.asList(this.inventory.getAllowedBlockType());
    }

    @Override
    public UnknownContainerTileEntitySnapshot getSnapshot() {
        return new BUnknownContainerTileEntitySnapshot(this);
    }

    @Override
    public UnknownBlockAttachedInventory getInventory() {
        return this.inventory;
    }
}
