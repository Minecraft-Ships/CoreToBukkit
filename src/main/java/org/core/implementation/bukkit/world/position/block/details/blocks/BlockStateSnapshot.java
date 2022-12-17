package org.core.implementation.bukkit.world.position.block.details.blocks;

import org.bukkit.block.data.BlockData;
import org.core.TranslateCore;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.world.position.block.BBlockType;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.BDirectionalData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.BRotationalData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.BAttachableKeyedData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.BMultiDirectionalKeyedData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.BOpenableKeyedData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.BWaterLoggedKeyedData;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.DirectionalData;
import org.core.world.position.block.details.data.keyed.AttachableKeyedData;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.details.data.keyed.MultiDirectionalKeyedData;
import org.core.world.position.block.details.data.keyed.OpenableKeyedData;
import org.core.world.position.block.details.data.keyed.TileEntityKeyedData;
import org.core.world.position.block.details.data.keyed.WaterLoggedKeyedData;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.impl.BlockPosition;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.Optional;

public class BlockStateSnapshot implements BlockSnapshot.SyncBlockSnapshot, IBBlockDetails {

    protected final org.bukkit.block.BlockState state;

    public BlockStateSnapshot(org.bukkit.block.BlockState state) {
        this.state = state;
    }

    @Override
    public AsyncBlockSnapshot asAsynced() {
        return new AsyncBlockStateSnapshot(this);
    }

    public org.bukkit.block.BlockState getBukkitBlockState() {
        return this.state;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return new BBlockPosition(this.state.getBlock());
    }

    @Override
    public BlockType getType() {
        return new BBlockType(this.state.getType());
    }

    @Override
    public AsyncBlockSnapshot createSnapshot(ASyncBlockPosition position) {
        return new AsyncBlockStateSnapshot(position, this.state.getBlockData());
    }

    @Override
    public SyncBlockSnapshot createSnapshot(SyncBlockPosition position) {
        return new BExtendedBlockSnapshot(position, this.state.getBlockData());
    }

    @Override
    public Optional<DirectionalData> getDirectionalData() {
        if (this.state.getBlockData() instanceof org.bukkit.block.data.Directional) {
            return Optional.of(new BDirectionalData((org.bukkit.block.data.Directional) this.state.getBlockData()));
        }
        if (this.state.getBlockData() instanceof org.bukkit.block.data.Rotatable) {
            return Optional.of(new BRotationalData((org.bukkit.block.data.Rotatable) this.state.getBlockData()));
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
    public BlockStateSnapshot createCopyOf() {
        return (BlockStateSnapshot) this.createSnapshot(this.getPosition());
    }

    @Override
    public BlockData getBukkitData() {
        return this.state.getBlockData();
    }

    @Override
    public void setBukkitData(BlockData data) {
        this.state.setBlockData(data);
    }

    private <T> Optional<KeyedData<T>> getKey(Class<? extends KeyedData<T>> data) {
        KeyedData<T> key = null;
        if (data.isAssignableFrom(WaterLoggedKeyedData.class) &&
                this.state.getBlockData() instanceof org.bukkit.block.data.Waterlogged) {
            key = (KeyedData<T>) new BWaterLoggedKeyedData(
                    (org.bukkit.block.data.Waterlogged) this.state.getBlockData());
        } else if (data.isAssignableFrom(TileEntityKeyedData.class)) {
            key = (KeyedData<T>) new BlockStateSnapshot.BTileEntityKeyedData();
        } else if (data.isAssignableFrom(OpenableKeyedData.class) &&
                (this.state.getBlockData() instanceof org.bukkit.block.data.Openable)) {
            key = (KeyedData<T>) new BOpenableKeyedData((org.bukkit.block.data.Openable) this.state.getBlockData());
        } else if (data.isAssignableFrom(AttachableKeyedData.class) &&
                BAttachableKeyedData.getKeyedData(this).isPresent()) {
            key = (KeyedData<T>) BAttachableKeyedData.getKeyedData(this).get();
        } else if (data.isAssignableFrom(MultiDirectionalKeyedData.class) &&
                this.state.getBlockData() instanceof org.bukkit.block.data.MultipleFacing) {
            key = (KeyedData<T>) new BMultiDirectionalKeyedData(
                    (org.bukkit.block.data.MultipleFacing) this.state.getBlockData());
        }
        return Optional.ofNullable(key);
    }

    private class BTileEntityKeyedData implements TileEntityKeyedData {

        @Override
        public Optional<TileEntitySnapshot<? extends TileEntity>> getData() {
            BukkitPlatform platform = (BukkitPlatform) TranslateCore.getPlatform();
            Optional<LiveTileEntity> opTile = platform.createTileEntityInstance(BlockStateSnapshot.this.state);
            return opTile.map(TileEntity::getSnapshot);
        }

        @Override
        public void setData(TileEntitySnapshot<? extends TileEntity> value) {
            BukkitPlatform platform = (BukkitPlatform) TranslateCore.getPlatform();
            Optional<LiveTileEntity> opTile = platform.createTileEntityInstance(BlockStateSnapshot.this.state);
            if (opTile.isEmpty()) {
                return;
            }
            this.apply(value, opTile.get());
        }

        private <T extends LiveTileEntity> void apply(TileEntitySnapshot<T> snapshot, LiveTileEntity lte) {
            snapshot.apply((T) lte);
        }
    }
}
