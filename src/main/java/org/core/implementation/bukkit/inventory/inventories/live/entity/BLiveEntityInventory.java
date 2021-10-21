package org.core.implementation.bukkit.inventory.inventories.live.entity;

import org.core.entity.EntitySnapshot;
import org.core.entity.LiveEntity;
import org.core.entity.living.ArmoredEntity;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.core.inventory.inventories.BasicEntityInventory;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.ArmorPart;
import org.core.inventory.parts.Slot;

import java.util.Optional;

public abstract class BLiveEntityInventory<E extends LiveEntity, S extends EntitySnapshot<E>> implements BasicEntityInventory {

    private class ZombieArmorPart implements ArmorPart {

        private class ZombieHelmetSlot implements Slot {

            @Override
            public Optional<Integer> getPosition() {
                return Optional.empty();
            }

            @Override
            public Optional<ItemStack> getItem() {
                org.bukkit.inventory.ItemStack item = BLiveEntityInventory.this.getBukkitEntity().getEquipment().getHelmet();
                if(item == null){
                    return Optional.empty();
                }
                return Optional.of(new BLiveItemStack(item));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack item = ((BAbstractItemStack)stack).getBukkitItem();
                BLiveEntityInventory.this.getBukkitEntity().getEquipment().setHelmet(item);
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
                org.bukkit.inventory.ItemStack item = BLiveEntityInventory.this.getBukkitEntity().getEquipment().getChestplate();
                if(item == null){
                    return Optional.empty();
                }
                return Optional.of(new BLiveItemStack(item));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack item = ((BAbstractItemStack)stack).getBukkitItem();
                BLiveEntityInventory.this.getBukkitEntity().getEquipment().setChestplate(item);
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
                org.bukkit.inventory.ItemStack item = BLiveEntityInventory.this.getBukkitEntity().getEquipment().getLeggings();
                if(item == null){
                    return Optional.empty();
                }
                return Optional.of(new BLiveItemStack(item));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack item = ((BAbstractItemStack)stack).getBukkitItem();
                BLiveEntityInventory.this.getBukkitEntity().getEquipment().setLeggings(item);
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
                org.bukkit.inventory.ItemStack item = BLiveEntityInventory.this.getBukkitEntity().getEquipment().getBoots();
                if(item == null){
                    return Optional.empty();
                }
                return Optional.of(new BLiveItemStack(item));
            }

            @Override
            public Slot setItem(ItemStack stack) {
                org.bukkit.inventory.ItemStack item = ((BAbstractItemStack)stack).getBukkitItem();
                BLiveEntityInventory.this.getBukkitEntity().getEquipment().setBoots(item);
                return this;
            }
        }

        BLiveEntityInventory.ZombieArmorPart.ZombieHelmetSlot helmet = new BLiveEntityInventory.ZombieArmorPart.ZombieHelmetSlot();
        BLiveEntityInventory.ZombieArmorPart.ZombieArmorSlot armor = new BLiveEntityInventory.ZombieArmorPart.ZombieArmorSlot();
        BLiveEntityInventory.ZombieArmorPart.ZombieLeggingsSlot leggings = new BLiveEntityInventory.ZombieArmorPart.ZombieLeggingsSlot();
        BLiveEntityInventory.ZombieArmorPart.ZombieShoesSlot shoes = new BLiveEntityInventory.ZombieArmorPart.ZombieShoesSlot();

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
    }

    private class ZombieMainHandSlot implements Slot {

        @Override
        public Optional<Integer> getPosition() {
            return Optional.empty();
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack item = BLiveEntityInventory.this.getBukkitEntity().getEquipment().getItemInMainHand();
            if(item == null){
                return Optional.empty();
            }
            return Optional.of(new BLiveItemStack(item));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.inventory.ItemStack item = ((BAbstractItemStack)stack).getBukkitItem();
            BLiveEntityInventory.this.getBukkitEntity().getEquipment().setItemInMainHand(item);
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
            org.bukkit.inventory.ItemStack item = BLiveEntityInventory.this.getBukkitEntity().getEquipment().getItemInOffHand();
            if(item == null){
                return Optional.empty();
            }
            return Optional.of(new BLiveItemStack(item));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.inventory.ItemStack item = ((BAbstractItemStack)stack).getBukkitItem();
            BLiveEntityInventory.this.getBukkitEntity().getEquipment().setItemInOffHand(item);
            return this;
        }
    }

    protected ArmoredEntity entity;
    protected BLiveEntityInventory.ZombieArmorPart parts = new BLiveEntityInventory.ZombieArmorPart();
    protected BLiveEntityInventory.ZombieMainHandSlot mainHand = new BLiveEntityInventory.ZombieMainHandSlot();
    protected BLiveEntityInventory.ZombieSecondaryHandSlot secondaryHand = new BLiveEntityInventory.ZombieSecondaryHandSlot();


    public BLiveEntityInventory(ArmoredEntity entity){
        this.entity = entity;
    }

    private org.bukkit.entity.LivingEntity getBukkitEntity(){
        return (org.bukkit.entity.LivingEntity)((BLiveEntity)this.entity).getBukkitEntity();
    }

    @Override
    public ArmorPart getArmor() {
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
}
