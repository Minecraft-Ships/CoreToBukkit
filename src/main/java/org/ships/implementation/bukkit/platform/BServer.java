package org.ships.implementation.bukkit.platform;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.core.TranslateCore;
import org.core.command.CommandLauncher;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.User;
import org.core.exceptions.BlockNotSupported;
import org.core.platform.PlatformServer;
import org.core.platform.plugin.Plugin;
import org.core.platform.tps.TPSExecutor;
import org.core.schedule.Scheduler;
import org.core.schedule.unit.TimeUnit;
import org.core.world.WorldExtent;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.details.data.keyed.TileEntityKeyedData;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.ships.implementation.bukkit.entity.living.human.player.live.BUser;
import org.ships.implementation.bukkit.world.BWorldExtent;
import org.ships.implementation.bukkit.world.position.block.details.blocks.AsyncBlockStateSnapshot;
import org.ships.implementation.bukkit.world.position.impl.async.BAsyncBlockPosition;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BServer implements PlatformServer {

    protected Set<CommandLauncher> commands = new HashSet<>();
    protected TPSExecutor tpsExecutor = new TPSExecutor();

    @Override
    public Set<WorldExtent> getWorlds() {
        Set<WorldExtent> set = new HashSet<>();
        Bukkit.getWorlds().forEach(w -> set.add(new BWorldExtent(w)));
        return set;
    }

    @Override
    public Optional<WorldExtent> getWorldByPlatformSpecific(String name) {
        return this.getWorld(name, true);
    }

    @Override
    public Collection<LivePlayer> getOnlinePlayers() {
        Set<LivePlayer> set = new HashSet<>();
        BukkitPlatform platform = ((BukkitPlatform) TranslateCore.getPlatform());
        Bukkit.getServer().getOnlinePlayers().forEach(p -> set.add((LivePlayer) platform.createEntityInstance(p)));
        return set;
    }

    @Override
    public void applyBlockSnapshots(Collection<BlockSnapshot.AsyncBlockSnapshot> collection, Plugin plugin, Runnable onComplete) {
        Set<BlockSnapshot<ASyncBlockPosition>> withTileEntities = collection
                .stream()
                .filter(bs -> bs.get(TileEntityKeyedData.class).isPresent())
                .collect(Collectors.toSet());
        Scheduler syncedSchedule = TranslateCore
                .createSchedulerBuilder()
                .setDelay(0)
                .setDelayUnit(TimeUnit.MINECRAFT_TICKS)
                .setDisplayName("BlockSnapshotApplyEntities")
                .setExecutor(() -> {
                    withTileEntities
                            .forEach(bs -> bs
                                    .get(TileEntityKeyedData.class)
                                    .ifPresent(tileEntity -> {
                                        try {
                                            tileEntity.apply(Position.toSync(bs.getPosition()));
                                        } catch (BlockNotSupported e) {
                                            throw new RuntimeException(e);
                                        }
                                    }));
                    onComplete.run();
                })
                .build(plugin);

        Scheduler asyncedSchedule = TranslateCore
                .createSchedulerBuilder()
                .setDisplayName("BlockSnapshotAsyncedEnd")
                .setDelayUnit(TimeUnit.MINECRAFT_TICKS)
                .setDelay(0)
                .setExecutor(() -> {
                    for (BlockSnapshot.AsyncBlockSnapshot blockSnapshot : collection) {
                        Block block = ((BAsyncBlockPosition) blockSnapshot.getPosition()).getBukkitBlock();
                        try {
                            block.setBlockData(((AsyncBlockStateSnapshot) blockSnapshot).getBukkitData(), false);
                        } catch (IllegalStateException e) {
                            System.err.println("Failed to set block type of " + blockSnapshot.getType().getId());
                            throw e;
                        }
                    }
                    syncedSchedule.run();
                })
                .setAsync(true)
                .build(plugin);
        asyncedSchedule.run();

    }

    @Override
    public CompletableFuture<Optional<User>> getOfflineUser(UUID uuid) {
        Player player = Bukkit.getServer().getPlayer(uuid);
        if (player != null) {
            Optional<User> opUser = Optional.of((LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(player));
            return CompletableFuture.supplyAsync(() -> opUser);
        }
        OfflinePlayer user = Bukkit.getServer().getOfflinePlayer(uuid);
        return CompletableFuture.supplyAsync(() -> Optional.of(user).map(BUser::new));
    }

    @Override
    public CompletableFuture<Optional<User>> getOfflineUser(String lastName) {
        Player player = Bukkit.getServer().getPlayer(lastName);
        if (player != null) {
            Optional<User> opUser = Optional.of((LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(player));
            return CompletableFuture.supplyAsync(() -> opUser);
        }
        OfflinePlayer user = Bukkit.getServer().getOfflinePlayer(lastName);
        return CompletableFuture.supplyAsync(() -> Optional.of(user).map(BUser::new));
    }

    @Override
    public Collection<CompletableFuture<User>> getOfflineUsers() {
        return Stream.of(Bukkit.getServer().getOfflinePlayers()).map(op -> {
            Player player = op.getPlayer();
            if (player == null) {
                return CompletableFuture.supplyAsync(() -> (User) new BUser(op));
            }
            return CompletableFuture.supplyAsync(() -> (User) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(player));
        }).collect(Collectors.toSet());
    }

    @Override
    public TPSExecutor getTPSExecutor() {
        return this.tpsExecutor;
    }
}
