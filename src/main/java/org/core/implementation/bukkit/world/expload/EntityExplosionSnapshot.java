package org.core.implementation.bukkit.world.expload;

import org.core.entity.Entity;
import org.core.world.expload.Explosion;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.sync.SyncExactPosition;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class EntityExplosionSnapshot implements Explosion.ExplosionSnapshot, Explosion.EntityExplosion {

    private final Collection<BlockSnapshot.SyncBlockSnapshot> blocks;
    private final SyncExactPosition position;
    private final Entity<?> source;

    public EntityExplosionSnapshot(Explosion.EntityExplosion explosion) {
        this.source = explosion.getSource();
        this.position = explosion.getPosition();
        this.blocks = explosion
                .getAffectedPositions()
                .stream()
                .map(b -> b.getBlockDetails().createCopyOf())
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<BlockSnapshot.SyncBlockSnapshot> getBlocks() {
        return Collections.unmodifiableCollection(this.blocks);
    }

    @Override
    public Entity<?> getSource() {
        return this.source;
    }

    @Override
    public SyncExactPosition getPosition() {
        return this.position;
    }
}
