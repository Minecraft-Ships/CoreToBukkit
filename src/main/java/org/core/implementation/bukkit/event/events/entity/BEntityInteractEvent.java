package org.core.implementation.bukkit.event.events.entity;

import org.core.entity.living.human.player.LivePlayer;
import org.core.event.events.entity.EntityInteractEvent;
import org.core.world.direction.Direction;
import org.core.world.position.impl.sync.SyncBlockPosition;

public class BEntityInteractEvent {

    public static class PlayerInteractWithBlock implements EntityInteractEvent.WithBlock.AsPlayer {

        protected final SyncBlockPosition position;
        protected final LivePlayer player;
        protected final Direction direction;
        protected final int click;
        protected boolean cancelled;

        public PlayerInteractWithBlock(SyncBlockPosition position, int click, Direction clickedFace,
                LivePlayer player) {
            this.player = player;
            this.position = position;
            this.direction = clickedFace;
            this.click = click;
        }

        @Override
        public SyncBlockPosition getInteractPosition() {
            return this.position;
        }

        @Override
        public int getClickAction() {
            return this.click;
        }

        @Override
        public LivePlayer getEntity() {
            return this.player;
        }

        @Override
        public Direction getClickedBlockFace() {
            return this.direction;
        }

        @Override
        public boolean isCancelled() {
            return this.cancelled;
        }

        @Override
        public void setCancelled(boolean value) {
            this.cancelled = value;
        }
    }
}
