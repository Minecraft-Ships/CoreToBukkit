package org.ships.implementation.bukkit.entity.living.hostile.undead.classic.live;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.living.AgeableEntity;
import org.core.entity.living.hostile.undead.classic.ClassicZombieSnapshot;
import org.core.entity.living.hostile.undead.classic.LiveClassicZombie;
import org.core.inventory.inventories.general.entity.ZombieInventory;
import org.ships.implementation.bukkit.entity.BLiveEntity;
import org.ships.implementation.bukkit.entity.living.hostile.undead.classic.snapshot.BZombieSnapshot;
import org.ships.implementation.bukkit.inventory.inventories.live.entity.BLiveZombieInventory;

public class BLiveZombie extends BLiveEntity<org.bukkit.entity.Zombie> implements LiveClassicZombie {

    public BLiveZombie(org.bukkit.entity.Entity entity){
        super((org.bukkit.entity.Zombie)entity);
    }

    public BLiveZombie(org.bukkit.entity.Zombie entity) {
        super(entity);
    }

    @Override
    public ZombieInventory getInventory() {
        return new BLiveZombieInventory(this);
    }

    @Override
    public boolean isAdult() {
        return !this.entity.isBaby();
    }

    @Override
    public AgeableEntity setAdult(boolean check) {
        this.entity.setBaby(!check);
        return this;
    }

    @Override
    public EntityType<LiveClassicZombie, ClassicZombieSnapshot> getType() {
        return EntityTypes
                .ZOMBIE.get();
    }

    @Override
    public EntitySnapshot createSnapshot() {
        return new BZombieSnapshot(this);
    }

}
