package org.ships.implementation.bukkit.world.position.block.entity.banner.pattern;

import org.core.inventory.item.data.dye.DyeType;
import org.core.world.position.block.entity.banner.pattern.PatternLayer;
import org.core.world.position.block.entity.banner.pattern.PatternLayerType;
import org.ships.implementation.bukkit.inventory.item.data.dye.BItemDyeType;

public class BPatternLayer implements PatternLayer {

    protected org.bukkit.block.banner.Pattern pattern;

    public BPatternLayer(org.bukkit.block.banner.Pattern pattern){
        this.pattern = pattern;
    }

    @Override
    public DyeType getColour() {
        return new BItemDyeType(this.pattern.getColor());
    }

    @Override
    public PatternLayer setColour(DyeType type) {
        org.bukkit.DyeColor colour = ((BItemDyeType)type).getBukkitDye();
        return new BPatternLayer(new org.bukkit.block.banner.Pattern(colour, this.pattern.getPattern()));
    }

    @Override
    public PatternLayerType getPattern() {
        return new BPatternLayerType(this.pattern.getPattern());
    }

    @Override
    public PatternLayer setPattern(PatternLayerType type) {
        return new BPatternLayer(new org.bukkit.block.banner.Pattern(this.pattern.getColor(), ((BPatternLayerType)type).type));
    }
}
