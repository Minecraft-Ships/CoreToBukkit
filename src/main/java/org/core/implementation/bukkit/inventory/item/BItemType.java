package org.core.implementation.bukkit.inventory.item;

import org.core.implementation.bukkit.inventory.item.stack.BItemStackSnapshot;
import org.core.implementation.bukkit.world.position.block.BBlockType;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.stack.ItemStackSnapshot;
import org.core.world.position.block.BlockType;

import java.util.Optional;

public class BItemType implements ItemType {

    protected final org.bukkit.Material material;

    public BItemType(org.bukkit.Material material) {
        this.material = material;
    }

    public org.bukkit.Material getBukkitMaterial() {
        return this.material;
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof BBlockType type) {
            if (type.getBukkitMaterial() == this.material) {
                return true;
            }
        }
        if (object instanceof BItemType type) {
            return type.getBukkitMaterial() == this.material;
        }
        return false;
    }

    @Override
    public ItemStackSnapshot getDefaultItemStack() {
        org.bukkit.inventory.ItemStack stack = new org.bukkit.inventory.ItemStack(this.material, 1);
        return new BItemStackSnapshot(stack);
    }

    @Override
    public Optional<BlockType> getBlockType() {
        if (!this.material.isBlock()) {
            return Optional.empty();
        }
        return Optional.of(new BBlockType(this.material));
    }

    @Override
    public String getId() {
        return this.material.getKey().toString();
    }

    @Override
    public String getName() {
        return this.material.name();
    }
}
