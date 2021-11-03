package org.core.implementation.bukkit.world.position.block.entity.banner;

import org.core.implementation.bukkit.world.position.block.entity.banner.pattern.BPatternLayersSnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.blocktypes.post.BlockTypes1V13;
import org.core.world.position.block.entity.banner.BannerTileEntity;
import org.core.world.position.block.entity.banner.BannerTileEntitySnapshot;
import org.core.world.position.block.entity.banner.LiveBannerTileEntity;
import org.core.world.position.block.entity.banner.pattern.PatternLayersSnapshot;

import java.util.Collection;

public class BBannerTileEntitySnapshot implements BannerTileEntitySnapshot {

    protected final PatternLayersSnapshot layers;

    public BBannerTileEntitySnapshot() {
        this.layers = new BPatternLayersSnapshot();
    }

    public BBannerTileEntitySnapshot(BannerTileEntity bannerTileEntity) {
        this.layers = bannerTileEntity.getLayers().createSnapshot();
    }

    @Override
    public LiveBannerTileEntity apply(LiveBannerTileEntity lbte) {
        lbte.getLayers().removeLayers().addLayers(this.layers.getLayers());
        return lbte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return BlockTypes1V13.BLACK_BANNER.getLike();
    }

    @Override
    public PatternLayersSnapshot getLayers() {
        return this.layers;
    }

    @Override
    public BannerTileEntitySnapshot getSnapshot() {
        return new BBannerTileEntitySnapshot(this);
    }
}
