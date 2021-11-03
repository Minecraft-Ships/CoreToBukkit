package org.core.implementation.bukkit.entity.living.animal.type;

import org.core.entity.living.animal.parrot.ParrotType;

public class BParrotType implements ParrotType {

    protected final org.bukkit.entity.Parrot.Variant type;

    public BParrotType(org.bukkit.entity.Parrot.Variant variant) {
        this.type = variant;
    }

    public org.bukkit.entity.Parrot.Variant getType() {
        return this.type;
    }

    @Override
    public String getId() {
        return "minecraft:parrot_variant_" + this.type.name().toLowerCase();
    }

    @Override
    public String getName() {
        return this.type.name();
    }
}
