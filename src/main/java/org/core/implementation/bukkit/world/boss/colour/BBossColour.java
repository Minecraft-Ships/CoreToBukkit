package org.core.implementation.bukkit.world.boss.colour;

import org.bukkit.boss.BarColor;
import org.core.world.boss.colour.BossColour;

public class BBossColour implements BossColour {

    private final BarColor colour;

    public BBossColour(BarColor colour) {
        this.colour = colour;
    }

    public BarColor getBukkitColor() {
        return this.colour;
    }

    @Override
    public String getId() {
        return "minecraft." + this.colour.name().toLowerCase();
    }

    @Override
    public String getName() {
        return this.colour.name();
    }
}
