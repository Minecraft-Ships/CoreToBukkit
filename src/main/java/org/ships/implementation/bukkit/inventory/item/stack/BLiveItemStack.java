package org.ships.implementation.bukkit.inventory.item.stack;

import org.bukkit.inventory.ItemStack;
import org.core.inventory.item.stack.LiveItemStack;

public class BLiveItemStack extends BAbstractItemStack implements LiveItemStack {

    public BLiveItemStack(ItemStack stack) {
        super(stack);
    }

    @Override
    public org.core.inventory.item.stack.ItemStack copy() {
        return new BLiveItemStack(this.stack.clone());
    }

    @Override
    public org.core.inventory.item.stack.ItemStack copyWithQuantity(int quantity) {
        org.bukkit.inventory.ItemStack item = this.stack.clone();
        item.setAmount(quantity);
        return new BLiveItemStack(item);
    }
}
