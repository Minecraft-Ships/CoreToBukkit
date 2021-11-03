package org.core.implementation.bukkit.entity;

import org.core.entity.EntitySnapshot;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;

@Deprecated
public class UnknownEntitySnapshot<T extends org.bukkit.entity.Entity> extends BEntitySnapshot<UnknownLiveEntity<T>> {

    protected final BEntityType.UnknownType<T> type;

    public UnknownEntitySnapshot(UnknownLiveEntity<T> entity) {
        super(entity);
        this.type = new BEntityType.UnknownType<>(entity.getBukkitEntity().getType());
    }

    public UnknownEntitySnapshot(EntitySnapshot<UnknownLiveEntity<T>> entity) {
        super(entity);
        this.type = (BEntityType.UnknownType<T>) (Object) entity.getType();
    }

    @Override
    public UnknownLiveEntity<T> spawnEntity() {
        BWorldExtent world = ((BWorldExtent) this.position.getWorld());
        T entity =
                (T) world.getBukkitWorld().spawnEntity(((BAbstractPosition<Double>) this.position).toBukkitLocation(),
                        this.type.getBukkitEntityType());
        UnknownLiveEntity<T> coreEntity = new UnknownLiveEntity<>(entity);
        this.applyDefaults(coreEntity);
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
