package org.core.implementation.bukkit.world.position.impl.sync;

import org.bukkit.entity.Player;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.exceptions.BlockNotSupported;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.implementation.bukkit.world.position.block.details.blocks.BExtendedBlockSnapshot;
import org.core.implementation.bukkit.world.position.block.details.blocks.IBBlockDetails;
import org.core.implementation.bukkit.world.position.flags.BApplyPhysicsFlag;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.vector.type.Vector3;
import org.core.world.WorldExtent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.sign.SignTileEntity;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlags;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class BBlockPosition extends BAbstractPosition<Integer> implements SyncBlockPosition {

    protected final org.bukkit.block.Block block;

    public BBlockPosition(int x, int y, int z, @NotNull org.bukkit.World world) {
        this(world.getBlockAt(x, y, z));
    }

    public BBlockPosition(@NotNull org.bukkit.block.Block block) {
        this.block = block;
    }

    @Override
    public Vector3<Integer> getPosition() {
        return Vector3.valueOf(this.block.getX(), this.block.getY(), this.block.getZ());
    }

    @Override
    public WorldExtent getWorld() {
        return new BWorldExtent(this.block.getWorld());
    }

    @Override
    public BExtendedBlockSnapshot getBlockDetails() {
        return new BExtendedBlockSnapshot(this);
    }

    @Override
    public BBlockPosition setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        BApplyPhysicsFlag physicsFlag = (BApplyPhysicsFlag) Stream
                .of(flags)
                .filter(b -> b instanceof ApplyPhysicsFlag)
                .findAny()
                .orElse(ApplyPhysicsFlags.NONE.get());

        this.block.setBlockData(((IBBlockDetails) details).getBukkitData(), physicsFlag.getBukkitValue());
        Optional<TileEntitySnapshot<? extends TileEntity>> opTile = details.get(KeyedData.TILED_ENTITY);
        if (opTile.isPresent()) {
            try {
                opTile.get().apply(this);
            } catch (BlockNotSupported blockNotSupported) {
                blockNotSupported.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public BBlockPosition setBlock(BlockDetails details, LivePlayer... player) {
        Stream
                .of(player)
                .forEach(lp -> ((BLiveEntity<Player>) lp)
                        .getBukkitEntity()
                        .sendBlockChange(this.block.getLocation(), ((IBBlockDetails) details).getBukkitData()));
        Optional<TileEntitySnapshot<? extends TileEntity>> opTile = details.get(KeyedData.TILED_ENTITY);
        if (opTile.isPresent()) {
            TileEntitySnapshot<? extends TileEntity> tile = opTile.get();
            if (tile instanceof SignTileEntitySnapshot) {
                SignTileEntity stes = (SignTileEntity) tile;
                List<AText> lines = stes.getText();
                String[] linesArray = lines.parallelStream().map(AText::toLegacy).toArray(String[]::new);
                Stream
                        .of(player)
                        .forEach(lp -> ((BLiveEntity<Player>) lp)
                                .getBukkitEntity()
                                .sendSignChange(this.block.getLocation(), linesArray));
            }
        }
        return this;
    }

    @Override
    public BBlockPosition resetBlock(LivePlayer... player) {
        return this.setBlock(this.getBlockDetails(), player);
    }

    @Override
    public BBlockPosition destroy() {
        this.block.breakNaturally();
        return this;
    }

    @Override
    public Optional<LiveTileEntity> getTileEntity() {
        BukkitPlatform platform = (BukkitPlatform) TranslateCore.getPlatform();
        return platform.createTileEntityInstance(this.block.getState());
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createEntity(
            EntityType<E, ? extends S> type) {
        return ((BukkitPlatform) TranslateCore.getPlatform()).createSnapshot(type, this.toExactPosition());
    }

    @Override
    public boolean equals(Object value) {
        if (!(value instanceof Position)) {
            return false;
        }
        Position<? extends Number> pos = (Position<? extends Number>) value;
        return pos.getPosition().equals(this.getPosition());
    }
}
