package org.core.implementation.bukkit.world.position.block.entity.banner.pattern;

import org.core.implementation.bukkit.inventory.item.data.dye.BItemDyeType;
import org.core.inventory.item.data.dye.DyeType;
import org.core.world.position.block.entity.banner.pattern.PatternLayer;
import org.core.world.position.block.entity.banner.pattern.PatternLayerType;

public class BPatternLayer implements PatternLayer {

    private final org.bukkit.block.banner.Pattern pattern;

    public BPatternLayer(org.bukkit.block.banner.Pattern pattern) {
        this.pattern = pattern;
    }

    public org.bukkit.block.banner.Pattern getBukkitValue() {
        return this.pattern;
    }

    @Override
    public DyeType getColour() {
        return new BItemDyeType(this.pattern.getColor());
    }

    @Override
    public PatternLayer setColour(DyeType type) {
        org.bukkit.DyeColor colour = ((BItemDyeType) type).getBukkitDye();
        return new BPatternLayer(new org.bukkit.block.banner.Pattern(colour, this.pattern.getPattern()));
    }

    @Override
    public PatternLayerType getPattern() {
        return new BPatternLayerType(this.pattern.getPattern());
    }

    @Override
    public PatternLayer setPattern(PatternLayerType type) {
        return new BPatternLayer(
                new org.bukkit.block.banner.Pattern(this.pattern.getColor(), ((BPatternLayerType) type).type));
    }
}
