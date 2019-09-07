package org.ships.implementation.bukkit.entity.living.animal.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.living.AgeableEntity;
import org.core.entity.living.animal.cow.CowSnapshot;
import org.core.entity.living.animal.cow.LiveCow;
import org.core.world.position.ExactPosition;
import org.ships.implementation.bukkit.entity.BEntitySnapshot;
import org.ships.implementation.bukkit.entity.living.animal.live.BLiveCow;
import org.ships.implementation.bukkit.world.position.BExactPosition;

public class BCowSnapshot extends BEntitySnapshot<LiveCow> implements CowSnapshot {

    private boolean adult;

    public BCowSnapshot(ExactPosition position) {
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
        org.bukkit.Location loc = ((BExactPosition)this.position).getBukkitLocation();
        loc.setPitch((float)this.pitch);
        loc.setYaw((float)this.yaw);
        org.bukkit.entity.Cow cow = (org.bukkit.entity.Cow)loc.getWorld().spawnEntity(loc, org.bukkit.entity.EntityType.COW);
        BLiveCow coreCow = new BLiveCow(cow);
        applyDefaults(coreCow);
        coreCow.setAdult(this.adult);
        return coreCow;
    }

    @Override
    public EntityType<LiveCow, CowSnapshot> getType() {
        return EntityTypes.COW;
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
    public AgeableEntity setAdult(boolean check) {
        this.adult = check;
        return this;
    }
}
