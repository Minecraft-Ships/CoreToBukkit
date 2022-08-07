package org.core.implementation.bukkit.entity.living.animal.live;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.LiveEntity;
import org.core.entity.living.AgeableEntity;
import org.core.entity.living.animal.parrot.LiveParrot;
import org.core.entity.living.animal.parrot.Parrot;
import org.core.entity.living.animal.parrot.ParrotSnapshot;
import org.core.entity.living.animal.parrot.ParrotType;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.living.animal.snapshot.BParrotSnapshot;
import org.core.implementation.bukkit.entity.living.animal.type.BParrotType;

public class BLiveParrot extends BLiveEntity<org.bukkit.entity.Parrot> implements LiveParrot {

    @Deprecated
    public BLiveParrot(org.bukkit.entity.Entity entity) {
        this((org.bukkit.entity.Parrot) entity);
    }

    public BLiveParrot(org.bukkit.entity.Parrot entity) {
        super(entity);
    }

    @Override
    public ParrotType getVariant() {
        return new BParrotType(this.entity.getVariant());
    }

    @Override
    public Parrot<LiveEntity> setVariant(ParrotType type) {
        BParrotType type2 = (BParrotType) type;
        this.entity.setVariant(type2.getType());
        return this;
    }

    @Override
    public boolean isAdult() {
        return this.entity.isAdult();
    }

    @Override
    public AgeableEntity<LiveEntity> setAdult(boolean check) {
        if (check) {
            this.entity.setAdult();
        } else {
            this.entity.setBaby();
        }
        return this;
    }

    @Override
    public EntityType<LiveParrot, ParrotSnapshot> getType() {
        return EntityTypes.PARROT.get();
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> createSnapshot() {
        return new BParrotSnapshot(this);
    }
}
