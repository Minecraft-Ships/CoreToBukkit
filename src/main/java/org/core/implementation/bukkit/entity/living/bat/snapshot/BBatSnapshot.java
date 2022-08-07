package org.core.implementation.bukkit.entity.living.bat.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.living.bat.BatSnapshot;
import org.core.entity.living.bat.LiveBat;
import org.core.implementation.bukkit.entity.BEntitySnapshot;
import org.core.implementation.bukkit.entity.living.bat.live.BLiveBat;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.world.position.impl.sync.SyncExactPosition;

public class BBatSnapshot extends BEntitySnapshot<LiveBat> implements BatSnapshot {

    protected boolean awake;

    public BBatSnapshot(SyncExactPosition position) {
        super(position);
    }

    public BBatSnapshot(BatSnapshot entity) {
        super(entity);
        this.awake = entity.isAwake();
    }

    public BBatSnapshot(LiveBat entity) {
        super(entity);
        this.awake = entity.isAwake();

    }

    @Override
    public BLiveBat spawnEntity() {
        org.bukkit.Location loc = ((BAbstractPosition<Double>) this.position).toBukkitLocation();
        loc.setPitch((float) this.pitch);
        loc.setYaw((float) this.yaw);
        org.bukkit.entity.Bat bat = (org.bukkit.entity.Bat) loc
                .getWorld()
                .spawnEntity(loc, org.bukkit.entity.EntityType.BAT);
        BLiveBat coreBat = new BLiveBat(bat);
        this.applyDefaults(coreBat);
        bat.setAwake(this.awake);
        return coreBat;
    }

    @Override
    public EntityType<LiveBat, BatSnapshot> getType() {
        return EntityTypes.BAT.get();
    }

    @Override
    public EntitySnapshot<LiveBat> createSnapshot() {
        return new BBatSnapshot(this);
    }

    @Override
    public boolean isAwake() {
        return this.awake;
    }

    @Override
    public BBatSnapshot setAwake(boolean state) {
        this.awake = state;
        return this;
    }
}
