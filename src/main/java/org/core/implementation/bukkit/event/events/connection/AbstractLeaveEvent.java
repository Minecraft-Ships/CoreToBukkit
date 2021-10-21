package org.core.implementation.bukkit.event.events.connection;

import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.connection.ClientConnectionEvent;
import org.core.text.Text;

public class AbstractLeaveEvent implements ClientConnectionEvent.Leave {

    protected LivePlayer player;
    protected AText leaveMessage;

    @Deprecated
    public AbstractLeaveEvent(LivePlayer player, Text leaveMessage) {
        this(player, leaveMessage.toAdventure());
    }

    public AbstractLeaveEvent(LivePlayer player, AText leaveMessage) {
        this.leaveMessage = leaveMessage;
        this.player = player;
    }

    @Override
    public LivePlayer getEntity() {
        return this.player;
    }

    @Override
    @Deprecated
    public Text getLeaveMessage() {
        return TranslateCore.buildText(this.leaveMessage.toLegacy());
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
