package org.core.implementation.bukkit.entity.living.animal.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.living.animal.cow.CowSnapshot;
import org.core.entity.living.animal.cow.LiveCow;
import org.core.implementation.bukkit.entity.BEntitySnapshot;
import org.core.implementation.bukkit.entity.living.animal.live.BLiveCow;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.world.position.impl.sync.SyncExactPosition;

public class BCowSnapshot extends BEntitySnapshot<LiveCow> implements CowSnapshot {

    private boolean adult;

    public BCowSnapshot(SyncExactPosition position) {
        super(position);
    }

    public BCowSnapshot(LiveCow entity) {
        super(entity);
        this.adult = entity.isAdult();
    }

    public BCowSnapshot(CowSnapshot entity) {
        super(entity);
        this.adult = entity.isAdult();
    }

    @Override
    public LiveCow spawnEntity() {
        org.bukkit.Location loc = ((BAbstractPosition<Double>) this.position).toBukkitLocation();
        loc.setPitch((float) this.pitch);
        loc.setYaw((float) this.yaw);
        org.bukkit.entity.Cow cow = (org.bukkit.entity.Cow) loc.getWorld().spawnEntity(loc, org.bukkit.entity.EntityType.COW);
        BLiveCow coreCow = new BLiveCow(cow);
        this.applyDefaults(coreCow);
        coreCow.setAdult(this.adult);
        return coreCow;
    }

    @Override
    public EntityType<LiveCow, CowSnapshot> getType() {
        return EntityTypes.COW.get();
    }

    @Override
    public EntitySnapshot<LiveCow> createSnapshot() {
        return new BCowSnapshot(this);
    }

    @Override
    public boolean isAdult() {
        return this.adult;
    }

    @Override
    public BCowSnapshot setAdult(boolean check) {
        this.adult = check;
        return this;
    }
}
