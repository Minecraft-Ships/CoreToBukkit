package org.core.implementation.bukkit.world.position.block.entity.banner.pattern;

import org.core.world.position.block.entity.banner.pattern.PatternLayer;
import org.core.world.position.block.entity.banner.pattern.PatternLayers;
import org.core.world.position.block.entity.banner.pattern.PatternLayersSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class BPatternLayersSnapshot implements PatternLayersSnapshot {

    protected final List<PatternLayer> list;

    public BPatternLayersSnapshot(PatternLayer... list) {
        this(Arrays.asList(list));
    }

    public BPatternLayersSnapshot(List<PatternLayer> list) {
        this.list = list;
    }

    @Override
    public List<PatternLayer> getLayers() {
        return this.list;
    }

    @Override
    public PatternLayers addLayers(Collection<? extends PatternLayer> layers) {
        this.list.addAll(layers);
        return this;
    }

    @Override
    public PatternLayers addLayer(int A, PatternLayer layer) {
        this.list.add(A, layer);
        return this;
    }

    @Override
    public PatternLayers removeLayer(int layer) {
        this.list.remove(layer);
        return this;
    }

    @Override
    public PatternLayers removeLayers() {
        this.list.clear();
        return this;
    }

    @Override
    public PatternLayersSnapshot createSnapshot() {
        return new BPatternLayersSnapshot(new ArrayList<>(this.list));
    }
}
