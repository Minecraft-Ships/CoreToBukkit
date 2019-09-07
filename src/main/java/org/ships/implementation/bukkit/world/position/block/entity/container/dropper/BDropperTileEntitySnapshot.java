package org.ships.implementation.bukkit.world.position.block.entity.container.dropper;

import org.core.inventory.inventories.general.block.dispenser.DropperInventory;
import org.core.inventory.inventories.snapshots.block.dispenser.DropperInventorySnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.container.dropper.DropperTileEntity;
import org.core.world.position.block.entity.container.dropper.DropperTileEntitySnapshot;
import org.core.world.position.block.entity.container.dropper.LiveDropperTileEntity;
import org.ships.implementation.bukkit.inventory.inventories.snapshot.block.dispenser.BDropperInventorySnapshot;

import java.util.Arrays;
import java.util.Collection;

public class BDropperTileEntitySnapshot implements DropperTileEntitySnapshot {

    protected DropperInventorySnapshot inventory;

    public BDropperTileEntitySnapshot(){
        inventory = new BDropperInventorySnapshot();
    }

    public BDropperTileEntitySnapshot(DropperTileEntity dte){
        this.inventory = dte.getInventory().createSnapshot();
    }

    @Override
    public LiveDropperTileEntity apply(LiveDropperTileEntity ldte) {
        this.inventory.apply(ldte.getInventory());
        return ldte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return Arrays.asList(BlockTypes.DROPPER.get());
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
