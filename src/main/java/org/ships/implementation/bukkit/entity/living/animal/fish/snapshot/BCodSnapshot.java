package org.ships.implementation.bukkit.entity.living.animal.fish.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.living.fish.cod.CodSnapshot;
import org.core.entity.living.fish.cod.LiveCod;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.ships.implementation.bukkit.entity.BEntitySnapshot;
import org.ships.implementation.bukkit.entity.BEntityType;
import org.ships.implementation.bukkit.entity.living.animal.fish.live.BLiveCod;
import org.ships.implementation.bukkit.world.position.impl.sync.BExactPosition;

public class BCodSnapshot extends BEntitySnapshot<LiveCod> implements CodSnapshot {

    public BCodSnapshot(SyncExactPosition position) {
        super(position);
    }

    public BCodSnapshot(LiveCod entity) {
        super(entity);
    }

    public BCodSnapshot(EntitySnapshot<LiveCod> entity) {
        super(entity);
    }

    @Override
    public LiveCod spawnEntity() {
        org.bukkit.Location loc = ((BExactPosition)this.position).toBukkitLocation();
        loc.setPitch((float)this.pitch);
        loc.setYaw((float)this.yaw);
        org.bukkit.entity.Cod cod = (org.bukkit.entity.Cod)loc.getWorld().spawnEntity(loc, org.bukkit.entity.EntityType.COD);
        LiveCod coreCod = new BLiveCod(cod);
        applyDefaults(coreCod);
        return coreCod;
    }

    @Override
    public EntityType<LiveCod, CodSnapshot> getType() {
        return new BEntityType.CodType();
    }

    @Override
    public EntitySnapshot<LiveCod> createSnapshot() {
        return new BCodSnapshot(this);
    }
}
