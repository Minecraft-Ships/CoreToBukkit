package org.core.implementation.bukkit.entity;

import org.core.entity.EntitySnapshot;
import org.core.entity.LiveEntity;

public class UnknownLiveEntity<T extends org.bukkit.entity.Entity> extends BLiveEntity<T> {

    public UnknownLiveEntity(T entity) {
        super(entity);
    }

    @Override
    public BEntityType.UnknownType<T> getType() {
        return new BEntityType.UnknownType<>(this.entity.getType());
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> createSnapshot() {
        return new BSnapshotValueEntity<>(this);
    }
}
