package org.core.implementation.bukkit.inventory.inventories.snapshot.entity;

import org.bukkit.entity.LivingEntity;
import org.core.entity.LiveEntity;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.inventory.inventories.snapshots.entity.EntityInventorySnapshot;

public interface BEntityInventorySnapshot <E extends LiveEntity> extends EntityInventorySnapshot<E> {

    @Override
    default void apply(E entity) {
        BLiveEntity<? extends LivingEntity> entity1 = (BLiveEntity)entity;
        org.bukkit.inventory.EntityEquipment equipment = entity1.getBukkitEntity().getEquipment();
        getMainHoldingItem().getItem().ifPresent(i -> equipment.setItemInMainHand(((BAbstractItemStack)i).getBukkitItem()));
        getOffHoldingItem().getItem().ifPresent(i -> equipment.setItemInOffHand(((BAbstractItemStack)i).getBukkitItem()));
        getArmor().getShoesSlot().getItem().ifPresent(i -> equipment.setBoots(((BAbstractItemStack)i).getBukkitItem()));
        getArmor().getLeggingsSlot().getItem().ifPresent(i -> equipment.setLeggings(((BAbstractItemStack)i).getBukkitItem()));
        getArmor().getArmorSlot().getItem().ifPresent(i -> equipment.setChestplate(((BAbstractItemStack)i).getBukkitItem()));
        getArmor().getHelmetSlot().getItem().ifPresent(i -> equipment.setHelmet(((BAbstractItemStack)i).getBukkitItem()));
    }
}
