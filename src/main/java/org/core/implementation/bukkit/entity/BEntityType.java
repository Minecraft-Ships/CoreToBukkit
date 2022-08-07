package org.core.implementation.bukkit.entity;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.animal.chicken.ChickenSnapshot;
import org.core.entity.living.animal.chicken.LiveChicken;
import org.core.entity.living.animal.cow.CowSnapshot;
import org.core.entity.living.animal.cow.LiveCow;
import org.core.entity.living.animal.parrot.LiveParrot;
import org.core.entity.living.animal.parrot.ParrotSnapshot;
import org.core.entity.living.bat.BatSnapshot;
import org.core.entity.living.bat.LiveBat;
import org.core.entity.living.fish.cod.CodSnapshot;
import org.core.entity.living.fish.cod.LiveCod;
import org.core.entity.living.hostile.creeper.CreeperEntitySnapshot;
import org.core.entity.living.hostile.creeper.LiveCreeperEntity;
import org.core.entity.living.hostile.undead.classic.ClassicZombieSnapshot;
import org.core.entity.living.hostile.undead.classic.LiveClassicZombie;
import org.core.entity.living.human.HumanSnapshot;
import org.core.entity.living.human.LiveHuman;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.entity.projectile.item.snowball.LiveSnowballEntity;
import org.core.entity.projectile.item.snowball.SnowballEntitySnapshot;
import org.core.entity.scene.droppeditem.DroppedItemSnapshot;
import org.core.entity.scene.droppeditem.LiveDroppedItem;
import org.core.entity.scene.itemframe.ItemFrameSnapshot;
import org.core.entity.scene.itemframe.LiveItemFrame;
import org.core.implementation.bukkit.entity.living.animal.fish.live.BLiveCod;
import org.core.implementation.bukkit.entity.living.animal.fish.snapshot.BCodSnapshot;
import org.core.implementation.bukkit.entity.living.animal.live.BLiveChicken;
import org.core.implementation.bukkit.entity.living.animal.live.BLiveCow;
import org.core.implementation.bukkit.entity.living.animal.live.BLiveParrot;
import org.core.implementation.bukkit.entity.living.animal.snapshot.BChickenSnapshot;
import org.core.implementation.bukkit.entity.living.animal.snapshot.BCowSnapshot;
import org.core.implementation.bukkit.entity.living.animal.snapshot.BParrotSnapshot;
import org.core.implementation.bukkit.entity.living.bat.live.BLiveBat;
import org.core.implementation.bukkit.entity.living.bat.snapshot.BBatSnapshot;
import org.core.implementation.bukkit.entity.living.hostile.creeper.BCreeperSnapshot;
import org.core.implementation.bukkit.entity.living.hostile.creeper.BLiveCreeper;
import org.core.implementation.bukkit.entity.living.hostile.undead.classic.live.BLiveZombie;
import org.core.implementation.bukkit.entity.living.hostile.undead.classic.snapshot.BZombieSnapshot;
import org.core.implementation.bukkit.entity.living.human.player.live.BLivePlayer;
import org.core.implementation.bukkit.entity.living.human.player.snapshot.BPlayerSnapshot;
import org.core.implementation.bukkit.entity.projectile.item.live.BLiveSnowballEntity;
import org.core.implementation.bukkit.entity.projectile.item.snapshot.BSnowballEntitySnapshot;
import org.core.implementation.bukkit.entity.scene.live.BLiveDroppedItem;
import org.core.implementation.bukkit.entity.scene.live.BLiveItemFrame;
import org.core.implementation.bukkit.entity.scene.snapshot.BDroppedItemSnapshot;
import org.core.implementation.bukkit.entity.scene.snapshot.BItemFrameSnapshot;

public interface BEntityType<E extends LiveEntity, S extends EntitySnapshot<E>> extends EntityType<E, S> {

    org.bukkit.entity.EntityType getBukkitEntityType();

    class CreeperType implements BEntityType<LiveCreeperEntity, CreeperEntitySnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.CREEPER;
        }

        @Override
        public Class<? extends LiveCreeperEntity> getEntityClass() {
            return BLiveCreeper.class;
        }

        @Override
        public Class<? extends CreeperEntitySnapshot> getSnapshotClass() {
            return BCreeperSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Creeper";
        }
    }

    class ParrotType implements BEntityType<LiveParrot, ParrotSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.PARROT;
        }

        @Override
        public Class<? extends LiveParrot> getEntityClass() {
            return BLiveParrot.class;
        }

        @Override
        public Class<? extends ParrotSnapshot> getSnapshotClass() {
            return BParrotSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Parrot";
        }
    }

    class BatType implements BEntityType<LiveBat, BatSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.BAT;
        }

        @Override
        public Class<? extends LiveBat> getEntityClass() {
            return BLiveBat.class;
        }

        @Override
        public Class<? extends BatSnapshot> getSnapshotClass() {
            return BBatSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Bat";
        }
    }

    class SnowballType implements BEntityType<LiveSnowballEntity, SnowballEntitySnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.SNOWBALL;
        }

        @Override
        public Class<? extends LiveSnowballEntity> getEntityClass() {
            return BLiveSnowballEntity.class;
        }

        @Override
        public Class<? extends SnowballEntitySnapshot> getSnapshotClass() {
            return BSnowballEntitySnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Snowball";
        }
    }

    class DroppedItemType implements BEntityType<LiveDroppedItem, DroppedItemSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.DROPPED_ITEM;
        }

        @Override
        public Class<? extends LiveDroppedItem> getEntityClass() {
            return BLiveDroppedItem.class;
        }

        @Override
        public Class<? extends DroppedItemSnapshot> getSnapshotClass() {
            return BDroppedItemSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "DroppedItem";
        }
    }

    class PlayerType implements BEntityType<LivePlayer, PlayerSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.PLAYER;
        }

        @Override
        public Class<? extends LivePlayer> getEntityClass() {
            return BLivePlayer.class;
        }

        @Override
        public Class<? extends PlayerSnapshot> getSnapshotClass() {
            return BPlayerSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Player";
        }
    }

    class HumanType implements BEntityType<LiveHuman, HumanSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.PLAYER;
        }

        @Override
        public Class<? extends LiveHuman> getEntityClass() {
            return LiveHuman.class;
        }

        @Override
        public Class<? extends HumanSnapshot> getSnapshotClass() {
            return HumanSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Human";
        }
    }

    class CodType implements BEntityType<LiveCod, CodSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.COD;
        }

        @Override
        public Class<? extends LiveCod> getEntityClass() {
            return BLiveCod.class;
        }

        @Override
        public Class<? extends CodSnapshot> getSnapshotClass() {
            return BCodSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Cod";
        }
    }

    class ItemFrameType implements BEntityType<LiveItemFrame, ItemFrameSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.ITEM_FRAME;
        }

        @Override
        public Class<? extends LiveItemFrame> getEntityClass() {
            return BLiveItemFrame.class;
        }

        @Override
        public Class<? extends ItemFrameSnapshot> getSnapshotClass() {
            return BItemFrameSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "ItemFrame";
        }
    }

    class CowType implements BEntityType<LiveCow, CowSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.COW;
        }

        @Override
        public Class<? extends LiveCow> getEntityClass() {
            return BLiveCow.class;
        }

        @Override
        public Class<? extends CowSnapshot> getSnapshotClass() {
            return BCowSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Cow";
        }
    }

    class ChickenType implements BEntityType<LiveChicken, ChickenSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.CHICKEN;
        }

        @Override
        public Class<? extends LiveChicken> getEntityClass() {
            return BLiveChicken.class;
        }

        @Override
        public Class<? extends ChickenSnapshot> getSnapshotClass() {
            return BChickenSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Chicken";
        }
    }

    class ZombieType implements BEntityType<LiveClassicZombie, ClassicZombieSnapshot> {

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return org.bukkit.entity.EntityType.ZOMBIE;
        }

        @Override
        public Class<? extends LiveClassicZombie> getEntityClass() {
            return BLiveZombie.class;
        }

        @Override
        public Class<? extends ClassicZombieSnapshot> getSnapshotClass() {
            return BZombieSnapshot.class;
        }

        @Override
        public String getId() {
            return "minecraft:" + this.getName().toLowerCase();
        }

        @Override
        public String getName() {
            return "Zombie";
        }
    }

    class UnknownType<T extends org.bukkit.entity.Entity>
            implements BEntityType<UnknownLiveEntity<T>, UnknownEntitySnapshot<T>> {

        protected final org.bukkit.entity.EntityType type;

        public UnknownType(org.bukkit.entity.EntityType type) {
            this.type = type;
        }

        @Override
        public org.bukkit.entity.EntityType getBukkitEntityType() {
            return this.type;
        }

        @Override
        public Class<UnknownLiveEntity<T>> getEntityClass() {
            return (Class<UnknownLiveEntity<T>>) (Object) UnknownLiveEntity.class;
        }

        @Override
        public Class<UnknownEntitySnapshot<T>> getSnapshotClass() {
            return (Class<UnknownEntitySnapshot<T>>) (Object) UnknownEntitySnapshot.class;
        }

        @Override
        public String getId() {
            return this.type.getKey().toString();
        }

        @Override
        public String getName() {
            return this.type.getKey().getNamespace();
        }
    }
}
