package org.core.implementation.bukkit.world.position.block.entity.skull;

import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockState;
import org.core.entity.living.human.player.User;
import org.core.implementation.bukkit.entity.living.human.player.live.BUser;
import org.core.implementation.bukkit.world.position.block.entity.AbstractLiveTileEntity;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.skull.LiveSkull;
import org.core.world.position.block.entity.skull.Skull;

import java.util.Optional;

public class BSkullEntity extends AbstractLiveTileEntity implements LiveSkull {

    public BSkullEntity(BlockState state) {
        super(state);
    }

    public org.bukkit.block.Skull getBukkitTileEntity() {
        return (org.bukkit.block.Skull) this.state;
    }

    @Override
    public Optional<User> getOwner() {
        OfflinePlayer user = this.getBukkitTileEntity().getOwningPlayer();
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(new BUser(user));
    }

    @Override
    public Skull setOwner(User user) {
        BUser user2 = (BUser) user;
        org.bukkit.OfflinePlayer user3 = user2.getBukkitUser();
        this.getBukkitTileEntity().setOwningPlayer(user3);
        this.getBukkitTileEntity().update();
        return this;
    }

    @Override
    public TileEntitySnapshot<? extends TileEntity> getSnapshot() {
        return new BSkullSnapshot(this);
    }
}
