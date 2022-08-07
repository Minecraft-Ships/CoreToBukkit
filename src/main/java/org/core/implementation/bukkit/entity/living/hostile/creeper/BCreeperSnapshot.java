package org.core.implementation.bukkit.entity.living.hostile.creeper;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.LiveEntity;
import org.core.entity.living.hostile.creeper.CreeperEntity;
import org.core.entity.living.hostile.creeper.CreeperEntitySnapshot;
import org.core.entity.living.hostile.creeper.LiveCreeperEntity;
import org.core.implementation.bukkit.entity.BEntitySnapshot;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.world.position.impl.sync.SyncExactPosition;

public class BCreeperSnapshot extends BEntitySnapshot<LiveCreeperEntity> implements CreeperEntitySnapshot {

    private boolean charged;

    public BCreeperSnapshot(SyncExactPosition position) {
        super(position);
    }

    public BCreeperSnapshot(LiveCreeperEntity entity) {
        super(entity);
        this.charged = entity.isCharged();
    }

    public BCreeperSnapshot(CreeperEntitySnapshot entity) {
        super(entity);
        this.charged = entity.isCharged();
    }

    @Override
    public LiveCreeperEntity spawnEntity() {
        org.bukkit.Location loc = ((BAbstractPosition<Double>) this.position).toBukkitLocation();
        loc.setPitch((float) this.pitch);
        loc.setYaw((float) this.yaw);
        org.bukkit.entity.Creeper creeper = (org.bukkit.entity.Creeper) loc
                .getWorld()
                .spawnEntity(loc, org.bukkit.entity.EntityType.CREEPER);
        BLiveCreeper liveCreeper = new BLiveCreeper(creeper);
        this.applyDefaults(liveCreeper);
        liveCreeper.setCharged(this.charged);
        return liveCreeper;
    }

    @Override
    public boolean isCharged() {
        return this.charged;
    }

    @Override
    public CreeperEntity<EntitySnapshot<? extends LiveEntity>> setCharged(boolean check) {
        this.charged = check;
        return this;
    }

    @Override
    public EntityType<LiveCreeperEntity, CreeperEntitySnapshot> getType() {
        return EntityTypes.CREEPER.get();
    }

    @Override
    public EntitySnapshot<LiveCreeperEntity> createSnapshot() {
        return new BCreeperSnapshot(this);
    }
}
