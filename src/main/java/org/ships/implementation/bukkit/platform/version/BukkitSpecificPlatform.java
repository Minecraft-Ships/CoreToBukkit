package org.ships.implementation.bukkit.platform.version;

import org.core.TranslateCore;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.world.position.block.entity.LiveTileEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface BukkitSpecificPlatform {

    int[] getVersion();

    Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getSpecificEntityTypes();
    Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getGeneralEntityTypes();

    Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> getSpecificEntityToEntity();
    Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> getGeneralEntityToEntity();

    Map<Class<? extends org.bukkit.block.BlockState>, Class<? extends LiveTileEntity>> getSpecificStateToTile();
    Map<Class<? extends org.bukkit.block.BlockState>, Class<? extends LiveTileEntity>> getGeneralStateToTile();

    static Set<BukkitSpecificPlatform> getPlatforms(){
        Set<BukkitSpecificPlatform> set = new HashSet<>();
        int[] version = TranslateCore.getPlatform().getMinecraftVersion();
        if(version[0] == 1){
            if(version[1] >= 13){
                set.add(new Specific1V13Platform());
            }
            if(version[1] >= 14){
                set.add(new Specific1V14Platform());
            }
        }
        return set;
    }

    static Optional<BukkitSpecificPlatform> getSpecificPlatform(){
        int[] version = TranslateCore.getPlatform().getMinecraftVersion();
        return getPlatforms().stream().filter(p -> version[0] == p.getVersion()[0] && version[1] == p.getVersion()[1]).findAny();
    }
}
