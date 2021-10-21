package org.core.implementation.bukkit.inventory.inventories.live.entity;

import org.core.entity.living.hostile.undead.classic.LiveClassicZombie;
import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.core.inventory.inventories.live.entity.LiveZombieInventory;
import org.core.inventory.inventories.snapshots.entity.ZombieInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.ArmorPart;
import org.core.inventory.parts.Slot;
import org.core.implementation.bukkit.entity.living.hostile.undead.classic.live.BLiveZombie;
import org.core.implementation.bukkit.inventory.inventories.snapshot.entity.BClassicZombieInventorySnapshot;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BLiveZombieInventory implements LiveZombieInventory {

    private class ZombieArmorSlots implements ArmorPart {

        private class ZombieHelmetSlot implements Slot {

            @Override
            public Optional<Integer> getPosition() {
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> getItem() {
                org.bukkit.inventory.ItemStack stack = BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().getHelmet();
                if(stack == null){
                    return Optional.empty();
                }
                return Optional.of(new BLiveItemStack(stack));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack)stack).getBukkitItem();
                BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().setHelmet(stack2);
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
                org.bukkit.inventory.ItemStack stack = BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().getChestplate();
                if(stack == null){
                    return Optional.empty();
                }
                return Optional.of(new BLiveItemStack(stack));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack)stack).getBukkitItem();
                BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().setChestplate(stack2);
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
                org.bukkit.inventory.ItemStack stack = BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().getLeggings();
                if(stack == null){
                    return Optional.empty();
                }
                return Optional.of(new BLiveItemStack(stack));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack)stack).getBukkitItem();
                BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().setLeggings(stack2);
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
                org.bukkit.inventory.ItemStack stack = BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().getBoots();
                if(stack == null){
                    return Optional.empty();
                }
                return Optional.of(new BLiveItemStack(stack));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack)stack).getBukkitItem();
                BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().setBoots(stack2);
                return this;
            }

        }

        protected ZombieHelmetSlot helmet = new ZombieHelmetSlot();
        protected ZombieArmorSlot armor = new ZombieArmorSlot();
        protected ZombieLeggingsSlot legging = new ZombieLeggingsSlot();
        protected ZombieBootsSlot boots = new ZombieBootsSlot();

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
            return this.legging;
        }

        @Override
        public Slot getShoesSlot() {
            return this.boots;
        }
    }

    private class ZombieMainHand implements Slot{

        @Override
        public Optional<Integer> getPosition() {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack stack = BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().getItemInMainHand();
            if(stack == null){
                return Optional.empty();
            }
            return Optional.of(new BLiveItemStack(stack));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().setItemInMainHand(((BAbstractItemStack)stack).getBukkitItem());
            return this;
        }
    }

    private class ZombieSecondHand implements Slot{

        @Override
        public Optional<Integer> getPosition() {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack stack = BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().getItemInOffHand();
            if(stack == null){
                return Optional.empty();
            }
            return Optional.of(new BLiveItemStack(stack));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            BLiveZombieInventory.this.zombie.getBukkitEntity().getEquipment().setItemInOffHand(((BAbstractItemStack)stack).getBukkitItem());
            return this;
        }
    }

    private BLiveZombie zombie;
    private ZombieArmorSlots armor = new ZombieArmorSlots();
    private ZombieSecondHand second = new ZombieSecondHand();
    private ZombieMainHand main = new ZombieMainHand();

    public BLiveZombieInventory(BLiveZombie zombie){
        this.zombie = zombie;
    }

    @Override
    public Set<Slot> getSlots() {
        Set<Slot> set = new HashSet<>();
        set.addAll(this.armor.getSlots());
        set.add(this.main);
        set.add(this.second);
        return set;
    }

    @Override
    public ZombieInventorySnapshot createSnapshot() {
        return new BClassicZombieInventorySnapshot(this);
    }

    @Override
    public ArmorPart getArmor() {
        return this.armor;
    }

    @Override
    public Slot getMainHoldingItem() {
        return this.main;
    }

    @Override
    public Slot getOffHoldingItem() {
        return this.second;
    }

    @Override
    public Optional<LiveClassicZombie> getAttachedEntity() {
        return Optional.of(this.zombie);
    }
}
