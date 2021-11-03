package org.core.implementation.bukkit.world.position.block.entity.container.dropper;

import org.core.implementation.bukkit.inventory.inventories.snapshot.block.dispenser.BDropperInventorySnapshot;
import org.core.inventory.inventories.general.block.dispenser.DropperInventory;
import org.core.inventory.inventories.snapshots.block.dispenser.DropperInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.container.dropper.DropperTileEntity;
import org.core.world.position.block.entity.container.dropper.DropperTileEntitySnapshot;
import org.core.world.position.block.entity.container.dropper.LiveDropperTileEntity;

import java.util.Collection;
import java.util.Collections;

public class BDropperTileEntitySnapshot implements DropperTileEntitySnapshot {

    protected final DropperInventorySnapshot inventory;

    public BDropperTileEntitySnapshot() {
        this.inventory = new BDropperInventorySnapshot();
    }

    public BDropperTileEntitySnapshot(DropperTileEntity dte) {
        this.inventory = dte.getInventory().createSnapshot();
    }

    @Override
    public LiveDropperTileEntity apply(LiveDropperTileEntity ldte) {
        this.inventory.apply(ldte.getInventory());
        return ldte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return Collections.singletonList(BlockTypes.DROPPER);
    }

    @Override
    public DropperInventory getInventory() {
        return this.inventory;
    }

    @Override
    public DropperTileEntitySnapshot getSnapshot() {
        return new BDropperTileEntitySnapshot(this);
    }
}
