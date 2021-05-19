package org.ships.implementation.bukkit.entity.projectile.item.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.projectile.ProjectileEntity;
import org.core.entity.projectile.item.snowball.LiveSnowballEntity;
import org.core.entity.projectile.item.snowball.SnowballEntitySnapshot;
import org.core.source.projectile.ProjectileSource;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.ships.implementation.bukkit.entity.BEntitySnapshot;
import org.ships.implementation.bukkit.entity.projectile.item.live.BLiveSnowballEntity;
import org.ships.implementation.bukkit.world.position.impl.sync.BExactPosition;

import java.util.Optional;

public class BSnowballEntitySnapshot extends BEntitySnapshot<LiveSnowballEntity> implements SnowballEntitySnapshot {

    private ProjectileSource source;

    public BSnowballEntitySnapshot(SyncExactPosition position) {
        super(position);
    }

    public BSnowballEntitySnapshot(LiveSnowballEntity entity) {
        super(entity);
        this.source = entity.getSource().orElse(null);
    }

    public BSnowballEntitySnapshot(SnowballEntitySnapshot entity) {
        super(entity);
        this.source = entity.getSource().orElse(null);
    }

    @Override
    public BLiveSnowballEntity spawnEntity() {
        org.bukkit.Location loc = ((BExactPosition)this.position).getBukkitLocation();
        loc.setPitch((float)this.pitch);
        loc.setYaw((float)this.yaw);
        org.bukkit.entity.Snowball snowball = (org.bukkit.entity.Snowball)loc.getWorld().spawnEntity(loc, org.bukkit.entity.EntityType.SNOWBALL);
        BLiveSnowballEntity coreSnowball = new BLiveSnowballEntity(snowball);
        applyDefaults(coreSnowball);
        coreSnowball.setSource(this.source);
        return coreSnowball;
    }

    @Override
    public EntityType<LiveSnowballEntity, SnowballEntitySnapshot> getType() {
        return EntityTypes.SNOWBALL.get();
    }

    @Override
    public EntitySnapshot<LiveSnowballEntity> createSnapshot() {
        return new BSnowballEntitySnapshot(this);
    }

    @Override
    public Optional<ProjectileSource> getSource() {
        return Optional.ofNullable(this.source);
    }

    @Override
    public ProjectileEntity setSource(ProjectileSource source) {
        this.source = source;
        return this;
    }
}
