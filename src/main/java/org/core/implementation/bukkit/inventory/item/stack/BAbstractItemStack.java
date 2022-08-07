package org.core.implementation.bukkit.inventory.item.stack;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.core.adventureText.AText;
import org.core.implementation.bukkit.inventory.item.BItemType;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.item.stack.ItemStackSnapshot;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BAbstractItemStack implements ItemStack {

    protected final org.bukkit.inventory.ItemStack stack;

    public BAbstractItemStack(org.bukkit.inventory.ItemStack stack) {
        this.stack = stack;
    }

    public org.bukkit.inventory.ItemStack getBukkitItem() {
        return this.stack;
    }

    @Override
    public @NotNull ItemType getType() {
        return new BItemType(this.stack.getType());
    }

    @Override
    public int getQuantity() {
        return this.stack.getAmount();
    }

    @Override
    public List<AText> getLoreText() {
        ItemMeta meta = this.stack.getItemMeta();
        if (meta == null) {
            return Collections.emptyList();
        }
        List<String> lore = meta.getLore();
        if (lore == null) {
            return Collections.emptyList();
        }
        return lore.stream().map(AText::ofLegacy).collect(Collectors.toList());
    }

    @Override
    public org.core.inventory.item.stack.ItemStack setLoreText(Collection<? extends AText> lore) {
        ItemMeta meta = this.stack.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(this.stack.getType());
            if (meta == null) {
                throw new IllegalStateException("Could not create ItemMeta for " + this.stack.getType().name());
            }
        }
        meta.setLore(lore.stream().map(AText::toLegacy).collect(Collectors.toList()));
        this.stack.setItemMeta(meta);
        return this;
    }

    @Override
    public ItemStackSnapshot createSnapshot() {
        return new BItemStackSnapshot(this);
    }
}
