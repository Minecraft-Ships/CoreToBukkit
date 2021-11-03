package org.core.implementation.bukkit.event.events.connection;

import org.core.adventureText.AText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.connection.ClientConnectionEvent;

public class AbstractLeaveEvent implements ClientConnectionEvent.Leave {

    protected final LivePlayer player;
    protected AText leaveMessage;

    public AbstractLeaveEvent(LivePlayer player, AText leaveMessage) {
        this.leaveMessage = leaveMessage;
        this.player = player;
    }

    @Override
    public LivePlayer getEntity() {
        return this.player;
    }

    @Override
    public AText getLeavingMessage() {
        return this.leaveMessage;
    }

    @Override
    public Leave setLeavingMessage(AText message) {
        this.leaveMessage = message;
        return this;
    }
}
