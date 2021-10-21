package org.core.implementation.bukkit.entity;

import org.core.entity.EntitySnapshot;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.implementation.bukkit.world.position.impl.sync.BExactPosition;

@Deprecated
public class UnknownEntitySnapshot<T extends org.bukkit.entity.Entity> extends BEntitySnapshot<UnknownLiveEntity<T>> {

    protected BEntityType.UnknownType<T> type;

    public UnknownEntitySnapshot(UnknownLiveEntity<T> entity) {
        super(entity);
        this.type = new BEntityType.UnknownType<>(entity.entity.getType());
    }

    public UnknownEntitySnapshot(EntitySnapshot<UnknownLiveEntity<T>> entity) {
        super(entity);
        this.type = (BEntityType.UnknownType)entity.getType();
    }

    @Override
    public UnknownLiveEntity<T> spawnEntity() {
        BWorldExtent world = ((BWorldExtent)this.position.getWorld());
        T entity = (T)world.getBukkitWorld().spawnEntity(((BExactPosition)this.position).toBukkitLocation(), this.type.type);
        UnknownLiveEntity<T> coreEntity = new UnknownLiveEntity<>(entity);
        applyDefaults(coreEntity);
        return coreEntity;
    }

    @Override
    public BEntityType.UnknownType<T> getType() {
        return this.type;
    }

    @Override
    public EntitySnapshot<UnknownLiveEntity<T>> createSnapshot() {
        return new UnknownEntitySnapshot<>(this);
    }
}
