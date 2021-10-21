package org.core.implementation.bukkit.event.events.connection;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.connection.ClientConnectionEvent;
import org.core.text.Text;

public class BQuitEvent extends AbstractLeaveEvent implements ClientConnectionEvent.Leave.Quit {

    @Deprecated
    public BQuitEvent(LivePlayer player, Text leaveMessage) {
        super(player, leaveMessage);
    }
}
