package org.ships.implementation.bukkit.platform;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Parrot;
import org.core.CorePlugin;
import org.core.config.ConfigurationFormat;
import org.core.config.parser.unspecific.UnspecificParser;
import org.core.config.parser.unspecific.UnspecificParsers;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.LiveEntity;
import org.core.entity.living.animal.parrot.ParrotType;
import org.core.entity.living.animal.parrot.ParrotTypes;
import org.core.event.CustomEvent;
import org.core.event.EventPriority;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.data.dye.DyeType;
import org.core.inventory.item.data.dye.DyeTypes;
import org.core.inventory.item.type.ItemTypeCommon;
import org.core.permission.Permission;
import org.core.platform.Platform;
import org.core.platform.PlatformDetails;
import org.core.platform.Plugin;
import org.core.source.command.CommandSource;
import org.core.source.projectile.ProjectileSource;
import org.core.text.TextColour;
import org.core.text.TextColours;
import org.core.utils.Singleton;
import org.core.world.boss.colour.BossColour;
import org.core.world.boss.colour.BossColours;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.block.entity.TileEntity;
import org.core.world.position.block.entity.TileEntitySnapshot;
import org.core.world.position.block.entity.banner.pattern.PatternLayerType;
import org.core.world.position.block.entity.banner.pattern.PatternLayerTypes;
import org.core.world.position.block.grouptype.BlockGroup;
import org.core.world.position.block.grouptype.BlockGroups;
import org.core.world.position.flags.physics.ApplyPhysicsFlag;
import org.core.world.position.flags.physics.ApplyPhysicsFlags;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.jetbrains.annotations.NotNull;
import org.ships.implementation.bukkit.entity.BLiveEntity;
import org.ships.implementation.bukkit.entity.UnknownLiveEntity;
import org.ships.implementation.bukkit.entity.living.animal.type.BParrotType;
import org.ships.implementation.bukkit.entity.living.human.player.live.BLivePlayer;
import org.ships.implementation.bukkit.event.BukkitListener;
import org.ships.implementation.bukkit.inventory.item.BItemType;
import org.ships.implementation.bukkit.inventory.item.data.dye.BItemDyeType;
import org.ships.implementation.bukkit.permission.BukkitPermission;
import org.ships.implementation.bukkit.platform.version.BukkitSpecificPlatform;
import org.ships.implementation.bukkit.text.BTextColour;
import org.ships.implementation.bukkit.world.boss.colour.BBossColour;
import org.ships.implementation.bukkit.world.position.block.BBlockType;
import org.ships.implementation.bukkit.world.position.block.details.blocks.grouptype.BBlockGroup;
import org.ships.implementation.bukkit.world.position.block.entity.unknown.BLiveUnknownContainerTileEntity;
import org.ships.implementation.bukkit.world.position.flags.BApplyPhysicsFlag;
import org.ships.implementation.bukkit.world.position.impl.sync.BBlockPosition;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BukkitPlatform implements Platform {

    protected Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> entityTypes = new HashSet<>();
    protected Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> entityToEntity = new HashMap<>();
    protected Map<Class<? extends org.bukkit.block.BlockState>, Class<? extends LiveTileEntity>> blockStateToTileEntity = new HashMap<>();
    protected Set<TileEntitySnapshot<? extends TileEntity>> defaultTileEntities = new HashSet<>();
    protected Set<BlockGroup> blockGroups = new HashSet<>();
    protected @Deprecated
    Set<UnspecificParser<?>> parsers = new HashSet<>();
    protected Set<BlockType> blockTypes = new HashSet<>();
    protected Set<ItemType> itemTypes = new HashSet<>();

    public void init() {
        for (Material material : Material.values()) {
            if (material.isBlock()) {
                BlockType type = new BBlockType(material);
                this.blockTypes.add(type);
            }
            if (material.isItem()) {
                this.itemTypes.add(new BItemType(material));
            }
        }
        BukkitSpecificPlatform.getPlatforms().forEach(bsp -> {
            this.entityToEntity.putAll(bsp.getGeneralEntityToEntity());
            this.entityTypes.addAll(bsp.getGeneralEntityTypes());
            this.blockStateToTileEntity.putAll(bsp.getGeneralStateToTile());
        });
        BukkitSpecificPlatform.getSpecificPlatform().ifPresent(bsp -> {
            this.entityToEntity.putAll(bsp.getSpecificEntityToEntity());
            this.entityTypes.addAll(bsp.getSpecificEntityTypes());
            this.blockStateToTileEntity.putAll(bsp.getSpecificStateToTile());
        });
        Class<org.bukkit.Tag> classTag = org.bukkit.Tag.class;
        for (Field field : classTag.getFields()) {
            if (!field.getType().isAssignableFrom(classTag)) {
                continue;
            }
            Set<BlockType> blockType = new HashSet<>();
            try {
                org.bukkit.Tag<?> tag = (org.bukkit.Tag<?>) field.get(null);
                tag.getValues().forEach(v -> {
                    if (!(v instanceof Material)) {
                        return;
                    }
                    Material m = (Material) v;
                    if (m.isBlock()) {
                        blockType.add(new BBlockType(m));
                    }
                });
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            this.blockGroups.add(new BBlockGroup(field.getName(), blockType.toArray(new BlockType[blockType.size()])));
        }
        this.blockGroups.addAll(BlockGroups.values());

    }

    public ProjectileSource getCoreProjectileSource(org.bukkit.projectiles.ProjectileSource source) {
        if (source instanceof org.bukkit.entity.Player) {
            return new BLivePlayer((org.bukkit.entity.Player) source);
        } else if (source instanceof org.bukkit.entity.Entity) {
            return (ProjectileSource) createEntityInstance((org.bukkit.entity.Entity) source);
        } else if (source instanceof org.bukkit.projectiles.BlockProjectileSource) {
            return (ProjectileSource) createTileEntityInstance(((org.bukkit.projectiles.BlockProjectileSource) source).getBlock().getState()).get();
        }
        return null;
    }

    public org.bukkit.projectiles.ProjectileSource getBukkitProjectileSource(ProjectileSource source) {
        if (source instanceof LiveTileEntity) {
            return (org.bukkit.projectiles.BlockProjectileSource) ((BBlockPosition) ((LiveTileEntity) source).getPosition()).toBukkitBlock();
        } else if (source instanceof BLiveEntity) {
            return (org.bukkit.projectiles.ProjectileSource) ((BLiveEntity) source).getBukkitEntity();
        }
        return null;
    }

    public Map<Class<? extends org.bukkit.block.BlockState>, Class<? extends LiveTileEntity>> getBukkitBlockStateToCoreTileEntity() {
        return this.blockStateToTileEntity;
    }

    public Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> getBukkitEntityToCoreEntityMap() {
        return this.entityToEntity;
    }

    public Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getEntityTypeSet() {
        return this.entityTypes;
    }

    public CommandSource getSource(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return CorePlugin.getConsole();
        }
        if (sender instanceof org.bukkit.entity.Player) {
            return new BLivePlayer((org.bukkit.entity.Player) sender);
        }
        return null;
    }

    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createSnapshot(EntityType<E, S> type, SyncExactPosition position) {
        if (type.equals(EntityTypes.PLAYER) || type.equals(EntityTypes.HUMAN)) {
            return Optional.empty();
        }
        try {
            return Optional.of(type.getSnapshotClass().getConstructor(SyncExactPosition.class).newInstance(position));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public LiveEntity createEntityInstance(org.bukkit.entity.Entity entity) {
        Optional<Map.Entry<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>>> opEntry = entityToEntity.entrySet().stream().filter(e -> e.getKey().isInstance(entity)).findAny();
        if (!opEntry.isPresent()) {
            return new UnknownLiveEntity<>(entity);
        }
        Class<? extends LiveEntity> bdclass = opEntry.get().getValue();
        try {
            return bdclass.getConstructor(org.bukkit.entity.Entity.class).newInstance(entity);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.err.println("Welp. Something went very wrong with entity " + entity.getName() + " | " + entity.getType().name());
        return null;
    }

    public Optional<LiveTileEntity> createTileEntityInstance(org.bukkit.block.BlockState state) {
        Optional<Map.Entry<Class<? extends org.bukkit.block.BlockState>, Class<? extends LiveTileEntity>>> opEntry = blockStateToTileEntity.entrySet().stream().filter(e -> e.getKey().isInstance(state)).findAny();
        if (!opEntry.isPresent()) {
            if (state instanceof org.bukkit.block.Container) {
                return Optional.of(new BLiveUnknownContainerTileEntity((org.bukkit.block.Container) state));
            }
            return Optional.empty();
        }
        Class<? extends LiveTileEntity> bdclass = opEntry.get().getValue();
        try {
            return Optional.of(bdclass.getConstructor(org.bukkit.block.BlockState.class).newInstance(state));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private org.bukkit.Material getMaterial(String id, boolean block) {
        for (org.bukkit.Material material : org.bukkit.Material.values()) {
            if ((block && material.isBlock()) || (!block && material.isItem())) {
                if (material.getKey().toString().equals(id)) {
                    return material;
                }
            }
        }
        return null;
    }


    @Override
    public @NotNull Singleton<BossColour> get(BossColours colours) {
        return new Singleton<>(() ->
                Stream
                        .of(BarColor.values())
                        .filter(c -> c.name().equalsIgnoreCase(colours.getName()))
                        .findAny()
                        .map(BBossColour::new)
                        .orElseThrow(() -> new RuntimeException("Could not find bar colour of " + colours.getName())));
    }

    @Override
    @Deprecated
    public <T> UnspecificParser<T> get(UnspecificParsers<T> parsers) {
        return (UnspecificParser<T>) getUnspecifiedParser(parsers.getId()).get();
    }

    @Override
    public @NotNull Singleton<ApplyPhysicsFlag> get(ApplyPhysicsFlags flags) {
        if (flags.getName().equals("None")) {
            return new Singleton<>(() -> BApplyPhysicsFlag.NONE);
        }
        return new Singleton<>(() -> BApplyPhysicsFlag.DEFAULT);
    }

    @Override
    public @NotNull Singleton<ItemType> get(ItemTypeCommon itemId) {
        return new Singleton<>(() -> new BItemType(getMaterial(itemId.getId(), false)));
    }

    @Override
    public @NotNull Singleton<ParrotType> get(ParrotTypes parrotID) {
        return new Singleton<>(() -> Stream
                .of(Parrot.Variant.values())
                .filter(v -> v.name().equalsIgnoreCase(parrotID.getName()))
                .findAny()
                .map(BParrotType::new)
                .orElseThrow(() -> new RuntimeException("Could not find parrot type of " + parrotID.getId())));
    }

    @Override
    @Deprecated
    public @NotNull TextColour get(TextColours id) {
        for (org.bukkit.ChatColor color : org.bukkit.ChatColor.values()) {
            if (id.getId().equals("minecraft:" + color.name().toLowerCase())) {
                return new BTextColour(color);
            }
        }
        throw new RuntimeException("Could not find Text Colour of " + id.getId());
    }

    @Override
    public @NotNull Singleton<DyeType> get(DyeTypes id) {
        return new Singleton<>(() -> Stream
                .of(DyeColor.values())
                .filter(d -> id.getId().equalsIgnoreCase("minecraft:" + d.name().toLowerCase()))
                .findAny()
                .map(BItemDyeType::new)
                .orElseThrow(() -> new RuntimeException("Could not find Dye colour of " + id.getId())));
    }

    @Override
    public @NotNull Singleton<PatternLayerType> get(PatternLayerTypes id) {
        return new Singleton<>(() -> {
            throw new RuntimeException("Not implemented");
        });
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> @NotNull Singleton<EntityType<E, S>> get(EntityTypes<E, S> entityId) {
        return new Singleton<>(() -> this
                .entityTypes
                .stream()
                .filter(t -> t.getId().equals(entityId.getId()))
                .findAny()
                .map(e -> (EntityType<E, S>) e)
                .orElseThrow(() -> new RuntimeException("Could not find entity of " + entityId.getId())));
    }

    @Override
    public <E extends LiveEntity> Optional<EntityType<E, ? extends EntitySnapshot<E>>> getEntityType(String id) {
        Optional<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> opEntity = this.entityTypes.stream().filter(t -> t.getId().equals(id)).findAny();
        if (opEntity.isPresent()) {
            EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>> entityType = opEntity.get();
            return Optional.of((EntityType<E, ? extends EntitySnapshot<E>>) entityType);
        }
        return Optional.empty();
    }

    @Override
    public Optional<BlockType> getBlockType(String id) {
        return this.blockTypes.stream().filter(bt -> bt.getId().equals(id)).findAny();

    }

    @Override
    public Optional<ItemType> getItemType(String id) {
        return this.itemTypes.stream().filter(it -> it.getId().equals(id)).findAny();
    }

    @Override
    @Deprecated
    public Optional<TextColour> getTextColour(String id) {
        for (org.bukkit.ChatColor color : org.bukkit.ChatColor.values()) {
            BTextColour colour = new BTextColour(color);
            if (colour.getId().equals(id)) {
                return Optional.of(colour);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<DyeType> getDyeType(String id) {
        for (org.bukkit.DyeColor colour : org.bukkit.DyeColor.values()) {
            BItemDyeType dyeType = new BItemDyeType(colour);
            if (dyeType.getId().equals(id)) {
                return Optional.of(new BItemDyeType(colour));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<PatternLayerType> getPatternLayerType(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<BossColour> getBossColour(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<ParrotType> getParrotType(String id) {
        for (org.bukkit.entity.Parrot.Variant variant : org.bukkit.entity.Parrot.Variant.values()) {
            if (("minecraft:parrot_variant_" + variant.name().toLowerCase()).equalsIgnoreCase(id)) {
                return Optional.of(new BParrotType(variant));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<ApplyPhysicsFlag> getApplyPhysics(String id) {
        return getApplyPhysics().stream().filter(f -> f.getId().equals(id)).findAny();
    }

    @Override
    @Deprecated
    public Optional<UnspecificParser<?>> getUnspecifiedParser(String id) {
        return this.parsers.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    @Override
    public Collection<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> getEntityTypes() {
        return new HashSet<>(this.entityTypes);
    }

    @Override
    public Collection<BlockType> getBlockTypes() {
        return Collections.unmodifiableCollection(this.blockTypes);
    }

    @Override
    public Collection<ItemType> getItemTypes() {
        return Collections.unmodifiableCollection(this.itemTypes);

    }

    @Override
    @Deprecated
    public Collection<TextColour> getTextColours() {
        Set<TextColour> set = new HashSet<>();
        for (org.bukkit.ChatColor color : org.bukkit.ChatColor.values()) {
            set.add(new BTextColour(color));
        }
        return set;
    }

    @Override
    public Collection<DyeType> getDyeTypes() {
        Set<DyeType> set = new HashSet<>();
        for (org.bukkit.DyeColor colour : org.bukkit.DyeColor.values()) {
            set.add(new BItemDyeType(colour));
        }
        return set;
    }

    @Override
    public Collection<PatternLayerType> getPatternLayerTypes() {
        return null;
    }

    @Override
    public Collection<BlockGroup> getBlockGroups() {
        return this.blockGroups;
    }

    @Override
    public Collection<BossColour> getBossColours() {
        Set<BossColour> set = new HashSet<>();
        for (BarColor color : BarColor.values()) {
            set.add(new BBossColour(color));
        }
        return set;
    }

    @Override
    public Collection<ParrotType> getParrotType() {
        List<ParrotType> list = new ArrayList<>();
        for (org.bukkit.entity.Parrot.Variant variant : org.bukkit.entity.Parrot.Variant.values()) {
            list.add(new BParrotType(variant));
        }
        return Collections.unmodifiableCollection(list);
    }

    @Override
    public Collection<ApplyPhysicsFlag> getApplyPhysics() {
        List<ApplyPhysicsFlag> list = new ArrayList<>();
        list.add(BApplyPhysicsFlag.DEFAULT);
        list.add(BApplyPhysicsFlag.NONE);
        return Collections.unmodifiableCollection(list);
    }

    @Override
    public Collection<Permission> getPermissions() {
        return Bukkit.getServer().getPluginManager().getPermissions().parallelStream().map(p -> new BukkitPermission(p.getName())).collect(Collectors.toList());
    }

    @Override
    public @NotNull Permission register(@NotNull String permissionNode) {
        if (Bukkit.getServer().getPluginManager().getPermission(permissionNode) == null) {
            BukkitPermission permission = new BukkitPermission(permissionNode);
            Bukkit.getServer().getPluginManager().addPermission(new org.bukkit.permissions.Permission(permissionNode));
            return permission;
        }
        return new BukkitPermission(permissionNode);
    }


    @Override
    @Deprecated
    public Collection<UnspecificParser<?>> getUnspecifiedParsers() {
        return this.parsers;
    }

    @Override
    public Collection<TileEntitySnapshot<? extends TileEntity>> getDefaultTileEntities() {
        return this.defaultTileEntities;
    }

    @Override
    public int[] getMinecraftVersion() {
        String version = Bukkit.getServer().getVersion();
        try {
            version = version.split("MC: ")[1];
            version = version.substring(0, version.length() - 1);
            if (version.contains(" ")) {
                version = version.split(" ")[0];
            }
            String[] versionString = version.split(Pattern.quote("."));
            int[] versionInt = new int[3];
            for (int A = 0; A < versionString.length; A++) {
                versionInt[A] = Integer.parseInt(versionString[A]);
            }
            return versionInt;
        } catch (ArrayIndexOutOfBoundsException e) {
            //fix for Pukkit (Pocket Edition of Spigot)
            if (version.startsWith("v")) {
                String[] versionString = version.substring(1).split(Pattern.quote("."));
                int[] versionInt = new int[3];
                for (int A = 0; A < versionString.length; A++) {
                    versionInt[A] = Integer.parseInt(versionString[A]);
                }
                return versionInt;
            }
            throw e;
        }

    }

    @Override
    public @NotNull PlatformDetails getDetails() {
        return new BPlatformDetails();
    }

    @Override
    public @NotNull ConfigurationFormat getConfigFormat() {
        return ConfigurationFormat.FORMAT_YAML;
    }

    @Override
    public Set<Plugin> getPlugins() {
        Set<Plugin> plugins = new HashSet<>();
        for (org.bukkit.plugin.Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            plugins.add(new BPlugin(plugin));
        }
        return plugins;
    }

    @Override
    public <E extends CustomEvent> E callEvent(E event) {
        return BukkitListener.call(EventPriority.IGNORE, event);
    }
}
