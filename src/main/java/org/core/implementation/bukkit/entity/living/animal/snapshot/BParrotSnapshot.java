package org.core.implementation.bukkit.entity.living.animal.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.LiveEntity;
import org.core.entity.living.AgeableEntity;
import org.core.entity.living.animal.parrot.LiveParrot;
import org.core.entity.living.animal.parrot.Parrot;
import org.core.entity.living.animal.parrot.ParrotSnapshot;
import org.core.entity.living.animal.parrot.ParrotType;
import org.core.implementation.bukkit.entity.BEntitySnapshot;
import org.core.implementation.bukkit.entity.living.animal.live.BLiveParrot;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.world.position.impl.sync.SyncExactPosition;

public class BParrotSnapshot extends BEntitySnapshot<LiveParrot> implements ParrotSnapshot {

    protected ParrotType type;
    protected boolean adult;

    public BParrotSnapshot(SyncExactPosition position) {
        super(position);
    }

    public BParrotSnapshot(LiveParrot entity) {
        super(entity);
        this.type = entity.getVariant();
        this.adult = entity.isAdult();
    }

    public BParrotSnapshot(ParrotSnapshot entity) {
        super(entity);
        this.type = entity.getVariant();
        this.adult = entity.isAdult();
    }

    @Override
    public BLiveParrot spawnEntity() {
        org.bukkit.Location loc = ((BAbstractPosition<Double>) this.position).toBukkitLocation();
        loc.setPitch((float) this.pitch);
        loc.setYaw((float) this.yaw);
        org.bukkit.entity.Parrot parrot = (org.bukkit.entity.Parrot) loc
                .getWorld()
                .spawnEntity(loc, org.bukkit.entity.EntityType.PARROT);
        BLiveParrot coreParrot = new BLiveParrot(parrot);
        this.applyDefaults(coreParrot);
        coreParrot.setAdult(this.adult);
        coreParrot.setVariant(this.type);
        return coreParrot;
    }

    @Override
    public EntityType<LiveParrot, ParrotSnapshot> getType() {
        return EntityTypes.PARROT.get();
    }

    @Override
    public EntitySnapshot<LiveParrot> createSnapshot() {
        return new BParrotSnapshot(this);
    }

    @Override
    public ParrotType getVariant() {
        return this.type;
    }

    @Override
    public Parrot<EntitySnapshot<? extends LiveEntity>> setVariant(ParrotType type) {
        this.type = type;
        return this;
    }

    @Override
    public boolean isAdult() {
        return this.adult;
    }

    @Override
    public AgeableEntity<EntitySnapshot<? extends LiveEntity>> setAdult(boolean check) {
        this.adult = check;
        return this;
    }
}
