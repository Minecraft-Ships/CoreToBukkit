package org.core.implementation.bukkit.entity;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.core.utils.entry.AbstractSnapshotValue;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class EntitySnapshotValue<E, O> extends AbstractSnapshotValue<E, O> implements Cloneable {

    public static final EntitySnapshotValue<Entity, Boolean> IS_REMOVED = new EntitySnapshotValue<>(Entity::isDead,
                                                                                                    (entity, aBoolean) -> {
                                                                                                        if (aBoolean) {
                                                                                                            entity.remove();
                                                                                                        }
                                                                                                    });

    public static final EntitySnapshotValue<Entity, Location> LOCATION = new EntitySnapshotValue<>(Entity::getLocation,
                                                                                                   Entity::teleport);
    public static final EntitySnapshotValue<Entity, Vector> VELOCITY = new EntitySnapshotValue<>(Entity::getVelocity,
                                                                                                 Entity::setVelocity);
    public static final EntitySnapshotValue<Entity, Boolean> GRAVITY = new EntitySnapshotValue<>(Entity::hasGravity,
                                                                                                 Entity::setGravity);
    public static final EntitySnapshotValue<MushroomCow, MushroomCow.Variant> MOOSHROOM_VARIANT = new EntitySnapshotValue<>(
            MushroomCow.class, MushroomCow::getVariant, MushroomCow::setVariant);

    public static final EntitySnapshotValue<Sheep, DyeColor> SHEEP_COLOUR = new EntitySnapshotValue<>(Sheep.class,
                                                                                                      Sheep::getColor,
                                                                                                      Sheep::setColor);

    public static final EntitySnapshotValue<Entity, String> CUSTOM_NAME = new EntitySnapshotValue<>(
            Nameable::getCustomName, Nameable::setCustomName);
    public static final EntitySnapshotValue<Entity, Boolean> CUSTOM_NAME_VISIBLE = new EntitySnapshotValue<>(
            Entity::isCustomNameVisible, Entity::setCustomNameVisible);
    public static final EntitySnapshotValue<Entity, Boolean> OP = new EntitySnapshotValue<>(Entity::isOp,
                                                                                            Entity::setOp);
    public static final EntitySnapshotValue<Entity, Boolean> GLOWING = new EntitySnapshotValue<>(Entity::isGlowing,
                                                                                                 Entity::setGlowing);
    public static final EntitySnapshotValue<Entity, Boolean> SILENT = new EntitySnapshotValue<>(Entity::isSilent,
                                                                                                Entity::setSilent);
    public static final EntitySnapshotValue<Entity, Boolean> INVULNERABLE = new EntitySnapshotValue<>(
            Entity::isInvulnerable, Entity::setInvulnerable);
    public static final EntitySnapshotValue<Entity, Boolean> IS_ON_GROUND = new EntitySnapshotValue<>(
            Entity::isOnGround, (e, v) -> {
    });

    public static final EntitySnapshotValue<Damageable, Double> HEALTH = new EntitySnapshotValue<>(Damageable.class,
                                                                                                   Damageable::getHealth,
                                                                                                   Damageable::setHealth);
    public static final EntitySnapshotValue<Damageable, Double> ABSORPTION = new EntitySnapshotValue<>(Damageable.class,
                                                                                                       Damageable::getAbsorptionAmount,
                                                                                                       Damageable::setAbsorptionAmount);

    public static final EntitySnapshotValue<Sittable, Boolean> SITTING = new EntitySnapshotValue<>(Sittable.class,
                                                                                                   Sittable::isSitting,
                                                                                                   Sittable::setSitting);

    public static final EntitySnapshotValue<LivingEntity, Boolean> PICK_UP_ITEMS = new EntitySnapshotValue<>(
            LivingEntity.class, LivingEntity::getCanPickupItems, LivingEntity::setCanPickupItems);
    public static final EntitySnapshotValue<LivingEntity, Boolean> REMOVE_WHEN_FAR_AWAY = new EntitySnapshotValue<>(
            LivingEntity.class, LivingEntity::getRemoveWhenFarAway, LivingEntity::setRemoveWhenFarAway);
    public static final EntitySnapshotValue<LivingEntity, Integer> MAX_AIR = new EntitySnapshotValue<>(
            LivingEntity.class, LivingEntity::getMaximumAir, LivingEntity::setMaximumAir);
    public static final EntitySnapshotValue<LivingEntity, Integer> REMAINING_AIR = new EntitySnapshotValue<>(
            LivingEntity.class, LivingEntity::getRemainingAir, LivingEntity::setRemainingAir);
    public static final EntitySnapshotValue<LivingEntity, Boolean> AI = new EntitySnapshotValue<>(LivingEntity.class,
                                                                                                  LivingEntity::hasAI,
                                                                                                  LivingEntity::setAI);
    public static final EntitySnapshotValue<LivingEntity, Boolean> COLLIDABLE = new EntitySnapshotValue<>(
            LivingEntity.class, LivingEntity::isCollidable, LivingEntity::setCollidable);
    public static final EntitySnapshotValue<LivingEntity, Boolean> GLIDING = new EntitySnapshotValue<>(
            LivingEntity.class, LivingEntity::isGliding, LivingEntity::setGliding);
    public static final EntitySnapshotValue<LivingEntity, Boolean> SWIMMING = new EntitySnapshotValue<>(
            LivingEntity.class, LivingEntity::isSwimming, LivingEntity::setSwimming);

    public static final EntitySnapshotValue<Ageable, Integer> AGE = new EntitySnapshotValue<>(Ageable.class,
                                                                                              Ageable::getAge,
                                                                                              Ageable::setAge);
    public static final EntitySnapshotValue<Ageable, Boolean> BREED = new EntitySnapshotValue<>(Ageable.class,
                                                                                                Ageable::canBreed,
                                                                                                Ageable::setBreed);
    public static final EntitySnapshotValue<Ageable, Boolean> AGE_LOCK = new EntitySnapshotValue<>(Ageable.class,
                                                                                                   Ageable::getAgeLock,
                                                                                                   Ageable::setAgeLock);

    public static final EntitySnapshotValue<Animals, Integer> LOVE_MODE_TICKS = new EntitySnapshotValue<>(Animals.class,
                                                                                                          Animals::getLoveModeTicks,
                                                                                                          Animals::setLoveModeTicks);
    public static final EntitySnapshotValue<Animals, UUID> BREED_CAUSE = new EntitySnapshotValue<>(Animals.class,
                                                                                                   Animals::getBreedCause,
                                                                                                   Animals::setBreedCause);

    public static final EntitySnapshotValue<Tameable, Boolean> TAMED = new EntitySnapshotValue<>(Tameable.class,
                                                                                                 Tameable::isTamed,
                                                                                                 Tameable::setTamed);
    public static final EntitySnapshotValue<Tameable, AnimalTamer> ANIMAL_TAMER = new EntitySnapshotValue<>(
            Tameable.class, Tameable::getOwner, Tameable::setOwner);

    public static final EntitySnapshotValue<Cat, Cat.Type> CAT_TYPE = new EntitySnapshotValue<>(Cat.class,
                                                                                                Cat::getCatType,
                                                                                                Cat::setCatType);
    public static final EntitySnapshotValue<Cat, DyeColor> COLLAR_COLOR = new EntitySnapshotValue<>(Cat.class,
                                                                                                    Cat::getCollarColor,
                                                                                                    Cat::setCollarColor);

    public static final EntitySnapshotValue<AbstractArrow, Boolean> CRITICAL = new EntitySnapshotValue<>(
            AbstractArrow.class, AbstractArrow::isCritical, AbstractArrow::setCritical);
    public static final EntitySnapshotValue<AbstractArrow, Double> ARROW_DAMAGE = new EntitySnapshotValue<>(
            AbstractArrow.class, AbstractArrow::getDamage, AbstractArrow::setDamage);
    public static final EntitySnapshotValue<AbstractArrow, Integer> KNOCKBACK_STRENGTH = new EntitySnapshotValue<>(
            AbstractArrow.class, AbstractArrow::getKnockbackStrength, AbstractArrow::setKnockbackStrength);
    public static final EntitySnapshotValue<AbstractArrow, AbstractArrow.PickupStatus> PICKUP_STATUS = new EntitySnapshotValue<>(
            AbstractArrow.class, AbstractArrow::getPickupStatus, AbstractArrow::setPickupStatus);
    public static final EntitySnapshotValue<AbstractArrow, Integer> PIERCE_LEVEL = new EntitySnapshotValue<>(
            AbstractArrow.class, AbstractArrow::getPierceLevel, AbstractArrow::setPierceLevel);

    public static final EntitySnapshotValue<AbstractHorse, Integer> DOMESTICATION = new EntitySnapshotValue<>(
            AbstractHorse.class, AbstractHorse::getDomestication, AbstractHorse::setDomestication);
    public static final EntitySnapshotValue<AbstractHorse, Integer> MAX_DOMESTICATION = new EntitySnapshotValue<>(
            AbstractHorse.class, AbstractHorse::getMaxDomestication, AbstractHorse::setMaxDomestication);
    public static final EntitySnapshotValue<AbstractHorse, Double> JUMP_STRENGTH = new EntitySnapshotValue<>(
            AbstractHorse.class, AbstractHorse::getJumpStrength, AbstractHorse::setJumpStrength);

    public static final EntitySnapshotValue<AreaEffectCloud, PotionData> POTION_DATA = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getBasePotionData, AreaEffectCloud::setBasePotionData);
    public static final EntitySnapshotValue<AreaEffectCloud, Color> COLOR = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getColor, AreaEffectCloud::setColor);
    public static final EntitySnapshotValue<AreaEffectCloud, Integer> DURATION = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getDuration, AreaEffectCloud::setDuration);
    public static final EntitySnapshotValue<AreaEffectCloud, Integer> DURATION_ON_USE = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getDurationOnUse, AreaEffectCloud::setDurationOnUse);
    public static final EntitySnapshotValue<AreaEffectCloud, Particle> PARTICLE = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getParticle, AreaEffectCloud::setParticle);
    public static final EntitySnapshotValue<AreaEffectCloud, Float> RADIUS = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getRadius, AreaEffectCloud::setRadius);
    public static final EntitySnapshotValue<AreaEffectCloud, Float> RADIUS_ON_USE = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getRadiusOnUse, AreaEffectCloud::setRadiusOnUse);
    public static final EntitySnapshotValue<AreaEffectCloud, Float> RADIUS_PER_TICK = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getRadiusPerTick, AreaEffectCloud::setRadiusPerTick);
    public static final EntitySnapshotValue<AreaEffectCloud, Integer> REAPPLICATION_DELAY = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getReapplicationDelay, AreaEffectCloud::setReapplicationDelay);
    public static final EntitySnapshotValue<AreaEffectCloud, ProjectileSource> PROJECTILE_SOURCE = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getSource, AreaEffectCloud::setSource);
    public static final EntitySnapshotValue<AreaEffectCloud, Integer> WAIT_TIME = new EntitySnapshotValue<>(
            AreaEffectCloud.class, AreaEffectCloud::getWaitTime, AreaEffectCloud::setWaitTime);

    public static final EntitySnapshotValue<ArmorStand, Boolean> HAS_ARMS = new EntitySnapshotValue<>(ArmorStand.class,
                                                                                                      ArmorStand::hasArms,
                                                                                                      ArmorStand::setArms);
    public static final EntitySnapshotValue<ArmorStand, Boolean> HAS_BASE_PLATE = new EntitySnapshotValue<>(
            ArmorStand.class, ArmorStand::hasBasePlate, ArmorStand::setBasePlate);
    public static final EntitySnapshotValue<ArmorStand, EulerAngle> HEAD_POSE = new EntitySnapshotValue<>(
            ArmorStand.class, ArmorStand::getHeadPose, ArmorStand::setHeadPose);
    public static final EntitySnapshotValue<ArmorStand, EulerAngle> LEFT_LEG_POSE = new EntitySnapshotValue<>(
            ArmorStand.class, ArmorStand::getLeftLegPose, ArmorStand::setLeftLegPose);
    public static final EntitySnapshotValue<ArmorStand, EulerAngle> LEFT_ARM_POSE = new EntitySnapshotValue<>(
            ArmorStand.class, ArmorStand::getLeftArmPose, ArmorStand::setLeftArmPose);
    public static final EntitySnapshotValue<ArmorStand, EulerAngle> RIGHT_LEG_POSE = new EntitySnapshotValue<>(
            ArmorStand.class, ArmorStand::getRightLegPose, ArmorStand::setRightLegPose);
    public static final EntitySnapshotValue<ArmorStand, EulerAngle> RIGHT_ARM_POSE = new EntitySnapshotValue<>(
            ArmorStand.class, ArmorStand::getRightArmPose, ArmorStand::setRightArmPose);

    public static final EntitySnapshotValue<ArmorStand, ItemStack> ARMOR_CHEST_EQUIPMENT = new EntitySnapshotValue<>(
            ArmorStand.class, stand -> stand.getEquipment().getItem(EquipmentSlot.CHEST),
            (stand, item) -> stand.getEquipment().setItem(EquipmentSlot.CHEST, item));

    public static final EntitySnapshotValue<ArmorStand, ItemStack> ARMOR_FEET_EQUIPMENT = new EntitySnapshotValue<>(
            ArmorStand.class, stand -> stand.getEquipment().getItem(EquipmentSlot.FEET),
            (stand, item) -> stand.getEquipment().setItem(EquipmentSlot.FEET, item));

    public static final EntitySnapshotValue<ArmorStand, ItemStack> ARMOR_MAIN_HAND_EQUIPMENT = new EntitySnapshotValue<>(
            ArmorStand.class, stand -> stand.getEquipment().getItem(EquipmentSlot.HAND),
            (stand, item) -> stand.getEquipment().setItem(EquipmentSlot.HAND, item));

    public static final EntitySnapshotValue<ArmorStand, ItemStack> ARMOR_OFF_MAIN_HAND_EQUIPMENT = new EntitySnapshotValue<>(
            ArmorStand.class, stand -> stand.getEquipment().getItem(EquipmentSlot.OFF_HAND),
            (stand, item) -> stand.getEquipment().setItem(EquipmentSlot.OFF_HAND, item));

    public static final EntitySnapshotValue<ArmorStand, ItemStack> ARMOR_HEAD_EQUIPMENT = new EntitySnapshotValue<>(
            ArmorStand.class, stand -> stand.getEquipment().getItem(EquipmentSlot.HEAD),
            (stand, item) -> stand.getEquipment().setItem(EquipmentSlot.HEAD, item));
    public static final EntitySnapshotValue<ArmorStand, Boolean> MARKER = new EntitySnapshotValue<>(ArmorStand.class,
                                                                                                    ArmorStand::isMarker,
                                                                                                    ArmorStand::setMarker);
    public static final EntitySnapshotValue<ArmorStand, Boolean> IS_SMALL = new EntitySnapshotValue<>(ArmorStand.class,
                                                                                                      ArmorStand::isSmall,
                                                                                                      ArmorStand::setSmall);
    public static final EntitySnapshotValue<ArmorStand, Boolean> IS_VISIBLE = new EntitySnapshotValue<>(
            ArmorStand.class, ArmorStand::isVisible, ArmorStand::setVisible);

    public static final EntitySnapshotValue<Arrow, PotionData> ARROW_POTION_DATA = new EntitySnapshotValue<>(
            Arrow.class, Arrow::getBasePotionData, Arrow::setBasePotionData);

    public static final EntitySnapshotValue<Bat, Boolean> IS_AWAKE = new EntitySnapshotValue<>(Bat.class, Bat::isAwake,
                                                                                               Bat::setAwake);

    public static final EntitySnapshotValue<Bee, Integer> ANGER = new EntitySnapshotValue<>(Bee.class, Bee::getAnger,
                                                                                            Bee::setAnger);
    public static final EntitySnapshotValue<Bee, Integer> CANNOT_ENTER_HIVE_TICKS = new EntitySnapshotValue<>(Bee.class,
                                                                                                              Bee::getCannotEnterHiveTicks,
                                                                                                              Bee::setCannotEnterHiveTicks);
    public static final EntitySnapshotValue<Bee, Location> FLOWER = new EntitySnapshotValue<>(Bee.class, Bee::getFlower,
                                                                                              Bee::setFlower);
    public static final EntitySnapshotValue<Bee, Boolean> HAS_NECTAR = new EntitySnapshotValue<>(Bee.class,
                                                                                                 Bee::hasNectar,
                                                                                                 Bee::setHasNectar);
    public static final EntitySnapshotValue<Bee, Boolean> HAS_STUNG = new EntitySnapshotValue<>(Bee.class,
                                                                                                Bee::hasStung,
                                                                                                Bee::setHasStung);
    public static final EntitySnapshotValue<Bee, Location> HIVE = new EntitySnapshotValue<>(Bee.class, Bee::getHive,
                                                                                            Bee::setHive);

    public static final EntitySnapshotValue<Boat, TreeSpecies> BOAT_TREE_SPECIES = new EntitySnapshotValue<>(Boat.class,
                                                                                                             Boat::getWoodType,
                                                                                                             Boat::setWoodType);

    public static final EntitySnapshotValue<ChestedHorse, Boolean> CARRYING_CHEST = new EntitySnapshotValue<>(
            ChestedHorse.class, ChestedHorse::isCarryingChest, ChestedHorse::setCarryingChest);

    public static final EntitySnapshotValue<Creeper, Boolean> POWERED = new EntitySnapshotValue<>(Creeper.class,
                                                                                                  Creeper::isPowered,
                                                                                                  Creeper::setPowered);
    public static final EntitySnapshotValue<Creeper, Integer> EXPLOSION_RADIUS = new EntitySnapshotValue<>(
            Creeper.class, Creeper::getExplosionRadius, Creeper::setExplosionRadius);
    public static final EntitySnapshotValue<Creeper, Integer> MAX_FUSE_TICKS = new EntitySnapshotValue<>(Creeper.class,
                                                                                                         Creeper::getMaxFuseTicks,
                                                                                                         Creeper::setMaxFuseTicks);

    public static final EntitySnapshotValue<EnderCrystal, Location> BEAM_TARGET = new EntitySnapshotValue<>(
            EnderCrystal.class, EnderCrystal::getBeamTarget, EnderCrystal::setBeamTarget);
    public static final EntitySnapshotValue<EnderCrystal, Boolean> SHOWING_BOTTOM = new EntitySnapshotValue<>(
            EnderCrystal.class, EnderCrystal::isShowingBottom, EnderCrystal::setShowingBottom);

    public static final EntitySnapshotValue<EnderDragon, EnderDragon.Phase> PHASE = new EntitySnapshotValue<>(
            EnderDragon.class, EnderDragon::getPhase, EnderDragon::setPhase);

    public static final EntitySnapshotValue<Endermite, Boolean> IS_PLAYER_SPAWNED = new EntitySnapshotValue<>(
            Endermite.class, Endermite::isPlayerSpawned, Endermite::setPlayerSpawned);

    public static final EntitySnapshotValue<EnderSignal, Integer> DESPAWN_TIMER = new EntitySnapshotValue<>(
            EnderSignal.class, EnderSignal::getDespawnTimer, EnderSignal::setDespawnTimer);
    public static final EntitySnapshotValue<EnderSignal, Boolean> WILL_DROP_ITEM = new EntitySnapshotValue<>(
            EnderSignal.class, EnderSignal::getDropItem, EnderSignal::setDropItem);
    public static final EntitySnapshotValue<EnderSignal, Location> TARGET_LOCATION = new EntitySnapshotValue<>(
            EnderSignal.class, EnderSignal::getTargetLocation, EnderSignal::setTargetLocation);

    public static final EntitySnapshotValue<EvokerFangs, LivingEntity> FANG_OWNER = new EntitySnapshotValue<>(
            EvokerFangs.class, EvokerFangs::getOwner, EvokerFangs::setOwner);
    public static final EntitySnapshotValue<ExperienceOrb, Integer> EXPERIENCE = new EntitySnapshotValue<>(
            ExperienceOrb.class, ExperienceOrb::getExperience, ExperienceOrb::setExperience);

    public static final EntitySnapshotValue<Explosive, Boolean> IS_INCENDIARY = new EntitySnapshotValue<>(
            Explosive.class, Explosive::isIncendiary, Explosive::setIsIncendiary);
    public static final EntitySnapshotValue<Explosive, Float> YIELD = new EntitySnapshotValue<>(Explosive.class,
                                                                                                Explosive::getYield,
                                                                                                Explosive::setYield);

    public static final EntitySnapshotValue<FallingBlock, Boolean> FALLING_BLOCK_WILL_DROP_ITEM = new EntitySnapshotValue<>(
            FallingBlock.class, FallingBlock::getDropItem, FallingBlock::setDropItem);
    public static final EntitySnapshotValue<FallingBlock, Boolean> WILL_HURT = new EntitySnapshotValue<>(
            FallingBlock.class, FallingBlock::canHurtEntities, FallingBlock::setHurtEntities);

    public static final EntitySnapshotValue<Fireball, Vector> DIRECTION = new EntitySnapshotValue<>(Fireball.class,
                                                                                                    Fireball::getDirection,
                                                                                                    Fireball::setDirection);

    public static final EntitySnapshotValue<Firework, FireworkMeta> FIREWORK_META = new EntitySnapshotValue<>(
            Firework.class, Firework::getFireworkMeta, Firework::setFireworkMeta);
    public static final EntitySnapshotValue<Firework, Boolean> SHOT_AT_ANGLE = new EntitySnapshotValue<>(Firework.class,
                                                                                                         Firework::isShotAtAngle,
                                                                                                         Firework::setShotAtAngle);

    public static final EntitySnapshotValue<Fox, Boolean> IS_CROUCHING = new EntitySnapshotValue<>(Fox.class,
                                                                                                   Fox::isCrouching,
                                                                                                   Fox::setCrouching);
    public static final EntitySnapshotValue<Fox, AnimalTamer> FIRST_TAMER = new EntitySnapshotValue<>(Fox.class,
                                                                                                      Fox::getFirstTrustedPlayer,
                                                                                                      Fox::setFirstTrustedPlayer);
    public static final EntitySnapshotValue<Fox, Fox.Type> FOX_TYPE = new EntitySnapshotValue<>(Fox.class,
                                                                                                Fox::getFoxType,
                                                                                                Fox::setFoxType);
    public static final EntitySnapshotValue<Fox, AnimalTamer> SECOND_TAMER = new EntitySnapshotValue<>(Fox.class,
                                                                                                       Fox::getSecondTrustedPlayer,
                                                                                                       Fox::setSecondTrustedPlayer);
    public static final EntitySnapshotValue<Fox, Boolean> IS_SLEEPING = new EntitySnapshotValue<>(Fox.class,
                                                                                                  Fox::isSleeping,
                                                                                                  Fox::setSleeping);

    private final Class<E> clazz;
    private String id;

    public EntitySnapshotValue(Class<E> clazz, O value, Function<E, O> getter, BiConsumer<E, O> setter) {
        super(value, getter, setter);
        this.clazz = clazz;
    }

    public EntitySnapshotValue(Class<E> clazz, Function<E, O> getter, BiConsumer<E, O> setter) {
        this(clazz, null, getter, setter);
    }

    public EntitySnapshotValue(Function<E, O> getter, BiConsumer<E, O> setter) {
        this((Class<E>) Entity.class, null, getter, setter);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean canApplyTo(Object obj) {
        return this.clazz.isInstance(obj);
    }

    @Override
    public EntitySnapshotValue<E, O> clone() {
        return new EntitySnapshotValue<>(this.clazz, this.value, this.getter, this.setter);
    }

    public static <E extends Entity> Set<EntitySnapshotValue<? super E, ?>> getSnapshotValues(E entity) {
        Set<EntitySnapshotValue<?, ?>> values = Stream
                .of(EntitySnapshotValue.class.getDeclaredFields())
                .parallel()
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> f.getType().isAssignableFrom(EntitySnapshotValue.class))
                .map(f -> {
                    try {
                        EntitySnapshotValue<?, ?> snapshot = (EntitySnapshotValue<?, ?>) f.get(null);
                        snapshot.setId(f.getName());
                        return snapshot;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
        return values
                .parallelStream()
                .filter(v -> v.canApplyTo(entity))
                .map(e -> (EntitySnapshotValue<? super E, ?>) e)
                .collect(Collectors.toSet());
    }
}
