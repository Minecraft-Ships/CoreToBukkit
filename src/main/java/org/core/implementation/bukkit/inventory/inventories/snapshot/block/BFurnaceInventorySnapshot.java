package org.core.implementation.bukkit.inventory.inventories.snapshot.block;

import org.core.implementation.bukkit.inventory.inventories.live.block.BLiveFurnaceInventory;
import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.inventory.parts.snapshot.SlotSnapshot;

public class BFurnaceInventorySnapshot extends FurnaceInventorySnapshot {

    public BFurnaceInventorySnapshot() {
        this.fuelSlot = new SlotSnapshot(BLiveFurnaceInventory.FUEL_POSITION, null);
        this.resultsSlot = new SlotSnapshot(BLiveFurnaceInventory.RESULTS_POSITION, null);
        this.smeltingSlot = new SlotSnapshot(BLiveFurnaceInventory.SMELTING_POSITION, null);
    }

    public BFurnaceInventorySnapshot(FurnaceInventory fi) {
        this.fuelSlot = fi.getFuelSlot().createSnapshot();
        this.resultsSlot = fi.getResultsSlot().createSnapshot();
        this.smeltingSlot = fi.getSmeltingSlot().createSnapshot();
    }

    @Override
    public FurnaceInventorySnapshot createSnapshot() {
        return new BFurnaceInventorySnapshot(this);
    }
}