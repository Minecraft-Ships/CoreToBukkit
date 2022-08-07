package org.core.implementation.bukkit.entity.scene.snapshot;

import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.scene.droppeditem.DroppedItemSnapshot;
import org.core.entity.scene.droppeditem.LiveDroppedItem;
import org.core.implementation.bukkit.entity.BEntitySnapshot;
import org.core.implementation.bukkit.entity.scene.live.BLiveDroppedItem;
import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.inventory.parts.snapshot.SlotSnapshot;
import org.core.world.position.impl.sync.SyncExactPosition;

import java.util.concurrent.TimeUnit;

public class BDroppedItemSnapshot extends BEntitySnapshot<LiveDroppedItem> implements DroppedItemSnapshot {

    protected int pickupDelay;
    protected SlotSnapshot slot = new SlotSnapshot(0, null);

    public BDroppedItemSnapshot(DroppedItemSnapshot item) {
        super(item);
        this.pickupDelay = item.getPickupDelayTicks();
        this.slot = item.getHoldingItem().createSnapshot();
    }

    public BDroppedItemSnapshot(LiveDroppedItem item) {
        super(item);
        this.pickupDelay = item.getPickupDelayTicks();
        this.slot = item.getHoldingItem().createSnapshot();
    }

    public BDroppedItemSnapshot(SyncExactPosition position) {
        super(position);
    }

    @Override
    public LiveDroppedItem spawnEntity() {
        org.bukkit.Location loc = ((BAbstractPosition<Double>) this.position).toBukkitLocation();
        loc.setPitch((float) this.pitch);
        loc.setYaw((float) this.yaw);
        if (!this.slot.getItem().isPresent()) {
            throw new IllegalStateException("A item  must be set for a DroppedItemSnapshot to spawn");
        }
        org.bukkit.entity.Item item = loc
                .getWorld()
                .dropItem(loc, ((BAbstractItemStack) this.slot.getItem().get()).getBukkitItem());
        BLiveDroppedItem coreItem = new BLiveDroppedItem(item);
        this.applyDefaults(coreItem);
        coreItem.setPickupDelay(this.pickupDelay);
        coreItem.getHoldingItem().setItem(this.slot.getItem().orElse(null));
        return coreItem;
    }

    @Override
    public int getPickupDelayTicks() {
        return this.pickupDelay;
    }

    @Override
    public DroppedItemSnapshot setPickupDelay(int ticks) {
        this.pickupDelay = ticks;
        return this;
    }

    @Override
    public BDroppedItemSnapshot setPickupDelay(int time, TimeUnit unit) {
        //TODO
        switch (unit) {
            case NANOSECONDS:
            case MILLISECONDS:
            case MICROSECONDS:
            case DAYS:
            case HOURS:
                break;
            case SECONDS:
                this.pickupDelay = (time * 20);
                break;
            case MINUTES:
                this.pickupDelay = ((time * 20) * 60);
                break;
        }
        return this;
    }

    @Override
    public SlotSnapshot getHoldingItem() {
        return this.slot;
    }

    @Override
    public EntityType<LiveDroppedItem, DroppedItemSnapshot> getType() {
        return EntityTypes.DROPPED_ITEM.get();
    }

    @Override
    public BDroppedItemSnapshot createSnapshot() {
        return new BDroppedItemSnapshot(this);
    }

}
