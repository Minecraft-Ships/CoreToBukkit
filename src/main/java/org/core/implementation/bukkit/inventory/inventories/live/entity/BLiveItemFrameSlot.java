package org.core.implementation.bukkit.inventory.inventories.live.entity;

import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;

import java.util.Optional;

public class BLiveItemFrameSlot implements Slot {

    protected final org.bukkit.entity.ItemFrame frame;

    public BLiveItemFrameSlot(org.bukkit.entity.ItemFrame frame) {
        this.frame = frame;
    }

    @Override
    public Optional<Integer> getPosition() {
        return Optional.empty();
    }

    @Override
    public Optional<ItemStack> getItem() {
        org.bukkit.inventory.ItemStack stack = this.frame.getItem();
        if (stack == null) {
            return Optional.empty();
        }
        return Optional.of(new BLiveItemStack(stack));
    }

    @Override
    public Slot setItem(ItemStack stack) {
        if (stack == null) {
            this.frame.setItem(null, false);
            return this;
        }
        org.bukkit.inventory.ItemStack stack2 = ((BAbstractItemStack) stack).getBukkitItem();
        this.frame.setItem(stack2, false);
        return this;
    }
}
