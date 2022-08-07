package org.core.implementation.bukkit.inventory.inventories.snapshot.entity;

import org.core.entity.LiveEntity;
import org.core.inventory.inventories.BasicEntityInventory;
import org.core.inventory.inventories.snapshots.entity.ZombieInventorySnapshot;

public class BClassicZombieInventorySnapshot<Z extends org.core.entity.living.hostile.undead.Zombie<LiveEntity> & LiveEntity>
        extends ZombieInventorySnapshot<Z> implements BEntityInventorySnapshot<Z> {

    public BClassicZombieInventorySnapshot(BasicEntityInventory<? extends Z> inv) {
        super(inv);
    }

    @Override
    public BClassicZombieInventorySnapshot<Z> createSnapshot() {
        return new BClassicZombieInventorySnapshot<>(this);
    }

}
