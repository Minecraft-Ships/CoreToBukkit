package org.ships.implementation.bukkit.entity.living.bat.live;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.living.bat.Bat;
import org.core.entity.living.bat.BatSnapshot;
import org.core.entity.living.bat.LiveBat;
import org.ships.implementation.bukkit.entity.BLiveEntity;
import org.ships.implementation.bukkit.entity.living.bat.snapshot.BBatSnapshot;

public class BLiveBat extends BLiveEntity<org.bukkit.entity.Bat> implements LiveBat {

    @Deprecated
    public BLiveBat(org.bukkit.entity.Entity entity){
        this((org.bukkit.entity.Bat)entity);
    }

    public BLiveBat(org.bukkit.entity.Bat entity) {
        super(entity);
    }

    public BLiveBat(BatSnapshot snapshot){
        super(snapshot);
    }

    @Override
    public boolean isAwake() {
        return getBukkitEntity().isAwake();
    }

    @Override
    public Bat setAwake(boolean state) {
        getBukkitEntity().setAwake(state);
        return this;
    }

    @Override
    public EntityType<LiveBat, BatSnapshot> getType() {
        return EntityTypes.BAT;
    }

    @Override
    public EntitySnapshot createSnapshot() {
        return new BBatSnapshot(this);
    }
}
