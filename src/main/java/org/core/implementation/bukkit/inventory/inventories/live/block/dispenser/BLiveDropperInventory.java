package org.core.implementation.bukkit.inventory.inventories.live.block.dispenser;

import org.bukkit.block.Container;
import org.core.implementation.bukkit.inventory.inventories.snapshot.block.dispenser.BDropperInventorySnapshot;
import org.core.inventory.inventories.general.block.dispenser.DropperInventory;
import org.core.inventory.inventories.snapshots.block.dispenser.DropperInventorySnapshot;

public class BLiveDropperInventory extends BLiveDispenserBasedInventory implements DropperInventory {

    final org.bukkit.block.Dropper dropper;

    public BLiveDropperInventory(org.bukkit.block.Dropper dropper) {
        this.dropper = dropper;
    }

    @Override
    protected Container getBukkitBlockState() {
        return this.dropper;
    }

    @Override
    public DropperInventorySnapshot createSnapshot() {
        return new BDropperInventorySnapshot(this);
    }
}
