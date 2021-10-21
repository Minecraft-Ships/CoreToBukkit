package org.core.implementation.bukkit.event.events.entity;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.entity.EntityCommandEvent;

import java.util.Collection;

public class BEntityCommandEvent implements EntityCommandEvent {

    private final LivePlayer player;
    private final String[] cmd;

    public BEntityCommandEvent(LivePlayer player, String... cmd) {
        this.player = player;
        this.cmd = cmd;
    }

    public BEntityCommandEvent(LivePlayer player, Collection<String> collection) {
        this(player, collection.toArray(new String[0]));
    }

    @Override
    public String[] getCommand() {
        return this.cmd;
    }

    @Override
    public LivePlayer getEntity() {
        return this.player;
    }
}
