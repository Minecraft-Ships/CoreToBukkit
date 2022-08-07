package org.core.implementation.bukkit.inventory.item.data.dye;

import org.core.inventory.item.data.dye.DyeType;

public class BItemDyeType implements DyeType {

    protected final org.bukkit.DyeColor dye;

    public BItemDyeType(org.bukkit.DyeColor dye) {
        this.dye = dye;
    }

    public org.bukkit.DyeColor getBukkitDye() {
        return this.dye;
    }

    @Override
    public String getId() {
        return "minecraft:" + this.getName().toLowerCase();
    }

    @Override
    public String getName() {
        return this.dye.name();
    }
}
