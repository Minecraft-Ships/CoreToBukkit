package org.core.implementation.bukkit.inventory.inventories.snapshot.block.dispenser;

import org.core.inventory.inventories.general.block.dispenser.DispenserInventory;
import org.core.inventory.inventories.snapshots.block.dispenser.DispenserInventorySnapshot;
import org.core.inventory.parts.snapshot.Grid3x3Snapshot;

public class BDispenserInventorySnapshot extends DispenserInventorySnapshot {

    public BDispenserInventorySnapshot(DispenserInventory dinv) {
        this.grid = dinv.getItems().createSnapshot();
        this.position = dinv.getPosition();
    }

    public BDispenserInventorySnapshot() {
        this.grid = new Grid3x3Snapshot();
    }

    @Override
    public DispenserInventorySnapshot createSnapshot() {
        return new BDispenserInventorySnapshot(this);
    }
}
