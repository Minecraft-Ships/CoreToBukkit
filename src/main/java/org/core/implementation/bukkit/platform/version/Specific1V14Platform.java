package org.core.implementation.bukkit.platform.version;

import org.bukkit.block.BlockState;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.world.position.block.entity.LiveTileEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Specific1V14Platform implements BukkitSpecificPlatform {
    @Override
    public int[] getVersion() {
        return new int[]{1, 14, 0};
    }

    @Override
    public Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getSpecificEntityTypes() {
        return new HashSet<>();
    }

    @Override
    public Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getGeneralEntityTypes() {
        return new HashSet<>();
    }

    @Override
    public Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> getSpecificEntityToEntity() {
        return new HashMap<>();
    }

    @Override
    public Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> getGeneralEntityToEntity() {
        return new HashMap<>();
    }

    @Override
    public Map<Class<? extends BlockState>, Class<? extends LiveTileEntity>> getSpecificStateToTile() {
        return new HashMap<>();
    }

    @Override
    public Map<Class<? extends BlockState>, Class<? extends LiveTileEntity>> getGeneralStateToTile() {
        Map<Class<? extends BlockState>, Class<? extends LiveTileEntity>> map = new HashMap<>();
        return map;
    }
}
