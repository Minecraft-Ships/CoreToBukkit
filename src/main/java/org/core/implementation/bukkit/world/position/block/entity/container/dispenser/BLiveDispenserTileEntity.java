package org.core.implementation.bukkit.world.position.block.entity.container.dispenser;

import org.core.implementation.bukkit.inventory.inventories.live.block.dispenser.BLiveDispenserInventory;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.inventory.inventories.general.block.dispenser.DispenserInventory;
import org.core.world.position.block.entity.container.dispenser.DispenserTileEntitySnapshot;
import org.core.world.position.block.entity.container.dispenser.LiveDispenserTileEntity;
import org.core.world.position.impl.sync.SyncBlockPosition;

public class BLiveDispenserTileEntity implements LiveDispenserTileEntity {

    protected final org.bukkit.block.Dispenser dispenser;

    @Deprecated
    public BLiveDispenserTileEntity(org.bukkit.block.BlockState state) {
        this((org.bukkit.block.Dispenser) state);
    }

    public BLiveDispenserTileEntity(org.bukkit.block.Dispenser dispenser) {
        this.dispenser = dispenser;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return new BBlockPosition(this.dispenser.getBlock());
    }

    @Override
    public DispenserInventory getInventory() {
        return new BLiveDispenserInventory(this.dispenser);
    }

    @Override
    public DispenserTileEntitySnapshot getSnapshot() {
        return new BDispenserTileEntitySnapshot(this);
    }
}
