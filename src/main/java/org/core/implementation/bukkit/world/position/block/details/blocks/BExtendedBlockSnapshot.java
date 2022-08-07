package org.core.implementation.bukkit.world.position.block.details.blocks;

import org.bukkit.block.data.BlockData;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.details.data.keyed.TileEntityKeyedData;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Optional;

public class BExtendedBlockSnapshot extends BBlockDetails implements BlockSnapshot.SyncBlockSnapshot {

    protected final SyncBlockPosition position;

    public BExtendedBlockSnapshot(BBlockPosition position) {
        super(position);
        this.position = position;
    }

    public BExtendedBlockSnapshot(SyncBlockPosition position, BlockData data) {
        super(data, false);
        this.position = position;
    }

    private BExtendedBlockSnapshot(SyncBlockPosition position, BlockData data,
            TileEntitySnapshot<? extends TileEntity> tileEntity) {
        super(data, tileEntity, false);
        this.position = position;
    }

    @Override
    protected <T> Optional<KeyedData<T>> getKey(Class<? extends KeyedData<T>> data) {
        if (data.isAssignableFrom(TileEntityKeyedData.class)) {
            return Optional.of((KeyedData<T>) new BTileEntityKeyedData());
        }
        return super.getKey(data);
    }

    @Override
    public SyncBlockPosition getPosition() {
        return this.position;
    }

    @Override
    public BExtendedBlockSnapshot createCopyOf() {
        return new BExtendedBlockSnapshot(this.position, this.getBukkitData().clone(),
                this.get(KeyedData.TILED_ENTITY).orElse(null));
    }

    @Override
    public AsyncBlockSnapshot asAsynced() {
        return new AsyncBlockStateSnapshot(this, Position.toASync(this.getPosition()));
    }
}
