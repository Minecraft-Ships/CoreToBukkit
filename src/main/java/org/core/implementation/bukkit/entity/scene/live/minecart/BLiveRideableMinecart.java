package org.core.implementation.bukkit.entity.scene.live.minecart;

import org.bukkit.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.scene.minecart.type.rideable.LiveRideableMinecart;

public class BLiveRideableMinecart extends BLiveMinecart<org.bukkit.entity.minecart.RideableMinecart>
        implements LiveRideableMinecart {

    public BLiveRideableMinecart(Entity entity) {
        super(entity);
    }

    public BLiveRideableMinecart(org.bukkit.entity.minecart.RideableMinecart minecart) {
        super(minecart);
    }

    @Override
    public <E extends LiveEntity> EntityType<E, ? extends EntitySnapshot<E>> getType() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public EntitySnapshot<? extends LiveEntity> createSnapshot() {
        throw new RuntimeException("Not implemented yet");

    }
}
