package org.core.implementation.bukkit.entity.scene.live;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.core.entity.scene.droppeditem.DroppedItem;
import org.core.entity.scene.droppeditem.LiveDroppedItem;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.inventory.parts.Slot;
import org.core.implementation.bukkit.entity.scene.snapshot.BDroppedItemSnapshot;
import org.core.implementation.bukkit.inventory.inventories.live.entity.BLiveDroppedItemSlot;

import java.util.concurrent.TimeUnit;

public class BLiveDroppedItem extends BLiveEntity<Item> implements LiveDroppedItem {

    @Deprecated
    public BLiveDroppedItem(Entity entity){
        this((Item)entity);
    }

    public BLiveDroppedItem(Item entity) {
        super(entity);
    }

    /*@Deprecated
    public BLiveDroppedItem(DroppedItemSnapshot snapshot){
        super(snapshot);
    }*/

    @Override
    public int getPickupDelayTicks() {
        return getBukkitEntity().getPickupDelay();
    }

    @Override
    public DroppedItem setPickupDelay(int ticks) {
        getBukkitEntity().setPickupDelay(ticks);
        return this;
    }

    @Override
    public DroppedItem setPickupDelay(int time, TimeUnit unit) {
        switch(unit){
            case NANOSECONDS:
                break;
            case MICROSECONDS:
                break;
            case MILLISECONDS:
                break;
            case SECONDS:
                getBukkitEntity().setPickupDelay(time * 20);
                break;
            case MINUTES:
                getBukkitEntity().setPickupDelay((time * 20) * 60);
                break;
            case HOURS:
                break;
            case DAYS:
                break;
        }
        return this;
    }

    @Override
    public Slot getHoldingItem() {
        return new BLiveDroppedItemSlot(getBukkitEntity());
    }

    @Override
    public BDroppedItemSnapshot createSnapshot() {
        return new BDroppedItemSnapshot(this);
    }
}
