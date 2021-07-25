package org.ships.implementation.bukkit.platform;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.core.CorePlugin;
import org.core.command.CommandLauncher;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.User;
import org.core.platform.PlatformServer;
import org.core.platform.tps.TPSExecutor;
import org.core.world.WorldExtent;
import org.ships.implementation.bukkit.entity.living.human.player.live.BUser;
import org.ships.implementation.bukkit.world.BWorldExtent;

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
        BukkitPlatform platform = ((BukkitPlatform) CorePlugin.getPlatform());
        Bukkit.getServer().getOnlinePlayers().forEach(p -> set.add((LivePlayer) platform.createEntityInstance(p)));
        return set;
    }

    @Override
    public CompletableFuture<Optional<User>> getOfflineUser(UUID uuid) {
        Player player = Bukkit.getServer().getPlayer(uuid);
        if (player != null) {
            Optional<User> opUser = Optional.of((LivePlayer) ((BukkitPlatform) CorePlugin.getPlatform()).createEntityInstance(player));
            return CompletableFuture.supplyAsync(() -> opUser);
        }
        OfflinePlayer user = Bukkit.getServer().getOfflinePlayer(uuid);
        return CompletableFuture.supplyAsync(() -> Optional.of(user).map(BUser::new));
    }

    @Override
    public CompletableFuture<Optional<User>> getOfflineUser(String lastName) {
        Player player = Bukkit.getServer().getPlayer(lastName);
        if (player != null) {
            Optional<User> opUser = Optional.of((LivePlayer) ((BukkitPlatform) CorePlugin.getPlatform()).createEntityInstance(player));
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
            return CompletableFuture.supplyAsync(() -> (User) ((BukkitPlatform) CorePlugin.getPlatform()).createEntityInstance(player));
        }).collect(Collectors.toSet());
    }

    @Override
    public TPSExecutor getTPSExecutor() {
        return this.tpsExecutor;
    }
}
