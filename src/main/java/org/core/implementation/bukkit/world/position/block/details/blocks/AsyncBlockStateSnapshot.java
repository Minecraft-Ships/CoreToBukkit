package org.core.implementation.bukkit.world.position.block.details.blocks;

import org.bukkit.block.data.BlockData;
import org.core.implementation.bukkit.world.position.impl.async.BAsyncBlockPosition;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.async.ASyncBlockPosition;

public class AsyncBlockStateSnapshot extends BBlockDetails implements BlockSnapshot.AsyncBlockSnapshot {

    protected ASyncBlockPosition position;

    public AsyncBlockStateSnapshot(BAsyncBlockPosition position) {
        super(position);
        this.position = position;
    }

    public AsyncBlockStateSnapshot(ASyncBlockPosition position, BlockData data) {
        super(data, true);
        this.position = position;
    }

    public AsyncBlockStateSnapshot(BBlockDetails details, ASyncBlockPosition position) {
        super(details);
        this.position = position;
    }

    @Override
    public ASyncBlockPosition getPosition() {
        return this.position;
    }

    @Override
    public AsyncBlockStateSnapshot createCopyOf() {
        return new AsyncBlockStateSnapshot(this, this.position);
    }
}
