package org.core.implementation.bukkit.world.position.block.entity.banner.pattern;

import org.core.inventory.item.data.dye.DyeType;
import org.core.world.position.block.entity.banner.pattern.PatternLayer;
import org.core.world.position.block.entity.banner.pattern.PatternLayerType;

public class BPatternLayerType implements PatternLayerType {

    org.bukkit.block.banner.PatternType type;

    public BPatternLayerType(org.bukkit.block.banner.PatternType type){
        this.type = type;
    }

    @Override
    public PatternLayer createLayer(DyeType type) {
        return null;
    }

    @Override
    public String getId() {
        return "minecraft:" + getName().toLowerCase();
    }

    @Override
    public String getName() {
        return this.type.name();
    }
}
