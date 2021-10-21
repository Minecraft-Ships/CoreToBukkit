package org.core.implementation.bukkit.entity.living.animal.live;

import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.living.AgeableEntity;
import org.core.entity.living.animal.cow.LiveCow;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.living.animal.snapshot.BCowSnapshot;

public class BLiveCow extends BLiveEntity<Cow> implements LiveCow {

    @Deprecated
    public BLiveCow(Entity entity){
        this((org.bukkit.entity.Cow)entity);
    }

    public BLiveCow(Cow entity) {
        super(entity);
    }

    /*@Deprecated
    public BLiveCow(CowSnapshot snapshot){
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
        return new BCowSnapshot(this);
    }
}
