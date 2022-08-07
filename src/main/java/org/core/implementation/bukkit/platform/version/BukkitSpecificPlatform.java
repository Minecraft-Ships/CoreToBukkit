package org.core.implementation.bukkit.platform.version;

import org.core.TranslateCore;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.platform.plugin.details.CorePluginVersion;
import org.core.world.position.block.entity.LiveTileEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface BukkitSpecificPlatform {

    static Set<BukkitSpecificPlatform> getPlatforms() {
        Set<BukkitSpecificPlatform> set = new HashSet<>();
        CorePluginVersion version = TranslateCore.getPlatform().getMinecraftVersion();
        if (version.getMajor() == 1) {
            if (version.getMinor() >= 13) {
                set.add(new Specific1V13Platform());
            }
            if (version.getMinor() >= 14) {
                set.add(new Specific1V14Platform());
            }
        }
        return set;
    }

    static Optional<BukkitSpecificPlatform> getSpecificPlatform() {
        CorePluginVersion version = TranslateCore.getPlatform().getMinecraftVersion();
        return getPlatforms()
                .stream()
                .filter(p -> version.getMajor() == p.getVersion()[0] && version.getMinor() == p.getVersion()[1])
                .findAny();
    }

    int[] getVersion();

    Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getSpecificEntityTypes();

    Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getGeneralEntityTypes();

    Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> getSpecificEntityToEntity();

    Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> getGeneralEntityToEntity();

    Map<Class<? extends org.bukkit.block.BlockState>, Class<? extends LiveTileEntity>> getSpecificStateToTile();

    Map<Class<? extends org.bukkit.block.BlockState>, Class<? extends LiveTileEntity>> getGeneralStateToTile();
}
