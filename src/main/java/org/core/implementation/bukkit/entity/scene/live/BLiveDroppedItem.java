package org.core.implementation.bukkit.entity.scene.live;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.core.entity.scene.droppeditem.LiveDroppedItem;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.scene.snapshot.BDroppedItemSnapshot;
import org.core.implementation.bukkit.inventory.inventories.live.entity.BLiveDroppedItemSlot;
import org.core.inventory.parts.Slot;

import java.util.concurrent.TimeUnit;

public class BLiveDroppedItem extends BLiveEntity<Item> implements LiveDroppedItem {

    @Deprecated
    public BLiveDroppedItem(Entity entity) {
        this((Item) entity);
    }

    public BLiveDroppedItem(Item entity) {
        super(entity);
    }

    @Override
    public int getPickupDelayTicks() {
        return this.getBukkitEntity().getPickupDelay();
    }

    @Override
    public BLiveDroppedItem setPickupDelay(int ticks) {
        this.getBukkitEntity().setPickupDelay(ticks);
        return this;
    }

    @Override
    public BLiveDroppedItem setPickupDelay(int time, TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS:
            case MICROSECONDS:
            case MILLISECONDS:
            case DAYS:
            case HOURS:
                break;
            case SECONDS:
                this.getBukkitEntity().setPickupDelay(time * 20);
                break;
            case MINUTES:
                this.getBukkitEntity().setPickupDelay((time * 20) * 60);
                break;
        }
        return this;
    }

    @Override
    public Slot getHoldingItem() {
        return new BLiveDroppedItemSlot(this.getBukkitEntity());
    }

    @Override
    public BDroppedItemSnapshot createSnapshot() {
        return new BDroppedItemSnapshot(this);
    }
}
