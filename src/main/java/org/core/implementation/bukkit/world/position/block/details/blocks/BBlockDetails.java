package org.core.implementation.bukkit.world.position.block.details.blocks;

import org.bukkit.block.data.BlockData;
import org.core.TranslateCore;
import org.core.implementation.bukkit.world.position.block.BBlockType;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.BDirectionalData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.BRotationalData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.BAttachableKeyedData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.BMultiDirectionalKeyedData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.BOpenableKeyedData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.BWaterLoggedKeyedData;
import org.core.implementation.bukkit.world.position.impl.async.BAsyncBlockPosition;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.*;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Optional;

public class BBlockDetails implements BlockDetails, IBBlockDetails {

    private final boolean async;
    private org.bukkit.block.data.BlockData data;
    private TileEntitySnapshot<? extends TileEntity> tileEntitySnapshot;
    public BBlockDetails(BlockData data, TileEntitySnapshot<? extends TileEntity> tileEntitySnapshot) {
        this(data, tileEntitySnapshot, true);
    }

    protected BBlockDetails(BlockData data, TileEntitySnapshot<? extends TileEntity> tileEntitySnapshot,
            boolean async) {
        this.data = data;
        this.tileEntitySnapshot = tileEntitySnapshot;
        this.async = async;
    }

    public BBlockDetails(org.bukkit.block.data.BlockData data, boolean async) {
        this.data = data;
        this.async = async;
        if (!async) {
            TranslateCore
                    .getPlatform()
                    .getDefaultTileEntity(this.getType())
                    .ifPresent(t -> this.tileEntitySnapshot = t);
        }
    }

    public BBlockDetails(BBlockDetails details) {
        this.data = details.data.clone();
        this.async = details.async;
        this.tileEntitySnapshot = details.tileEntitySnapshot.getSnapshot();
    }

    public BBlockDetails(BAsyncBlockPosition position) {
        this(new BBlockPosition(position.getBukkitBlock()), true);
    }

    public BBlockDetails(BBlockPosition position) {
        this(new BBlockPosition(position.toBukkitBlock()), false);
    }

    private BBlockDetails(BBlockPosition position, boolean async) {
        this(position.toBukkitBlock().getBlockData(), async);
        if (!async && position.getTileEntity().isPresent()) {
            this.tileEntitySnapshot = position.getTileEntity().get().getSnapshot();
        }
    }

    @Override
    public org.bukkit.block.data.BlockData getBukkitData() {
        return this.data;
    }

    @Override
    public void setBukkitData(org.bukkit.block.data.BlockData data) {
        this.data = data;
    }

    @Override
    public BlockType getType() {
        return new BBlockType(this.data.getMaterial());
    }

    @Override
    public BlockSnapshot.AsyncBlockSnapshot createSnapshot(ASyncBlockPosition position) {
        BlockSnapshot.AsyncBlockSnapshot snapshot = new AsyncBlockStateSnapshot(position, this.getBukkitData());
        if (this.tileEntitySnapshot != null) {
            snapshot.set(TileEntityKeyedData.class, this.tileEntitySnapshot);
        }
        return snapshot;
    }

    @Override
    public BlockSnapshot.SyncBlockSnapshot createSnapshot(SyncBlockPosition position) {
        return new BExtendedBlockSnapshot(position, this.getBukkitData());
    }

    @Override
    public Optional<DirectionalData> getDirectionalData() {
        if (this.data instanceof org.bukkit.block.data.Directional) {
            return Optional.of(new BDirectionalData((org.bukkit.block.data.Directional) this.data));
        }
        if (this.data instanceof org.bukkit.block.data.Rotatable) {
            return Optional.of(new BRotationalData((org.bukkit.block.data.Rotatable) this.data));
        }
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> get(Class<? extends KeyedData<T>> data) {
        Optional<KeyedData<T>> opKey = this.getKey(data);
        if (opKey.isPresent()) {
            return opKey.get().getData();
        }
        return Optional.empty();
    }

    @Override
    public <T> BlockDetails set(Class<? extends KeyedData<T>> data, T value) {
        Optional<KeyedData<T>> opKey = this.getKey(data);
        opKey.ifPresent(k -> k.setData(value));
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BBlockDetails)) {
            return false;
        }
        BBlockDetails details = (BBlockDetails) obj;
        return details.data.equals(this.data);
    }

    protected <T> Optional<KeyedData<T>> getKey(Class<? extends KeyedData<T>> data) {
        KeyedData<T> key = null;
        if (data.isAssignableFrom(WaterLoggedKeyedData.class) &&
                this.data instanceof org.bukkit.block.data.Waterlogged) {
            key = (KeyedData<T>) new BWaterLoggedKeyedData((org.bukkit.block.data.Waterlogged) this.data);
        } else if (data.isAssignableFrom(OpenableKeyedData.class) &&
                (this.data instanceof org.bukkit.block.data.Openable)) {
            key = (KeyedData<T>) new BOpenableKeyedData((org.bukkit.block.data.Openable) this.data);
        } else if (data.isAssignableFrom(AttachableKeyedData.class) &&
                BAttachableKeyedData.getKeyedData(this).isPresent()) {
            key = (KeyedData<T>) BAttachableKeyedData.getKeyedData(this).get();
        } else if (data.isAssignableFrom(MultiDirectionalKeyedData.class) &&
                this.data instanceof org.bukkit.block.data.MultipleFacing) {
            key = (KeyedData<T>) new BMultiDirectionalKeyedData((org.bukkit.block.data.MultipleFacing) this.data);
        } else if (data.isAssignableFrom(TileEntityKeyedData.class)) {
            key = (KeyedData<T>) new BTileEntityKeyedData();
        }
        return Optional.ofNullable(key);
    }

    @Override
    public BlockDetails createCopyOf() {
        return new BBlockDetails(this.data.clone(), this.async);
    }

    protected class BTileEntityKeyedData implements TileEntityKeyedData {

        @Override
        public Optional<TileEntitySnapshot<? extends TileEntity>> getData() {
            return Optional.ofNullable(BBlockDetails.this.tileEntitySnapshot);
        }

        @Override
        public void setData(TileEntitySnapshot<? extends TileEntity> value) {
            BBlockDetails.this.tileEntitySnapshot = value;
        }
    }
}
