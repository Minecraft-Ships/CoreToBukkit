package org.ships.implementation.bukkit.inventory.inventories.live.block;

import org.bukkit.block.Furnace;
import org.core.inventory.inventories.live.block.LiveFurnaceInventory;
import org.core.inventory.inventories.snapshots.block.FurnaceInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.ships.implementation.bukkit.inventory.inventories.snapshot.block.BFurnaceInventorySnapshot;
import org.ships.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.ships.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.ships.implementation.bukkit.world.position.impl.sync.BBlockPosition;

import java.util.Optional;

public class BLiveFurnaceInventory implements LiveFurnaceInventory {

    public static final int FUEL_POSITION = 1;
    public static final int SMELTING_POSITION = 0;
    public static final int RESULTS_POSITION = 2;

    private class FurnaceItemSlot implements Slot {

        private int position;

        public FurnaceItemSlot(int pos){
            this.position = pos;
        }

        @Override
        public Optional<Integer> getPosition() {
            return Optional.of(this.position);
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack is = BLiveFurnaceInventory.this.getFurnace().getInventory().getItem(this.position);
            if(is == null){
                return Optional.empty();
            }
            ItemStack isC = new BLiveItemStack(is);
            return Optional.of(isC);
        }

        @Override
        public Slot setItem(ItemStack stack) {
            Furnace furnace = BLiveFurnaceInventory.this.getFurnace();
            org.bukkit.inventory.ItemStack is = stack == null ? null : ((BAbstractItemStack) stack).getBukkitItem();
            furnace.getSnapshotInventory().setItem(this.position, is);
            furnace.update();
            return this;
        }
    }

    protected org.bukkit.block.Furnace block;
    protected BLiveFurnaceInventory.FurnaceItemSlot result = new BLiveFurnaceInventory.FurnaceItemSlot(RESULTS_POSITION);
    protected BLiveFurnaceInventory.FurnaceItemSlot fuel = new BLiveFurnaceInventory.FurnaceItemSlot(FUEL_POSITION);
    protected BLiveFurnaceInventory.FurnaceItemSlot smelting = new BLiveFurnaceInventory.FurnaceItemSlot(SMELTING_POSITION);


    public BLiveFurnaceInventory(org.bukkit.block.Furnace furnace){
        this.block = furnace;
    }

    public org.bukkit.block.Furnace getFurnace(){
        return this.block;
    }

    @Override
    public Slot getFuelSlot() {
        return this.fuel;
    }

    @Override
    public Slot getResultsSlot() {
        return this.result;
    }

    @Override
    public Slot getSmeltingSlot(){
        return this.smelting;
    }

    @Override
    public FurnaceInventorySnapshot createSnapshot() {
        return new BFurnaceInventorySnapshot(this);
    }

    @Override
    public SyncBlockPosition getPosition() {
        return new BBlockPosition(this.block.getBlock());
    }
}