package org.core.implementation.bukkit.world.position.impl;

import org.bukkit.Location;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.world.position.impl.Position;

public abstract class BAbstractPosition<T extends Number> implements Position<T> {

    public org.bukkit.Location toBukkitLocation() {
        return new Location(((BWorldExtent) this.getWorld()).getBukkitWorld(), this.getX().doubleValue(), this.getY().doubleValue(), this.getZ().doubleValue());
    }

    public org.bukkit.block.Block toBukkitBlock() {
        return this.toBukkitLocation().getBlock();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) {
            return false;
        }
        Position<? extends Number> pos = (Position<? extends Number>) obj;
        if (!pos.getX().equals(this.getX())) {
            return false;
        }
        if (!pos.getY().equals(this.getY())) {
            return false;
        }
        if (!pos.getZ().equals(this.getZ())) {
            return false;
        }
        return pos.getWorld().equals(this.getWorld());
    }
}
