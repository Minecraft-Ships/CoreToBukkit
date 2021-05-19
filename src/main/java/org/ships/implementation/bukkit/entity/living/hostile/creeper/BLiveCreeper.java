package org.ships.implementation.bukkit.entity.living.hostile.creeper;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.LiveEntity;
import org.core.entity.living.hostile.creeper.CreeperEntity;
import org.core.entity.living.hostile.creeper.CreeperEntitySnapshot;
import org.core.entity.living.hostile.creeper.LiveCreeperEntity;
import org.ships.implementation.bukkit.entity.BLiveEntity;

public class BLiveCreeper extends BLiveEntity<org.bukkit.entity.Creeper> implements LiveCreeperEntity {

    @Deprecated
    public BLiveCreeper(org.bukkit.entity.Entity entity){
        super((org.bukkit.entity.Creeper)entity);
    }

    public BLiveCreeper(org.bukkit.entity.Creeper entity) {
        super(entity);
    }

    @Override
    public boolean isCharged() {
        return this.entity.isPowered();
    }

    @Override
    public CreeperEntity<LiveEntity> setCharged(boolean check) {
        this.entity.setPowered(check);
        return this;
    }

    @Override
    public EntityType<LiveCreeperEntity, CreeperEntitySnapshot> getType() {
        return EntityTypes.CREEPER.get();
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> createSnapshot() {
        return new BCreeperSnapshot(this);
    }
}
