package org.core.implementation.bukkit.entity.living.animal.live;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.core.entity.living.animal.chicken.LiveChicken;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.living.animal.snapshot.BChickenSnapshot;

public class BLiveChicken extends BLiveEntity<Chicken> implements LiveChicken {

    @Deprecated
    public BLiveChicken(Entity entity) {
        this((org.bukkit.entity.Chicken) entity);
    }

    public BLiveChicken(org.bukkit.entity.Chicken entity) {
        super(entity);
    }

    @Override
    public boolean isAdult() {
        return this.getBukkitEntity().isAdult();
    }

    @Override
    public BLiveChicken setAdult(boolean check) {
        if (check) {
            this.getBukkitEntity().setAdult();
        } else {
            this.getBukkitEntity().setBaby();
        }
        return this;
    }


    @Override
    public BChickenSnapshot createSnapshot() {
        return new BChickenSnapshot(this);
    }
}
