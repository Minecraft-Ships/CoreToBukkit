package org.core.implementation.bukkit.inventory.inventories.snapshot.block.dispenser;

import org.core.inventory.inventories.general.block.dispenser.DropperInventory;
import org.core.inventory.inventories.snapshots.block.dispenser.DropperInventorySnapshot;
import org.core.inventory.parts.snapshot.Grid3x3Snapshot;

public class BDropperInventorySnapshot extends DropperInventorySnapshot {

    public BDropperInventorySnapshot(DropperInventory dinv) {
        this.grid = dinv.getItems().createSnapshot();
        this.position = dinv.getPosition();
    }

    public BDropperInventorySnapshot() {
        this.grid = new Grid3x3Snapshot();
    }

    @Override
    public DropperInventorySnapshot createSnapshot() {
        return new BDropperInventorySnapshot(this);
    }
}
