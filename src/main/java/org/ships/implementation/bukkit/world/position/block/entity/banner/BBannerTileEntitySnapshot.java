package org.ships.implementation.bukkit.world.position.block.entity.banner;

import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.entity.banner.BannerTileEntity;
import org.core.world.position.block.entity.banner.BannerTileEntitySnapshot;
import org.core.world.position.block.entity.banner.LiveBannerTileEntity;
import org.core.world.position.block.entity.banner.pattern.PatternLayersSnapshot;
import org.ships.implementation.bukkit.world.position.block.entity.banner.pattern.BPatternLayersSnapshot;

import java.util.Collection;

public class BBannerTileEntitySnapshot implements BannerTileEntitySnapshot {

    protected PatternLayersSnapshot layers;

    public BBannerTileEntitySnapshot(){
        this.layers = new BPatternLayersSnapshot();
    }

    public BBannerTileEntitySnapshot(BannerTileEntity bannerTileEntity){
        this.layers = bannerTileEntity.getLayers().createSnapshot();
    }

    @Override
    public LiveBannerTileEntity apply(LiveBannerTileEntity lbte) {
        lbte.getLayers().removeLayers().addLayers(this.layers.getLayers());
        return lbte;
    }

    @Override
    public Collection<BlockType> getSupportedBlocks() {
        return BlockTypes.BLACK_BANNER.get().getLike();
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
