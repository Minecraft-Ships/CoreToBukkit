package org.ships.implementation.bukkit.entity.projectile.item.live;

import org.core.TranslateCore;
import org.core.entity.EntitySnapshot;
import org.core.entity.projectile.ProjectileEntity;
import org.core.entity.projectile.item.snowball.LiveSnowballEntity;
import org.core.source.projectile.ProjectileSource;
import org.ships.implementation.bukkit.entity.BLiveEntity;
import org.ships.implementation.bukkit.entity.projectile.item.snapshot.BSnowballEntitySnapshot;
import org.ships.implementation.bukkit.platform.BukkitPlatform;

import java.util.Optional;

public class BLiveSnowballEntity extends BLiveEntity<org.bukkit.entity.Snowball> implements LiveSnowballEntity {

    public BLiveSnowballEntity(org.bukkit.entity.Entity entity){
        this((org.bukkit.entity.Snowball)entity);
    }

    public BLiveSnowballEntity(org.bukkit.entity.Snowball entity) {
        super(entity);
    }

    /*@Deprecated
    public BLiveSnowballEntity(SnowballEntitySnapshot entity){
        super(entity);
    }*/

    @Override
    public Optional<ProjectileSource> getSource() {
        org.bukkit.projectiles.ProjectileSource source = this.getBukkitEntity().getShooter();
        if(source == null){
            return Optional.empty();
        }
        return Optional.of(((BukkitPlatform) TranslateCore.getPlatform()).getCoreProjectileSource(source));
    }

    @Override
    public ProjectileEntity setSource(ProjectileSource source) {
        this.getBukkitEntity().setShooter(((BukkitPlatform) TranslateCore.getPlatform()).getBukkitProjectileSource(source));
        return this;
    }

    @Override
    public EntitySnapshot createSnapshot() {
        return new BSnowballEntitySnapshot(this);
    }
}
