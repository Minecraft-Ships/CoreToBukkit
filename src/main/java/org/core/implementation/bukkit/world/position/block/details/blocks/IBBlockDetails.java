package org.core.implementation.bukkit.world.position.block.details.blocks;

import org.core.world.position.block.details.BlockDetails;

public interface IBBlockDetails extends BlockDetails {

    org.bukkit.block.data.BlockData getBukkitData();

    void setBukkitData(org.bukkit.block.data.BlockData data);
}
