package org.core.implementation.bukkit.entity.living.animal.live;

import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.core.entity.living.animal.cow.LiveCow;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.living.animal.snapshot.BCowSnapshot;

public class BLiveCow extends BLiveEntity<Cow> implements LiveCow {

    @Deprecated
    public BLiveCow(Entity entity) {
        this((org.bukkit.entity.Cow) entity);
    }

    public BLiveCow(Cow entity) {
        super(entity);
    }

    @Override
    public boolean isAdult() {
        return this.getBukkitEntity().isAdult();
    }

    @Override
    public BLiveCow setAdult(boolean check) {
        if (check) {
            this.getBukkitEntity().setAdult();
        } else {
            this.getBukkitEntity().setBaby();
        }
        return this;
    }

    @Override
    public BCowSnapshot createSnapshot() {
        return new BCowSnapshot(this);
    }
}
