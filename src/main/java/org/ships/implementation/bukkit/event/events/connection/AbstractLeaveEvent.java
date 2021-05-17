package org.ships.implementation.bukkit.event.events.connection;

import org.core.CorePlugin;
import org.core.adventureText.AText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.connection.ClientConnectionEvent;
import org.core.text.Text;

public class AbstractLeaveEvent implements ClientConnectionEvent.Leave {

    protected LivePlayer player;
    protected AText leaveMessage;

    @Deprecated
    public AbstractLeaveEvent(LivePlayer player, Text leaveMessage) {
        this.leaveMessage = leaveMessage.toAdventure();
        this.player = player;
    }

    @Override
    public LivePlayer getEntity() {
        return this.player;
    }

    @Override
    @Deprecated
    public Text getLeaveMessage() {
        return CorePlugin.buildText(this.leaveMessage.toLegacy());
    }

    @Override
    @Deprecated
    public Leave setLeaveMessage(Text message) {
        this.leaveMessage = message.toAdventure();
        return this;
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
