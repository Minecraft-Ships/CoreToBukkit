package org.core.implementation.bukkit.entity.living.hostile.undead.classic.live;

import org.bukkit.entity.Zombie;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.living.hostile.undead.classic.ClassicZombieSnapshot;
import org.core.entity.living.hostile.undead.classic.LiveClassicZombie;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.living.hostile.undead.classic.snapshot.BZombieSnapshot;
import org.core.implementation.bukkit.inventory.inventories.live.entity.BLiveZombieInventory;
import org.core.inventory.inventories.general.entity.ZombieInventory;

public class BLiveZombie extends BLiveEntity<Zombie> implements LiveClassicZombie {

    public BLiveZombie(org.bukkit.entity.Entity entity) {
        super((org.bukkit.entity.Zombie) entity);
    }

    public BLiveZombie(org.bukkit.entity.Zombie entity) {
        super(entity);
    }

    @Override
    public boolean isAdult() {
        return this.entity.isAdult();
    }

    @Override
    public BLiveZombie setAdult(boolean check) {
        if (check) {
            this.entity.setAdult();
            return this;
        }
        this.entity.setBaby();
        return this;
    }

    @Override
    public ZombieInventory<LiveClassicZombie> getInventory() {
        return new BLiveZombieInventory<>(this);
    }

    @Override
    public EntityType<LiveClassicZombie, ClassicZombieSnapshot> getType() {
        return EntityTypes
                .ZOMBIE.get();
    }

    @Override
    public BZombieSnapshot createSnapshot() {
        return new BZombieSnapshot(this);
    }

}
