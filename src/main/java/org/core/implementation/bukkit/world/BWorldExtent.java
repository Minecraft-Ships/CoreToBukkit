package org.core.implementation.bukkit.world;

import org.bukkit.Chunk;
import org.bukkit.block.BlockState;
import org.core.TranslateCore;
import org.core.entity.LiveEntity;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.world.position.impl.async.BAsyncBlockPosition;
import org.core.implementation.bukkit.world.position.impl.async.BAsyncExactPosition;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.implementation.bukkit.world.position.impl.sync.BExactPosition;
import org.core.vector.type.Vector3;
import org.core.world.ChunkExtent;
import org.core.world.WorldExtent;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BWorldExtent implements WorldExtent {

    protected final org.bukkit.World world;

    public BWorldExtent(org.bukkit.World world) {
        this.world = world;
    }

    public org.bukkit.World getBukkitWorld() {
        return this.world;
    }

    @Override
    public SyncExactPosition getPosition(double x, double y, double z) {
        return new BExactPosition(x, y, z, this.world);
    }

    @Override
    public ASyncExactPosition getAsyncPosition(double x, double y, double z) {
        return new BAsyncExactPosition(this.world, x, y, z);
    }

    @Override
    public SyncBlockPosition getPosition(int x, int y, int z) {
        return new BBlockPosition(x, y, z, this.world);
    }

    @Override
    public ASyncBlockPosition getAsyncPosition(int x, int y, int z) {
        return new BAsyncBlockPosition(this.world, x, y, z);
    }

    @Override
    public boolean isLoaded() {
        return true;
    }

    @Override
    public Set<LiveEntity> getEntities() {
        Set<LiveEntity> entities = new HashSet<>();
        this.world.getEntities().forEach(e -> {
            LiveEntity entity = ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(e);
            if (entity == null) {
                System.err.println("Entity could not be converted: " + e.getType().name() + " | " + e.getName());
                return;
            }
            entities.add(entity);
        });
        return entities;
    }

    @Override
    public Set<LiveTileEntity> getTileEntities() {
        Set<LiveTileEntity> set = new HashSet<>();
        for (org.bukkit.Chunk chunk : this.world.getLoadedChunks()) {
            for (BlockState state : chunk.getTileEntities()) {
                ((BukkitPlatform) TranslateCore.getPlatform()).createTileEntityInstance(state).ifPresent(set::add);
            }
        }
        return set;
    }

    @Override
    public String getName() {
        return this.world.getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.world.getUID();
    }

    @Override
    public String getPlatformUniqueId() {
        return this.getName();
    }

    @Override
    public Set<ChunkExtent> getChunks() {
        return Stream.of(this.world.getLoadedChunks()).map(BChunkExtent::new).collect(Collectors.toSet());
    }

    @Override
    public Optional<ChunkExtent> getChunk(Vector3<Integer> vector) {
        Chunk chunk = this.world.getChunkAt(vector.getX(), vector.getZ());
        if (!chunk.isLoaded()) {
            return Optional.empty();
        }
        return Optional.of(new BChunkExtent(chunk));
    }

    @Override
    public CompletableFuture<ChunkExtent> loadChunkAsynced(Vector3<Integer> vector) {
        try {
            CompletableFuture<Chunk> future = (CompletableFuture<Chunk>) this.world
                    .getClass()
                    .getMethod("getChunkAtAsync", int.class, int.class)
                    .invoke(this.world, vector.getX(), vector.getZ());
            return future.thenApply(BChunkExtent::new);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            ChunkExtent extent = this.loadChunk(vector);
            return CompletableFuture.completedFuture(extent);
        }
    }

    @Override
    public ChunkExtent loadChunk(Vector3<Integer> vector) {
        this.world.loadChunk(vector.getX(), vector.getZ());
        Chunk chunk = this.world.getChunkAt(vector.getX(), vector.getZ());
        return new BChunkExtent(chunk);
    }

    @Override
    public int getMinimumBlockHeight() {
        return this.world.getMinHeight();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WorldExtent)) {
            return false;
        }
        return ((WorldExtent) obj).getName().equals(this.getName());
    }
}
