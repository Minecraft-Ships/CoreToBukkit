package org.core.implementation.bukkit.entity.living.animal.live;

import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.living.AgeableEntity;
import org.core.entity.living.animal.chicken.LiveChicken;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.living.animal.snapshot.BChickenSnapshot;

public class BLiveChicken extends BLiveEntity<Chicken> implements LiveChicken {

    @Deprecated
    public BLiveChicken(Entity entity){
        this((org.bukkit.entity.Chicken)entity);
    }

    public BLiveChicken(org.bukkit.entity.Chicken entity) {
        super(entity);
    }

    /*@Deprecated
    public BLiveChicken(ChickenSnapshot snapshot){
        super(snapshot);
    }*/

    @Override
    public boolean isAdult() {
        return getBukkitEntity().isAdult();
    }

    @Override
    public AgeableEntity setAdult(boolean check) {
        if(check){
            getBukkitEntity().setAdult();
        }else{
            getBukkitEntity().setBaby();
        }
        return this;
    }


    @Override
    public EntitySnapshot createSnapshot() {
        return new BChickenSnapshot(this);
    }
}
