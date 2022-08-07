package org.core.implementation.bukkit.world.position.block.entity.banner;

import org.bukkit.block.BlockState;
import org.core.implementation.bukkit.world.position.block.entity.AbstractLiveTileEntity;
import org.core.implementation.bukkit.world.position.block.entity.banner.pattern.BPatternLayers;
import org.core.world.position.block.entity.banner.BannerTileEntitySnapshot;
import org.core.world.position.block.entity.banner.LiveBannerTileEntity;
import org.core.world.position.block.entity.banner.pattern.PatternLayers;

public class BLiveBannerTileEntity extends AbstractLiveTileEntity implements LiveBannerTileEntity {

    public BLiveBannerTileEntity(BlockState state) {
        super(state);
    }

    @Override
    public PatternLayers getLayers() {
        return new BPatternLayers((org.bukkit.block.Banner) this.state);
    }

    @Override
    public BannerTileEntitySnapshot getSnapshot() {
        return new BBannerTileEntitySnapshot(this);
    }
}
