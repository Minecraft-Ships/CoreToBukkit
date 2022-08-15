package org.core.implementation.bukkit.world.position.block.entity.container.furnace;

import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.core.implementation.bukkit.inventory.inventories.live.block.BLiveFurnaceInventory;
import org.core.implementation.bukkit.world.position.block.entity.AbstractLiveTileEntity;
import org.core.inventory.inventories.general.block.FurnaceInventory;
import org.core.world.position.block.entity.container.furnace.FurnaceTileEntitySnapshot;
import org.core.world.position.block.entity.container.furnace.LiveFurnaceTileEntity;

public class BFurnaceEntity extends AbstractLiveTileEntity implements LiveFurnaceTileEntity {

    public BFurnaceEntity(BlockState state) {
        super(state);
    }

    public org.bukkit.block.Furnace getBukkitFurnace() {
        return (Furnace) this.state;
    }

    @Override
    public BLiveFurnaceInventory getInventory() {
        return new BLiveFurnaceInventory(this.getBukkitFurnace());
    }

    @Override
    public FurnaceTileEntitySnapshot getSnapshot() {
        return new BFurnaceEntitySnapshot(this);
    }
}
