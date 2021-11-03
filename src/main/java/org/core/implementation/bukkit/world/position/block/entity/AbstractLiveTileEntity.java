package org.core.implementation.bukkit.world.position.block.entity;

import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.impl.sync.SyncBlockPosition;

public abstract class AbstractLiveTileEntity implements LiveTileEntity {

    protected final org.bukkit.block.BlockState state;

    public AbstractLiveTileEntity(org.bukkit.block.BlockState state) {
        this.state = state;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return new BBlockPosition(this.state.getBlock());
    }
}
