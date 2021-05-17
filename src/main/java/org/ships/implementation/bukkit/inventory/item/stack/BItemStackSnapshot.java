package org.ships.implementation.bukkit.inventory.item.stack;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.core.adventureText.AText;
import org.core.inventory.item.stack.ItemStackSnapshot;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
