package org.core.implementation.bukkit.entity.living.bat.live;

import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.living.bat.BatSnapshot;
import org.core.entity.living.bat.LiveBat;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.living.bat.snapshot.BBatSnapshot;

public class BLiveBat extends BLiveEntity<org.bukkit.entity.Bat> implements LiveBat {

    @Deprecated
    public BLiveBat(org.bukkit.entity.Entity entity) {
        this((org.bukkit.entity.Bat) entity);
    }

    public BLiveBat(org.bukkit.entity.Bat entity) {
        super(entity);
    }

    @Override
    public boolean isAwake() {
        return this.getBukkitEntity().isAwake();
    }

    @Override
    public BLiveBat setAwake(boolean state) {
        this.getBukkitEntity().setAwake(state);
        return this;
    }

    @Override
    public EntityType<LiveBat, BatSnapshot> getType() {
        return EntityTypes.BAT.get();
    }

    @Override
    public BBatSnapshot createSnapshot() {
        return new BBatSnapshot(this);
    }
}
