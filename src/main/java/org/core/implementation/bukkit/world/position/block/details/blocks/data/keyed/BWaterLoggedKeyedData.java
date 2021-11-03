package org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed;

import org.core.world.position.block.details.data.keyed.WaterLoggedKeyedData;

import java.util.Optional;

public class BWaterLoggedKeyedData implements WaterLoggedKeyedData {

    final org.bukkit.block.data.Waterlogged data;

    public BWaterLoggedKeyedData(org.bukkit.block.data.Waterlogged data) {
        this.data = data;
    }

    @Override
    public Optional<Boolean> getData() {
        return Optional.of(this.data.isWaterlogged());
    }

    @Override
    public void setData(Boolean value) {
        this.data.setWaterlogged(value);
    }
}
