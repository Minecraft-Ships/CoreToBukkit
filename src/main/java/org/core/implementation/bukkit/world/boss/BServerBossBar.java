package org.core.implementation.bukkit.world.boss;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.core.adventureText.AText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.living.human.player.live.BLivePlayer;
import org.core.implementation.bukkit.world.boss.colour.BBossColour;
import org.core.world.boss.ServerBossBar;
import org.core.world.boss.colour.BossColour;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BServerBossBar implements ServerBossBar {

    final org.bukkit.boss.BossBar bossBar;

    public BServerBossBar() {
        this(Bukkit.createBossBar("<Unset Message>", BarColor.PURPLE, BarStyle.SOLID));
    }

    public BServerBossBar(org.bukkit.boss.BossBar boss) {
        this.bossBar = boss;
    }

    @Override
    public AText getTitle() {
        return AText.ofLegacy(this.bossBar.getTitle());
    }

    @Override
    public ServerBossBar setTitle(AText text) {
        this.bossBar.setTitle(text.toLegacy());
        return this;
    }

    @Override
    public BossColour getColour() {
        return new BBossColour(this.bossBar.getColor());
    }

    @Override
    public ServerBossBar setColour(BossColour colour) {
        this.bossBar.setColor(((BBossColour) colour).getBukkitColor());
        return this;
    }

    @Override
    public int getValue() {
        return (int) (this.bossBar.getProgress() * 100);
    }

    @Override
    public ServerBossBar setValue(int value) {
        if (value > 100) {
            throw new IllegalArgumentException("ServerBossBar.SetValue must be between 0 and 100 (" + value + ")");
        }
        double percent = (value / 100.0);
        this.bossBar.setProgress(percent);
        return this;
    }

    @Override
    public Set<LivePlayer> getPlayers() {
        List<org.bukkit.entity.Player> players = this.bossBar.getPlayers();
        Set<LivePlayer> set = new HashSet<>();
        players.forEach(p -> set.add(new BLivePlayer(p)));
        return set;
    }

    @Override
    public ServerBossBar register(LivePlayer... players) {
        for (LivePlayer player : players) {
            this.bossBar.addPlayer(((BLiveEntity<Player>) player).getBukkitEntity());
        }
        return this;
    }

    @Override
    public ServerBossBar deregister(LivePlayer... players) {
        for (LivePlayer player : players) {
            this.bossBar.removePlayer(((BLiveEntity<Player>) player).getBukkitEntity());
        }
        return this;
    }
}
