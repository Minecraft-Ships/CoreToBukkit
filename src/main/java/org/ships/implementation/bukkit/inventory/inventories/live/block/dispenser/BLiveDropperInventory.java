package org.ships.implementation.bukkit.inventory.inventories.live.block.dispenser;

import org.bukkit.block.Container;
import org.core.inventory.inventories.general.block.dispenser.DropperInventory;
import org.core.inventory.inventories.snapshots.block.dispenser.DropperInventorySnapshot;
import org.ships.implementation.bukkit.inventory.inventories.snapshot.block.dispenser.BDropperInventorySnapshot;

public class BLiveDropperInventory extends BLiveDispenserBasedInventory implements DropperInventory {

    org.bukkit.block.Dropper dropper;

    public BLiveDropperInventory(org.bukkit.block.Dropper dropper){
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
