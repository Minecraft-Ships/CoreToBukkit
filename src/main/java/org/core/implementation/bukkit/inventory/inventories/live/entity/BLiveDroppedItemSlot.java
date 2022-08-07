package org.core.implementation.bukkit.inventory.inventories.live.entity;

import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;

import java.util.Optional;

public class BLiveDroppedItemSlot implements Slot {

    protected final org.bukkit.entity.Item item;

    public BLiveDroppedItemSlot(org.bukkit.entity.Item item) {
        this.item = item;
    }

    @Override
    public Optional<Integer> getPosition() {
        return Optional.empty();
    }

    @Override
    public Optional<ItemStack> getItem() {
        org.bukkit.inventory.ItemStack stack = this.item.getItemStack();
        if (stack == null) {
            return Optional.empty();
        }
        return Optional.of(new BLiveItemStack(stack));
    }

    @Override
    public Slot setItem(ItemStack stack) {
        if (stack == null) {
            return this;
        }
        org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack) stack).getBukkitItem();
        this.item.setItemStack(stack2);
        return this;
    }
}
