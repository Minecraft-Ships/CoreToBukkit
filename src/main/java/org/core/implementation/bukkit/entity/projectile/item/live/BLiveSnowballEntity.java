package org.core.implementation.bukkit.entity.projectile.item.live;

import org.bukkit.entity.Snowball;
import org.core.TranslateCore;
import org.core.entity.projectile.item.snowball.LiveSnowballEntity;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.projectile.item.snapshot.BSnowballEntitySnapshot;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.source.projectile.ProjectileSource;

import java.util.Optional;

public class BLiveSnowballEntity extends BLiveEntity<Snowball> implements LiveSnowballEntity {

    public BLiveSnowballEntity(org.bukkit.entity.Entity entity) {
        this((org.bukkit.entity.Snowball) entity);
    }

    public BLiveSnowballEntity(org.bukkit.entity.Snowball entity) {
        super(entity);
    }

    @Override
    public Optional<ProjectileSource> getSource() {
        org.bukkit.projectiles.ProjectileSource source = this.getBukkitEntity().getShooter();
        if (source == null) {
            return Optional.empty();
        }
        return Optional.of(((BukkitPlatform) TranslateCore.getPlatform()).getCoreProjectileSource(source));
    }

    @Override
    public BLiveSnowballEntity setSource(ProjectileSource source) {
        this
                .getBukkitEntity()
                .setShooter(((BukkitPlatform) TranslateCore.getPlatform()).getBukkitProjectileSource(source));
        return this;
    }

    @Override
    public BSnowballEntitySnapshot createSnapshot() {
        return new BSnowballEntitySnapshot(this);
    }
}
