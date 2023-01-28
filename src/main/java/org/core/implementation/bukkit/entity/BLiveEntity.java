package org.core.implementation.bukkit.entity;

import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.entity.Entity;
import org.core.entity.LiveEntity;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.world.position.impl.sync.BExactPosition;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class BLiveEntity<T extends org.bukkit.entity.Entity> implements LiveEntity {

    protected final T entity;

    public BLiveEntity(T entity) {
        this.entity = entity;
    }

    public T getBukkitEntity() {
        return this.entity;
    }

    @Override
    public boolean isRemoved() {
        return this.entity.isDead();
    }

    @Override
    public BLiveEntity<T> setGravity(boolean check) {
        this.entity.setGravity(check);
        return this;
    }

    @Override
    public boolean hasGravity() {
        return this.entity.hasGravity();
    }

    @Override
    public boolean isOnGround() {
        return this.entity.isOnGround();
    }

    @Override
    public double getPitch() {
        return this.entity.getLocation().getPitch();
    }

    @Override
    public BLiveEntity<T> setPitch(double value) {
        org.bukkit.Location loc = this.entity.getLocation();
        loc.setPitch((float) value);
        this.entity.teleport(loc);
        return this;
    }

    @Override
    public double getYaw() {
        return this.entity.getLocation().getYaw();
    }

    @Override
    public BLiveEntity<T> setYaw(double value) {
        org.bukkit.Location loc = this.entity.getLocation();
        loc.setYaw((float) value);
        this.entity.teleport(loc);
        return this;
    }

    @Override
    public double getRoll() {
        return 0;
    }

    @Override
    public BLiveEntity<T> setRoll(double value) {
        return this;
    }

    @Override
    public Collection<LiveEntity> getPassengers() {
        BukkitPlatform bukkitPlatform = (BukkitPlatform) TranslateCore.getPlatform();
        Set<LiveEntity> set = new HashSet<>();
        this.entity.getPassengers().forEach(e -> set.add(bukkitPlatform.createEntityInstance(e)));
        return set;
    }

    @Override
    public LiveEntity addPassengers(Collection<? extends LiveEntity> entities) {
        return this;
    }

    @Override
    public LiveEntity removePassengers(Collection<LiveEntity> entities) {
        return this;
    }

    @Override
    public SyncExactPosition getPosition() {
        return new BExactPosition(this.entity.getLocation().getX(), this.entity.getLocation().getY(),
                this.entity.getLocation().getZ(), this.entity.getWorld());
    }

    @Override
    public BLiveEntity<T> setPosition(Position<? extends Number> position) {
        BExactPosition position1 = position instanceof BExactPosition ? (BExactPosition) position :
                (BExactPosition) ((BlockPosition) position).toExactPosition();
        this.entity.teleport(position1.toBukkitLocation());
        return this;
    }

    @Override
    public LiveEntity setVelocity(Vector3<Double> velocity) {
        this.entity.setVelocity(new org.bukkit.util.Vector(velocity.getX(), velocity.getY(), velocity.getZ()));
        return this;
    }

    @Override
    public Vector3<Double> getVelocity() {
        org.bukkit.util.Vector vector = this.entity.getVelocity();
        return Vector3.valueOf(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public Optional<AText> getCustomName() {
        String customName = this.entity.getCustomName();
        if (customName == null) {
            return Optional.empty();
        }
        return Optional.of(AText.ofLegacy(customName));
    }

    @Override
    public Entity<LiveEntity> setCustomName(@Nullable AText text) {
        if (text == null) {
            this.entity.setCustomName(null);
            return this;
        }
        this.entity.setCustomName(text.toLegacy());
        return this;
    }

    @Override
    public LiveEntity setCustomNameVisible(boolean visible) {
        this.entity.setCustomNameVisible(visible);
        return this;
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.entity.isCustomNameVisible();
    }

    @Override
    public void remove() {
        this.entity.remove();
    }
}
