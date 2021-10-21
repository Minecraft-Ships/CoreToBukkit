package org.core.implementation.bukkit.inventory.inventories.snapshot.entity;

import org.core.entity.living.hostile.undead.classic.ClassicZombie;
import org.core.entity.living.hostile.undead.classic.LiveClassicZombie;
import org.core.inventory.inventories.general.entity.ZombieInventory;
import org.core.inventory.inventories.snapshots.entity.ZombieInventorySnapshot;

public class BClassicZombieInventorySnapshot extends ZombieInventorySnapshot implements BEntityInventorySnapshot<LiveClassicZombie> {

    public BClassicZombieInventorySnapshot(ZombieInventory<? extends ClassicZombie> inv) {
        super(inv);
    }

    @Override
    public ZombieInventorySnapshot createSnapshot() {
        return new BClassicZombieInventorySnapshot(this);
    }

}
