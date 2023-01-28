package org.core.implementation.bukkit.entity;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.implementation.bukkit.world.position.impl.sync.BExactPosition;
import org.core.utils.entry.AbstractSnapshotValue;
import org.core.vector.type.Vector3;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BSnapshotValueEntity<BE extends org.bukkit.entity.Entity, E extends BLiveEntity<BE>>
        implements EntitySnapshot<E> {

    private final EntityType<E, ? extends EntitySnapshot<E>> type;
    private final E createdFrom;
    private Set<EntitySnapshotValue<? super BE, ?>> snapshotValues = new HashSet<>();

    public BSnapshotValueEntity(E entity) {
        BE bukkitEntity = entity.getBukkitEntity();
        this.snapshotValues = EntitySnapshotValue.getSnapshotValues(bukkitEntity);
        this.type = entity.getType();
        this.createdFrom = entity;
        this.snapshotValues.forEach(sv -> sv.storeValue(bukkitEntity));
    }

    public BSnapshotValueEntity(BSnapshotValueEntity<BE, E> entity) {
        this.snapshotValues.addAll(entity.snapshotValues);
        this.type = entity.getType();
        this.createdFrom = entity.createdFrom;
    }

    private <V> Optional<EntitySnapshotValue<?, V>> getSnapshotValue(String id) {
        return this.snapshotValues
                .parallelStream()
                .filter(v -> v.getId().equals(id))
                .findAny()
                .map(v -> (EntitySnapshotValue<?, V>) v);
    }

    @Override
    public SyncExactPosition getPosition() {
        return new BExactPosition(this.<Location>getSnapshotValue("LOCATION").get().getValue());
    }

    @Override
    public EntityType<E, ? extends EntitySnapshot<E>> getType() {
        return this.type;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setPitch(double value) {
        Location loc = this.<Location>getSnapshotValue("LOCATION").get().getValue();
        loc.setPitch((float) value);
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setYaw(double value) {
        Location loc = this.<Location>getSnapshotValue("LOCATION").get().getValue();
        loc.setYaw((float) value);
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setRoll(double value) {
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setPosition(Position<? extends Number> position) {
        EntitySnapshotValue<?, Location> locValue = this.<Location>getSnapshotValue("LOCATION").get();
        Location oldLoc = locValue.getValue();
        Location loc = new Location(((BWorldExtent) position.getWorld()).getBukkitWorld(),
                                    position.getX().doubleValue(), position.getY().doubleValue(),
                                    position.getZ().doubleValue());
        loc.setPitch(oldLoc.getPitch());
        loc.setYaw(oldLoc.getYaw());
        locValue.setValue(loc);
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setGravity(boolean check) {
        this.<Boolean>getSnapshotValue("GRAVITY").get().setValue(check);
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setVelocity(Vector3<Double> velocity) {
        this
                .<Vector>getSnapshotValue("VELOCITY")
                .get()
                .setValue(new Vector(velocity.getX(), velocity.getY(), velocity.getZ()));
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setCustomName(@Nullable AText text) {
        if (text == null) {
            this.<String>getSnapshotValue("CUSTOM_NAME").get().setValue(null);
            return this;
        }
        this.<String>getSnapshotValue("CUSTOM_NAME").get().setValue(text.toLegacy());
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> setCustomNameVisible(boolean visible) {
        this.<Boolean>getSnapshotValue("CUSTOM_NAME_VISIBLE").get().setValue(visible);
        return this;
    }

    @Override
    public double getPitch() {
        return this.<Location>getSnapshotValue("LOCATION").get().getValue().getPitch();
    }

    @Override
    public double getYaw() {
        return this.<Location>getSnapshotValue("LOCATION").get().getValue().getYaw();
    }

    @Override
    public double getRoll() {
        return 0;
    }

    @Override
    public boolean hasGravity() {
        return this.<Boolean>getSnapshotValue("GRAVITY").get().getValue();
    }

    @Override
    public Vector3<Double> getVelocity() {
        Vector vector = this.<Vector>getSnapshotValue("VELOCITY").get().getValue();
        return Vector3.valueOf(vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public Optional<AText> getCustomName() {
        Optional<EntitySnapshotValue<?, String>> opText = this.getSnapshotValue("CUSTOM_NAME");
        return opText.map(stringEntitySnapshotValue -> AText.ofLegacy(stringEntitySnapshotValue.getValue()));
    }

    @Override
    public boolean isCustomNameVisible() {
        return this.<Boolean>getSnapshotValue("CUSTOM_NAME_VISIBLE").get().getValue();
    }

    @Override
    public Collection<EntitySnapshot<? extends LiveEntity>> getPassengers() {
        return new HashSet<>();
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> addPassengers(Collection<? extends EntitySnapshot<? extends LiveEntity>> entities) {
        return this;
    }

    @Override
    public Entity<EntitySnapshot<? extends LiveEntity>> removePassengers(Collection<EntitySnapshot<? extends LiveEntity>> entities) {
        return this;
    }

    @Override
    public boolean isOnGround() {
        return this.<Boolean>getSnapshotValue("IS_ON_GROUND").get().getValue();
    }

    @Override
    public boolean isRemoved() {
        return this.<Boolean>getSnapshotValue("IS_REMOVED").map(AbstractSnapshotValue::getValue).orElse(false);
    }

    @Override
    public E spawnEntity() {
        Location loc = this.<Location>getSnapshotValue("LOCATION").get().getValue();
        BE entity = (BE) loc.getWorld().spawnEntity(loc, ((BEntityType<?, ?>) this.type).getBukkitEntityType());
        this.snapshotValues.stream().map(sv -> (EntitySnapshotValue<BE, ?>) sv).forEach(sv -> sv.applyValue(entity));
        return (E) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(entity);
    }

    @Override
    public EntitySnapshot<E> createSnapshot() {
        return new BSnapshotValueEntity<>(this);
    }

    @Override
    public Optional<E> getCreatedFrom() {
        return Optional.ofNullable(this.createdFrom);
    }
}
