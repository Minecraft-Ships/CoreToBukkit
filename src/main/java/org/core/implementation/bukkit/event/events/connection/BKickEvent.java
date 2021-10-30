package org.core.implementation.bukkit.event.events.connection;

import org.core.adventureText.AText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.connection.ClientConnectionEvent;

public class BKickEvent extends AbstractLeaveEvent implements ClientConnectionEvent.Leave.Kick {

    public BKickEvent(LivePlayer player, AText leaveMessage) {
        super(player, leaveMessage);
    }
}
