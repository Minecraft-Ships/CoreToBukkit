package org.ships.implementation.bukkit.world.position.impl.sync;

import org.core.CorePlugin;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.exceptions.BlockNotSupported;
import org.core.text.Text;
import org.core.vector.type.Vector3;
import org.core.world.WorldExtent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.keyed.KeyedData;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlags;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncPosition;
import org.ships.implementation.bukkit.entity.living.human.player.live.BLivePlayer;
import org.ships.implementation.bukkit.platform.BukkitPlatform;
import org.ships.implementation.bukkit.text.BText;
import org.ships.implementation.bukkit.world.BWorldExtent;
import org.ships.implementation.bukkit.world.position.block.details.blocks.BExtendedBlockSnapshot;
import org.ships.implementation.bukkit.world.position.block.details.blocks.IBBlockDetails;
import org.ships.implementation.bukkit.world.position.flags.BApplyPhysicsFlag;
import org.ships.implementation.bukkit.world.position.impl.BAbstractPosition;

import java.util.Optional;
import java.util.stream.Stream;

public class BBlockPosition extends BAbstractPosition<Integer> implements SyncBlockPosition {

    protected org.bukkit.block.Block block;

    public BBlockPosition(int x, int y, int z, org.bukkit.World world){
        this(world.getBlockAt(x, y, z));
    }

    public BBlockPosition(org.bukkit.block.Block block){
        if(block == null){
            new NullPointerException().printStackTrace();
        }
        this.block = block;
    }

    public org.bukkit.block.Block getBukkitBlock(){
        return this.block;
    }

    @Override
    public Vector3<Integer> getChunkPosition() {
        return Vector3.valueOf(this.block.getChunk().getX(), 0, this.block.getChunk().getZ());
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
    public BlockSnapshot<SyncBlockPosition> getBlockDetails() {
        return new BExtendedBlockSnapshot(this);
    }

    @Override
    public BBlockPosition setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        BApplyPhysicsFlag physicsFlag = (BApplyPhysicsFlag) Stream.of(flags).filter(b -> b instanceof ApplyPhysicsFlag).findAny().orElse(ApplyPhysicsFlags.NONE.get());

        this.block.setBlockData(((IBBlockDetails)details).getBukkitData(), physicsFlag.getBukitValue());
        Optional<TileEntitySnapshot<? extends TileEntity>> opTile = details.get(KeyedData.TILED_ENTITY);
        if(opTile.isPresent()){
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
        Stream.of(player).forEach(lp -> ((BLivePlayer)lp).getBukkitEntity().sendBlockChange(this.block.getLocation(), ((IBBlockDetails)details).getBukkitData()));
        Optional<TileEntitySnapshot<? extends TileEntity>> opTile = details.get(KeyedData.TILED_ENTITY);
        if(opTile.isPresent()){
            TileEntitySnapshot<? extends TileEntity> tile = opTile.get();
            if(tile instanceof SignTileEntitySnapshot){
                SignTileEntitySnapshot stes = (SignTileEntitySnapshot)tile;
                Text[] text = stes.getLines();
                String[] lines = new String[text.length];
                for(int A = 0; A < text.length; A++){
                    lines[A] = ((BText)text[A]).toBukkitString();
                }
                Stream.of(player).forEach(lp -> ((BLivePlayer)lp).getBukkitEntity().sendSignChange(this.block.getLocation(), lines));
            }
        }
        return this;
    }

    @Override
    public BBlockPosition resetBlock(LivePlayer... player) {
        return setBlock(getBlockDetails(), player);
    }

    @Override
    public BBlockPosition destroy() {
        this.getBukkitBlock().breakNaturally();
        return this;
    }

    @Override
    public Optional<LiveTileEntity> getTileEntity() {
        BukkitPlatform platform = (BukkitPlatform)CorePlugin.getPlatform();
        return platform.createTileEntityInstance(getBukkitBlock().getState());
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createEntity(EntityType<E, S> type) {
        return ((BukkitPlatform)CorePlugin.getPlatform()).createSnapshot(type, this.toExactPosition());
    }

    @Override
    public boolean equals(Object value){
        if(!(value instanceof SyncPosition)){
            return false;
        }
        SyncPosition<? extends Number> pos = (SyncPosition<? extends Number>)value;
        return pos.getPosition().equals(getPosition());
    }
}
