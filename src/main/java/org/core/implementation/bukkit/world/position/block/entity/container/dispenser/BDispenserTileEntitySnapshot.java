package org.core.implementation.bukkit.world.position.block.entity.container.dispenser;

import org.core.implementation.bukkit.inventory.inventories.snapshot.block.dispenser.BDispenserInventorySnapshot;
import org.core.inventory.inventories.general.block.dispenser.DispenserInventory;
import org.core.inventory.inventories.snapshots.block.dispenser.DispenserInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.container.dispenser.DispenserTileEntity;
import org.core.world.position.block.entity.container.dispenser.DispenserTileEntitySnapshot;
import org.core.world.position.block.entity.container.dispenser.LiveDispenserTileEntity;

import java.util.Collection;
import java.util.Collections;

public class BDispenserTileEntitySnapshot implements DispenserTileEntitySnapshot {

    protected final DispenserInventorySnapshot dis;

    public BDispenserTileEntitySnapshot() {
        this.dis = new BDispenserInventorySnapshot();
    }

    public BDispenserTileEntitySnapshot(DispenserTileEntity dte) {
        this.dis = dte.getInventory().createSnapshot();
    }

    @Override
    public LiveDispenserTileEntity apply(LiveDispenserTileEntity ldte) {
        this.dis.apply(ldte.getInventory());
        return ldte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return Collections.singletonList(BlockTypes.DISPENSER);
    }

    @Override
    public DispenserInventory getInventory() {
        return this.dis;
    }

    @Override
    public DispenserTileEntitySnapshot getSnapshot() {
        return new BDispenserTileEntitySnapshot(this);
    }
}
