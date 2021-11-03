package org.core.implementation.bukkit.platform.version;

import org.bukkit.block.BlockState;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.implementation.bukkit.entity.BEntityType;
import org.core.implementation.bukkit.entity.living.animal.fish.live.BLiveCod;
import org.core.implementation.bukkit.entity.living.animal.live.BLiveChicken;
import org.core.implementation.bukkit.entity.living.animal.live.BLiveCow;
import org.core.implementation.bukkit.entity.living.bat.live.BLiveBat;
import org.core.implementation.bukkit.entity.living.hostile.creeper.BLiveCreeper;
import org.core.implementation.bukkit.entity.living.hostile.undead.classic.live.BLiveZombie;
import org.core.implementation.bukkit.entity.living.human.player.live.BLivePlayer;
import org.core.implementation.bukkit.entity.projectile.item.live.BLiveSnowballEntity;
import org.core.implementation.bukkit.entity.scene.live.BLiveDroppedItem;
import org.core.implementation.bukkit.entity.scene.live.BLiveItemFrame;
import org.core.implementation.bukkit.world.position.block.entity.banner.BLiveBannerTileEntity;
import org.core.implementation.bukkit.world.position.block.entity.container.chest.BLiveChestTileEntity;
import org.core.implementation.bukkit.world.position.block.entity.container.dispenser.BLiveDispenserTileEntity;
import org.core.implementation.bukkit.world.position.block.entity.container.furnace.BFurnaceEntity;
import org.core.implementation.bukkit.world.position.block.entity.sign.BSignEntity;
import org.core.implementation.bukkit.world.position.block.entity.skull.BSkullEntity;
import org.core.world.position.block.entity.LiveTileEntity;

import java.util.*;

public class Specific1V13Platform implements BukkitSpecificPlatform {
    @Override
    public int[] getVersion() {
        return new int[]{1, 13, 0};
    }

    @Override
    public Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getSpecificEntityTypes() {
        return new HashSet<>();
    }

    @Override
    public Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getGeneralEntityTypes() {
        return new HashSet<>(
                Arrays.asList(
                        new BEntityType.ZombieType(),
                        new BEntityType.ChickenType(),
                        new BEntityType.CowType(),
                        new BEntityType.ItemFrameType(),
                        new BEntityType.DroppedItemType(),
                        new BEntityType.CodType(),
                        new BEntityType.ParrotType(),
                        new BEntityType.BatType(),
                        new BEntityType.SnowballType(),
                        new BEntityType.PlayerType(),
                        new BEntityType.HumanType(),
                        new BEntityType.CreeperType()
                ));
    }


    @Override
    public Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> getSpecificEntityToEntity() {
        Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> map = new HashMap<>();
        return map;
    }

    @Override
    public Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> getGeneralEntityToEntity() {
        Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> map = new HashMap<>();
        map.put(org.bukkit.entity.Player.class, BLivePlayer.class);
        map.put(org.bukkit.entity.Zombie.class, BLiveZombie.class);
        map.put(org.bukkit.entity.Chicken.class, BLiveChicken.class);
        map.put(org.bukkit.entity.Cow.class, BLiveCow.class);
        map.put(org.bukkit.entity.ItemFrame.class, BLiveItemFrame.class);
        map.put(org.bukkit.entity.Item.class, BLiveDroppedItem.class);
        map.put(org.bukkit.entity.Cod.class, BLiveCod.class);
        map.put(org.bukkit.entity.Bat.class, BLiveBat.class);
        map.put(org.bukkit.entity.Snowball.class, BLiveSnowballEntity.class);
        map.put(org.bukkit.entity.Creeper.class, BLiveCreeper.class);
        return map;
    }

    @Override
    public Map<Class<? extends BlockState>, Class<? extends LiveTileEntity>> getSpecificStateToTile() {
        Map<Class<? extends BlockState>, Class<? extends LiveTileEntity>> map = new HashMap<>();
        return map;
    }

    @Override
    public Map<Class<? extends BlockState>, Class<? extends LiveTileEntity>> getGeneralStateToTile() {
        Map<Class<? extends BlockState>, Class<? extends LiveTileEntity>> map = new HashMap<>();
        map.put(org.bukkit.block.Sign.class, BSignEntity.class);
        map.put(org.bukkit.block.Banner.class, BLiveBannerTileEntity.class);
        map.put(org.bukkit.block.Chest.class, BLiveChestTileEntity.class);
        map.put(org.bukkit.block.Dispenser.class, BLiveDispenserTileEntity.class);
        map.put(org.bukkit.block.Furnace.class, BFurnaceEntity.class);
        map.put(org.bukkit.block.Skull.class, BSkullEntity.class);
        return map;
    }
}
