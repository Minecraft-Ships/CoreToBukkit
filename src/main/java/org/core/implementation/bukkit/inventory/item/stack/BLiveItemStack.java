package org.core.implementation.bukkit.inventory.item.stack;

import org.bukkit.inventory.ItemStack;
import org.core.inventory.item.stack.LiveItemStack;
import org.core.inventory.item.stack.data.ItemStackData;

import java.util.Optional;

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

    @Override
    public Optional<ItemStackData> getStackData() {
        throw new RuntimeException("Not implemented yet");

    }

    @Override
    public void setStackData(ItemStackData data) {
        throw new RuntimeException("Not implemented yet");

    }
}
