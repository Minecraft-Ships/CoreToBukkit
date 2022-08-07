package org.core.implementation.bukkit.world.position.impl.sync;

import org.bukkit.Location;
import org.core.TranslateCore;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.vector.type.Vector3;
import org.core.world.WorldExtent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.position.impl.sync.SyncPosition;

import java.util.Optional;

public class BExactPosition extends BAbstractPosition<Double> implements SyncExactPosition {

    protected final org.bukkit.Location location;

    public BExactPosition(double x, double y, double z, org.bukkit.World world) {
        this(new Location(world, x, y, z));
    }

    public BExactPosition(org.bukkit.Location location) {
        this.location = location;
    }

    @Override
    public Vector3<Double> getPosition() {
        return Vector3.valueOf(this.location.getX(), this.location.getY(), this.location.getZ());
    }

    @Override
    public SyncBlockPosition toBlockPosition() {
        return new BBlockPosition(this.location.getBlock());
    }

    @Override
    public WorldExtent getWorld() {
        return new BWorldExtent(this.location.getWorld());
    }

    @Override
    public BlockSnapshot.SyncBlockSnapshot getBlockDetails() {
        return this.toBlockPosition().getBlockDetails();
    }

    @Override
    public SyncPosition<Double> setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        this.toBlockPosition().setBlock(details, flags);
        return this;
    }

    @Override
    public SyncPosition<Double> setBlock(BlockDetails details, LivePlayer... player) {
        this.toBlockPosition().setBlock(details, player);
        return this;
    }

    @Override
    public SyncPosition<Double> resetBlock(LivePlayer... player) {
        this.toBlockPosition().resetBlock(player);
        return this;
    }

    @Override
    public Optional<LiveTileEntity> getTileEntity() {
        return ((BukkitPlatform) TranslateCore.getPlatform()).createTileEntityInstance(
                this.location.getBlock().getState());
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createEntity(
            EntityType<E, ? extends S> type) {
        return ((BukkitPlatform) TranslateCore.getPlatform()).createSnapshot(type, this);
    }

    @Override
    public SyncPosition<Double> destroy() {
        this.toBlockPosition().destroy();
        return this;
    }

    @Override
    public boolean equals(Object value) {
        if (!(value instanceof SyncPosition)) {
            return false;
        }
        Position<? extends Number> pos = (Position<? extends Number>) value;
        return pos.getPosition().equals(this.getPosition());
    }
}
