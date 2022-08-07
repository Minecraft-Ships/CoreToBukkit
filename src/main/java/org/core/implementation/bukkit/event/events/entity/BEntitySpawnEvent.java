package org.core.implementation.bukkit.event.events.entity;

import org.core.entity.LiveEntity;
import org.core.event.events.entity.EntitySpawnEvent;
import org.core.world.position.impl.sync.SyncExactPosition;

public class BEntitySpawnEvent implements EntitySpawnEvent {

    final LiveEntity entity;
    final SyncExactPosition position;
    boolean cancel;

    public BEntitySpawnEvent(SyncExactPosition position, LiveEntity entity) {
        this.entity = entity;
        this.position = position;
    }

    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean value) {
        this.cancel = value;
    }

    @Override
    public LiveEntity getEntity() {
        return this.entity;
    }

    @Override
    public SyncExactPosition getPosition() {
        return this.position;
    }
}
