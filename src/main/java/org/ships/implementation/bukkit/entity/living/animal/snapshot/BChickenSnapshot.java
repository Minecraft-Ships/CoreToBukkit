package org.ships.implementation.bukkit.entity.living.animal.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.living.AgeableEntity;
import org.core.entity.living.animal.chicken.ChickenSnapshot;
import org.core.entity.living.animal.chicken.LiveChicken;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.ships.implementation.bukkit.entity.BEntitySnapshot;
import org.ships.implementation.bukkit.entity.living.animal.live.BLiveChicken;
import org.ships.implementation.bukkit.world.position.impl.sync.BExactPosition;

public class BChickenSnapshot extends BEntitySnapshot<LiveChicken> implements ChickenSnapshot {

    private boolean adult;

    public BChickenSnapshot(SyncExactPosition position) {
        super(position);
    }

    public BChickenSnapshot(ChickenSnapshot entity) {
        super(entity);
        this.adult = entity.isAdult();
    }

    public BChickenSnapshot(LiveChicken entity) {
        super(entity);
        this.adult = entity.isAdult();
    }

    @Override
    public LiveChicken spawnEntity() {
        org.bukkit.Location loc = ((BExactPosition) this.position).toBukkitLocation();
        loc.setPitch((float) this.pitch);
        loc.setYaw((float) this.yaw);
        org.bukkit.entity.Chicken chicken = (org.bukkit.entity.Chicken) loc.getWorld().spawnEntity(loc, org.bukkit.entity.EntityType.CHICKEN);


        LiveChicken coreChicken = new BLiveChicken(chicken);
        applyDefaults(coreChicken);
        coreChicken.setAdult(this.adult);
        return coreChicken;
    }

    @Override
    public EntityType<LiveChicken, ChickenSnapshot> getType() {
        return EntityTypes.CHICKEN.get();
    }

    @Override
    public EntitySnapshot<LiveChicken> createSnapshot() {
        return new BChickenSnapshot(this);
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
