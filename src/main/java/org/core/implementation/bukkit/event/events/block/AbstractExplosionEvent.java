package org.core.implementation.bukkit.event.events.block;

import org.core.event.events.block.ExplosionEvent;
import org.core.world.expload.Explosion;

public abstract class AbstractExplosionEvent implements ExplosionEvent {

    public static class Post extends AbstractExplosionEvent implements ExplosionEvent.Post {

        private final Explosion.ExplosionSnapshot explosion;

        public Post(Explosion.ExplosionSnapshot snapshot) {
            this.explosion = snapshot;
        }

        @Override
        public Explosion.ExplosionSnapshot getExplosion() {
            return this.explosion;
        }
    }
}
