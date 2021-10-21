package org.core.implementation.bukkit.inventory.inventories.snapshot.entity;

import org.core.entity.living.human.player.LivePlayer;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;

public class BPlayerInventorySnapshot extends PlayerInventorySnapshot implements BEntityInventorySnapshot<LivePlayer> {

    public BPlayerInventorySnapshot(PlayerInventory inventory) {
        super(inventory);
    }

    @Override
    public PlayerInventorySnapshot createSnapshot() {
        return new BPlayerInventorySnapshot(this);
    }

    @Override
    public void apply(LivePlayer entity) {
        BEntityInventorySnapshot.super.apply(entity);
        //TODO
    }
}
