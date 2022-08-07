package org.core.implementation.bukkit.world.position.impl.async;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.bukkit.world.BWorldExtent;
import org.core.implementation.bukkit.world.position.block.details.blocks.AsyncBlockStateSnapshot;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.platform.plugin.Plugin;
import org.core.threadsafe.FutureResult;
import org.core.vector.type.Vector3;
import org.core.world.WorldExtent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.core.world.position.impl.sync.SyncPosition;

import java.util.Optional;

public class BAsyncBlockPosition extends BAbstractPosition<Integer> implements ASyncBlockPosition {

    private final Block block;

    public BAsyncBlockPosition(World world, int x, int y, int z) {
        this(world.getBlockAt(x, y, z));
    }

    public BAsyncBlockPosition(Block block) {
        this.block = block;
    }

    public Block getBukkitBlock() {
        return this.block;
    }

    @Override
    public WorldExtent getWorld() {
        return new BWorldExtent(this.block.getWorld());
    }

    @Override
    public AsyncBlockStateSnapshot getBlockDetails() {
        return new AsyncBlockStateSnapshot(this);
    }

    @Override
    public ASyncExactPosition toExactPosition() {
        return new BAsyncExactPosition(this.block.getLocation());
    }

    @Override
    public Vector3<Integer> getPosition() {
        return Vector3.valueOf(this.block.getX(), this.block.getY(), this.block.getZ());
    }

    @Override
    public FutureResult<SyncPosition<Integer>> scheduleBlock(Plugin plugin, BlockDetails details,
            PositionFlag.SetFlag... flags) {
        FutureResult<SyncPosition<Integer>> future = new FutureResult<>();
        Bukkit.getScheduler().runTask((org.bukkit.plugin.Plugin) plugin.getPlatformLauncher(), () -> {
            SyncPosition<Integer> position = new BBlockPosition(this.block);
            position.setBlock(details, flags);
            future.run(position);
        });
        return future;
    }

    @Override
    public FutureResult<SyncPosition<Integer>> scheduleBlock(Plugin plugin, BlockDetails details,
            LivePlayer... player) {
        FutureResult<SyncPosition<Integer>> future = new FutureResult<>();
        Bukkit.getScheduler().runTask((org.bukkit.plugin.Plugin) plugin.getPlatformLauncher(), () -> {
            SyncPosition<Integer> position = new BBlockPosition(this.block);
            position.setBlock(details, player);
            future.run(position);
        });
        return future;
    }

    @Override
    public FutureResult<SyncPosition<Integer>> scheduleReset(Plugin plugin, LivePlayer... player) {
        FutureResult<SyncPosition<Integer>> future = new FutureResult<>();
        Bukkit.getScheduler().runTask((org.bukkit.plugin.Plugin) plugin.getPlatformLauncher(), () -> {
            SyncPosition<Integer> position = new BBlockPosition(this.block);
            position.resetBlock(player);
            future.run(position);
        });
        return future;
    }

    @Override
    public FutureResult<LiveTileEntity> getTileEntity(Plugin plugin) {
        FutureResult<LiveTileEntity> future = new FutureResult<>();
        Bukkit.getScheduler().runTask((org.bukkit.plugin.Plugin) plugin.getPlatformLauncher(), () -> {
            SyncPosition<Integer> position = new BBlockPosition(this.block);
            Optional<LiveTileEntity> opEntity = position.getTileEntity();
            opEntity.ifPresent(future::run);
        });
        return future;
    }
}
