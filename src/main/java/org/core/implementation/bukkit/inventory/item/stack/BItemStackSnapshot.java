package org.core.implementation.bukkit.inventory.item.stack;

import org.bukkit.inventory.ItemStack;
import org.core.inventory.item.stack.ItemStackSnapshot;

public class BItemStackSnapshot extends BAbstractItemStack implements ItemStackSnapshot {

    public BItemStackSnapshot(BAbstractItemStack stack) {
        this(stack.getBukkitItem());
    }

    public BItemStackSnapshot(ItemStack stack) {
        super(stack);
    }

    @Override
    public org.core.inventory.item.stack.ItemStack copy() {
        return new BItemStackSnapshot(this.stack.clone());
    }

    @Override
    public org.core.inventory.item.stack.ItemStack copyWithQuantity(int quantity) {
        org.bukkit.inventory.ItemStack item = this.stack.clone();
        item.setAmount(quantity);
        return new BItemStackSnapshot(item);
    }
}
