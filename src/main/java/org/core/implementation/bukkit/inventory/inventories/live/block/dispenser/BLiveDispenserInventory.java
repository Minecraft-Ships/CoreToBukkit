package org.core.implementation.bukkit.inventory.inventories.live.block.dispenser;

import org.bukkit.block.Container;
import org.core.implementation.bukkit.inventory.inventories.snapshot.block.dispenser.BDispenserInventorySnapshot;
import org.core.inventory.inventories.general.block.dispenser.DispenserInventory;
import org.core.inventory.inventories.snapshots.block.dispenser.DispenserInventorySnapshot;

public class BLiveDispenserInventory extends BLiveDispenserBasedInventory implements DispenserInventory {

    final org.bukkit.block.Dispenser dispenser;

    public BLiveDispenserInventory(org.bukkit.block.Dispenser dispenser) {
        this.dispenser = dispenser;
    }

    @Override
    public DispenserInventorySnapshot createSnapshot() {
        return new BDispenserInventorySnapshot(this);
    }

    @Override
    protected Container getBukkitBlockState() {
        return this.dispenser;
    }
}
