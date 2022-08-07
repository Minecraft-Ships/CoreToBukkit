package org.core.implementation.bukkit.world;

import org.bukkit.Chunk;
import org.core.TranslateCore;
import org.core.entity.LiveEntity;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.world.position.impl.async.BAsyncBlockPosition;
import org.core.implementation.bukkit.world.position.impl.async.BAsyncExactPosition;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.implementation.bukkit.world.position.impl.sync.BExactPosition;
import org.core.world.ChunkExtent;
import org.core.world.WorldExtent;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BChunkExtent implements ChunkExtent {

    private final Chunk chunk;

    public BChunkExtent(Chunk chunk) {
        this.chunk = chunk;
    }

    @Override
    public SyncExactPosition getPosition(double x, double y, double z) {
        return new BExactPosition(x, y, z, this.chunk.getWorld());
    }

    @Override
    public ASyncExactPosition getAsyncPosition(double x, double y, double z) {
        return new BAsyncExactPosition(this.chunk.getWorld(), x, y, z);
    }

    @Override
    public SyncBlockPosition getPosition(int x, int y, int z) {
        return new BBlockPosition(x, y, z, this.chunk.getWorld());
    }

    @Override
    public ASyncBlockPosition getAsyncPosition(int x, int y, int z) {
        return new BAsyncBlockPosition(this.chunk.getWorld(), x, y, z);
    }

    @Override
    public boolean isLoaded() {
        return this.chunk.isLoaded();
    }

    @Override
    public Set<LiveEntity> getEntities() {
        return Stream.of(this.chunk.getEntities())
                .map(entity -> ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(entity))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LiveTileEntity> getTileEntities() {
        return Stream.of(this.chunk.getTileEntities())
                .map(entity -> ((BukkitPlatform) TranslateCore.getPlatform())
                        .createTileEntityInstance(entity)
                        .orElse(null))
                .collect(Collectors.toSet());
    }

    @Override
    public WorldExtent getWorld() {
        return new BWorldExtent(this.chunk.getWorld());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BChunkExtent)) {
            return false;
        }
        BChunkExtent chunk = (BChunkExtent) obj;
        return this.chunk.equals(chunk.chunk);
    }
}
