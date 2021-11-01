package org.core.implementation.bukkit.inventory.inventories.live.entity;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.EntityEquipment;
import org.core.entity.LiveEntity;
import org.core.entity.living.hostile.undead.Zombie;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.inventory.inventories.snapshot.entity.BClassicZombieInventorySnapshot;
import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.core.inventory.inventories.live.entity.LiveZombieInventory;
import org.core.inventory.inventories.snapshots.entity.ZombieInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.ArmorPart;
import org.core.inventory.parts.Slot;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BLiveZombieInventory<Z extends Zombie<LiveEntity> & LiveEntity> implements LiveZombieInventory<Z> {

    private class ZombieArmorSlots implements ArmorPart {

        private class ZombieHelmetSlot implements Slot {

            @Override
            public Optional<Integer> getPosition() {
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> getItem() {
                return
                        BLiveZombieInventory
                                .this
                                .getEquipment()
                                .map(e -> new BLiveItemStack(e.getHelmet()));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack) stack).getBukkitItem();
                BLiveZombieInventory.this.getEquipment().ifPresent(e -> e.setHelmet(stack2));
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
                return
                        BLiveZombieInventory
                                .this
                                .getEquipment()
                                .map(e -> new BLiveItemStack(e.getChestplate()));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack) stack).getBukkitItem();
                BLiveZombieInventory.this.getEquipment().ifPresent(e -> e.setChestplate(stack2));
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
                return
                        BLiveZombieInventory
                                .this
                                .getEquipment()
                                .map(e -> new BLiveItemStack(e.getLeggings()));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack) stack).getBukkitItem();
                BLiveZombieInventory.this.getEquipment().ifPresent(e -> e.setLeggings(stack2));
                return this;
            }

        }

        private class ZombieBootsSlot implements Slot {

            @Override
            public Optional<Integer> getPosition() {
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> getItem() {
                return
                        BLiveZombieInventory
                                .this
                                .getEquipment()
                                .map(e -> new BLiveItemStack(e.getBoots()));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack) stack).getBukkitItem();
                BLiveZombieInventory.this.getEquipment().ifPresent(e -> e.setBoots(stack2));
                return this;
            }

        }

        protected ZombieHelmetSlot helmet = new ZombieHelmetSlot();
        protected ZombieArmorSlot armor = new ZombieArmorSlot();
        protected ZombieLeggingsSlot legging = new ZombieLeggingsSlot();
        protected ZombieBootsSlot boots = new ZombieBootsSlot();

        @Override
        public ZombieHelmetSlot getHelmetSlot() {
            return this.helmet;
        }

        @Override
        public ZombieArmorSlot getArmorSlot() {
            return this.armor;
        }

        @Override
        public ZombieLeggingsSlot getLeggingsSlot() {
            return this.legging;
        }

        @Override
        public ZombieBootsSlot getShoesSlot() {
            return this.boots;
        }
    }

    private class ZombieMainHand implements Slot {

        @Override
        public Optional<Integer> getPosition() {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> getItem() {
            return
                    BLiveZombieInventory
                            .this
                            .getEquipment()
                            .map(e -> new BLiveItemStack(e.getItemInMainHand()));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack) stack).getBukkitItem();
            BLiveZombieInventory.this.getEquipment().ifPresent(e -> e.setItemInMainHand(stack2));
            return this;
        }
    }

    private class ZombieSecondHand implements Slot {

        @Override
        public Optional<Integer> getPosition() {
            return Optional.empty();
        }


        @Override
        public Optional<ItemStack> getItem() {
            return
                    BLiveZombieInventory
                            .this
                            .getEquipment()
                            .map(e -> new BLiveItemStack(e.getItemInOffHand()));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack) stack).getBukkitItem();
            BLiveZombieInventory.this.getEquipment().ifPresent(e -> e.setItemInOffHand(stack2));
            return this;
        }
    }

    private final Z zombie;
    private final ZombieArmorSlots armor = new ZombieArmorSlots();
    private final ZombieSecondHand second = new ZombieSecondHand();
    private final ZombieMainHand main = new ZombieMainHand();

    public BLiveZombieInventory(Z zombie) {
        this.zombie = zombie;
    }

    @Override
    public Set<Slot> getSlots() {
        Set<Slot> set = new HashSet<>(this.armor.getSlots());
        set.add(this.main);
        set.add(this.second);
        return set;
    }

    @Override
    public ZombieInventorySnapshot<Z> createSnapshot() {
        return new BClassicZombieInventorySnapshot<Z>(this);
    }

    @Override
    public ZombieArmorSlots getArmor() {
        return this.armor;
    }

    @Override
    public ZombieMainHand getMainHoldingItem() {
        return this.main;
    }

    @Override
    public ZombieSecondHand getOffHoldingItem() {
        return this.second;
    }

    @Override
    public Optional<Z> getAttachedEntity() {
        return Optional.of(this.zombie);
    }

    private Optional<EntityEquipment> getEquipment() {
        return Optional.ofNullable(((BLiveEntity<org.bukkit.entity.Zombie>)this.zombie).getBukkitEntity().getEquipment());
    }

}
