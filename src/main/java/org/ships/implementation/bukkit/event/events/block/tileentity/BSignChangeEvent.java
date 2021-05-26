package org.ships.implementation.bukkit.event.events.block.tileentity;

import org.core.adventureText.AText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.block.tileentity.SignChangeEvent;
import org.core.text.Text;
import org.core.world.position.block.entity.sign.SignTileEntitySnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.ships.implementation.bukkit.world.position.block.entity.sign.BSignEntitySnapshot;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class BSignChangeEvent implements SignChangeEvent.ByPlayer {

    protected boolean isCancelled;
    protected SignTileEntitySnapshot original;
    protected SignTileEntitySnapshot to;
    protected SyncBlockPosition position;
    protected LivePlayer player;

    @Deprecated
    public BSignChangeEvent(LivePlayer player, SyncBlockPosition position, Text... lines) {
        this(player, position, Arrays.asList(lines).parallelStream().map(Text::toAdventure).collect(Collectors.toList()));
    }

    public BSignChangeEvent(LivePlayer player, SyncBlockPosition position, AText... text) {
        this(player, position, Arrays.asList(text));
    }

    public BSignChangeEvent(LivePlayer player, SyncBlockPosition position, Collection<AText> lines) {
        this.position = position;
        this.original = new BSignEntitySnapshot(lines);
        this.player = player;
        this.to = new BSignEntitySnapshot(lines);
    }

    public BSignChangeEvent(LivePlayer player, SyncBlockPosition position) {
        this(player, position, Collections.emptyList());
    }

    @Override
    public SignTileEntitySnapshot getTo() {
        return this.to;
    }

    @Override
    public SignChangeEvent setTo(SignTileEntitySnapshot snapshot) {
        this.to = snapshot;
        return this;
    }

    @Override
    public SignTileEntitySnapshot getFrom() {
        return this.original;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        this.isCancelled = value;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return this.position;
    }

    @Override
    public LivePlayer getEntity() {
        return this.player;
    }
}
