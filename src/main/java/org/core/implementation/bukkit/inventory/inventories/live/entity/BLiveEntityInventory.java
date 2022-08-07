package org.core.implementation.bukkit.inventory.inventories.live.entity;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.core.entity.EntitySnapshot;
import org.core.entity.LiveEntity;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.core.inventory.inventories.BasicEntityInventory;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.ArmorPart;
import org.core.inventory.parts.Slot;

import java.util.Optional;

public abstract class BLiveEntityInventory<E extends LiveEntity, S extends EntitySnapshot<E>>
        implements BasicEntityInventory<E> {

    protected final E entity;
    protected final BLiveEntityInventory<E, S>.ZombieArmorPart parts = new BLiveEntityInventory<E, S>.ZombieArmorPart();
    protected final BLiveEntityInventory<E, S>.ZombieMainHandSlot mainHand =
            new BLiveEntityInventory<E, S>.ZombieMainHandSlot();
    protected final BLiveEntityInventory<E, S>.ZombieSecondaryHandSlot secondaryHand =
            new BLiveEntityInventory<E, S>.ZombieSecondaryHandSlot();
    public BLiveEntityInventory(E entity) {
        this.entity = entity;
    }

    private org.bukkit.entity.LivingEntity getBukkitEntity() {
        return ((BLiveEntity<? extends LivingEntity>) this.entity).getBukkitEntity();
    }

    @Override
    public BLiveEntityInventory<E, S>.ZombieArmorPart getArmor() {
        return this.parts;
    }

    @Override
    public Slot getMainHoldingItem() {
        return this.mainHand;
    }

    @Override
    public Slot getOffHoldingItem() {
        return this.secondaryHand;
    }

    private Optional<EntityEquipment> getEquipment() {
        return Optional.ofNullable(this.getBukkitEntity().getEquipment());
    }

    private class ZombieArmorPart implements ArmorPart {

        final BLiveEntityInventory<E, S>.ZombieArmorPart.ZombieHelmetSlot helmet =
                new BLiveEntityInventory<E, S>.ZombieArmorPart.ZombieHelmetSlot();
        final BLiveEntityInventory<E, S>.ZombieArmorPart.ZombieArmorSlot armor =
                new BLiveEntityInventory<E, S>.ZombieArmorPart.ZombieArmorSlot();
        final BLiveEntityInventory<E, S>.ZombieArmorPart.ZombieLeggingsSlot leggings =
                new BLiveEntityInventory<E, S>.ZombieArmorPart.ZombieLeggingsSlot();
        final BLiveEntityInventory<E, S>.ZombieArmorPart.ZombieShoesSlot shoes =
                new BLiveEntityInventory<E, S>.ZombieArmorPart.ZombieShoesSlot();

        @Override
        public Slot getHelmetSlot() {
            return this.helmet;
        }

        @Override
        public Slot getArmorSlot() {
            return this.armor;
        }

        @Override
        public Slot getLeggingsSlot() {
            return this.leggings;
        }

        @Override
        public Slot getShoesSlot() {
            return this.shoes;
        }

        private class ZombieHelmetSlot implements Slot {

            @Override
            public Optional<Integer> getPosition() {
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> getItem() {
                return BLiveEntityInventory.this.getEquipment().map(e -> new BLiveItemStack(e.getHelmet()));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack item = ((BAbstractItemStack) stack).getBukkitItem();
                BLiveEntityInventory.this.getEquipment().ifPresent(e -> e.setHelmet(item));
                return this;
            }
        }

        private class ZombieArmorSlot implements Slot {

            @Override
            public Optional<Integer> getPosition() {
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> getItem() {
                return BLiveEntityInventory.this.getEquipment().map(e -> new BLiveItemStack(e.getChestplate()));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack item = ((BAbstractItemStack) stack).getBukkitItem();
                BLiveEntityInventory.this.getEquipment().ifPresent(e -> e.setChestplate(item));
                return this;
            }
        }

        private class ZombieLeggingsSlot implements Slot {

            @Override
            public Optional<Integer> getPosition() {
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> getItem() {
                return BLiveEntityInventory.this.getEquipment().map(e -> new BLiveItemStack(e.getLeggings()));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack item = ((BAbstractItemStack) stack).getBukkitItem();
                BLiveEntityInventory.this.getEquipment().ifPresent(e -> e.setLeggings(item));
                return this;
            }
        }

        private class ZombieShoesSlot implements Slot {

            @Override
            public Optional<Integer> getPosition() {
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> getItem() {
                return BLiveEntityInventory.this.getEquipment().map(e -> new BLiveItemStack(e.getBoots()));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack item = ((BAbstractItemStack) stack).getBukkitItem();
                BLiveEntityInventory.this.getEquipment().ifPresent(e -> e.setBoots(item));
                return this;
            }
        }
    }

    private class ZombieMainHandSlot implements Slot {

        @Override
        public Optional<Integer> getPosition() {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> getItem() {
            return BLiveEntityInventory.this.getEquipment().map(e -> new BLiveItemStack(e.getItemInMainHand()));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.inventory.ItemStack item = ((BAbstractItemStack) stack).getBukkitItem();
            BLiveEntityInventory.this.getEquipment().ifPresent(e -> e.setItemInMainHand(item));
            return this;
        }
    }

    private class ZombieSecondaryHandSlot implements Slot {

        @Override
        public Optional<Integer> getPosition() {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> getItem() {
            return BLiveEntityInventory.this.getEquipment().map(e -> new BLiveItemStack(e.getItemInOffHand()));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.inventory.ItemStack item = ((BAbstractItemStack) stack).getBukkitItem();
            BLiveEntityInventory.this.getEquipment().ifPresent(e -> e.setItemInOffHand(item));
            return this;
        }
    }
}
