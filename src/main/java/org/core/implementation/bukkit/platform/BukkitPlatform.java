package org.core.implementation.bukkit.platform;

import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Parrot;
import org.bukkit.permissions.PermissionDefault;
import org.core.TranslateCore;
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
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.UnknownLiveEntity;
import org.core.implementation.bukkit.entity.living.animal.type.BParrotType;
import org.core.implementation.bukkit.entity.living.human.player.live.BLivePlayer;
import org.core.implementation.bukkit.event.BukkitListener;
import org.core.implementation.bukkit.inventory.item.BItemType;
import org.core.implementation.bukkit.inventory.item.data.dye.BItemDyeType;
import org.core.implementation.bukkit.permission.BukkitPermission;
import org.core.implementation.bukkit.platform.details.BukkitTranslatePlatformDetails;
import org.core.implementation.bukkit.platform.plugin.BPlugin;
import org.core.implementation.bukkit.platform.structure.BukkitStructurePlatform;
import org.core.implementation.bukkit.platform.version.BukkitSpecificPlatform;
import org.core.implementation.bukkit.world.boss.colour.BBossColour;
import org.core.implementation.bukkit.world.position.block.BBlockType;
import org.core.implementation.bukkit.world.position.block.details.blocks.grouptype.BBlockGroup;
import org.core.implementation.bukkit.world.position.block.entity.unknown.BLiveUnknownContainerTileEntity;
import org.core.implementation.bukkit.world.position.flags.BApplyPhysicsFlag;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.inventory.item.ItemType;
import org.core.inventory.item.data.dye.DyeType;
import org.core.inventory.item.data.dye.DyeTypes;
import org.core.inventory.item.type.ItemTypeCommon;
import org.core.permission.CorePermission;
import org.core.permission.Permission;
import org.core.platform.Platform;
import org.core.platform.PlatformDetails;
import org.core.platform.plugin.Plugin;
import org.core.platform.plugin.details.CorePluginVersion;
import org.core.platform.update.PlatformUpdate;
import org.core.platform.update.bukkit.DevBukkitUpdateChecker;
import org.core.source.command.CommandSource;
import org.core.source.projectile.ProjectileSource;
import org.core.utils.Identifiable;
import org.core.utils.Singleton;
import org.core.world.boss.colour.BossColour;
import org.core.world.boss.colour.BossColours;
import org.core.world.position.Positionable;
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
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.structure.Structure;
import org.core.world.structure.StructureBuilder;
import org.core.world.structure.StructureFileBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BukkitPlatform implements Platform {

    protected final Set<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> entityTypes = new HashSet<>();
    protected final Map<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>> entityToEntity = new HashMap<>();
    protected final Map<Class<? extends org.bukkit.block.BlockState>, Class<? extends LiveTileEntity>> blockStateToTileEntity = new HashMap<>();
    protected final Set<TileEntitySnapshot<? extends TileEntity>> defaultTileEntities = new HashSet<>();
    protected final Set<BlockGroup> blockGroups = new HashSet<>();
    @Deprecated
    protected final Set<UnspecificParser<?>> parsers = new HashSet<>();
    protected final Set<BlockType> blockTypes = new HashSet<>();
    protected final Set<ItemType> itemTypes = new HashSet<>();
    private final BukkitStructurePlatform structurePlatform;
    private final Collection<PlatformUpdate<?>> updateServices = new HashSet<>();

    public BukkitPlatform() {
        BukkitStructurePlatform structurePlatform1;
        try {
            Server.class.getDeclaredMethod("getStructureManager");
            structurePlatform1 = new BukkitStructurePlatform();
        } catch (NoSuchMethodException e) {
            structurePlatform1 = null;
        }
        this.structurePlatform = structurePlatform1;
    }

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

        Bukkit.getTags(Tag.REGISTRY_BLOCKS, Material.class).forEach(tag -> {
            String value = tag.getKey().toString().substring(tag.getKey().getNamespace().length() + 1);
            this.blockGroups.add(new BBlockGroup(value, tag
                    .getValues()
                    .stream()
                    .map(BBlockType::new)
                    .distinct()
                    .toArray(BlockType[]::new)));
        });
        this.blockGroups.addAll(BlockGroups.values());

        this.updateServices.add(new DevBukkitUpdateChecker());

    }

    public ProjectileSource getCoreProjectileSource(org.bukkit.projectiles.ProjectileSource source) {
        if (source instanceof org.bukkit.entity.Player) {
            return new BLivePlayer((org.bukkit.entity.Player) source);
        } else if (source instanceof org.bukkit.entity.Entity) {
            return (ProjectileSource) this.createEntityInstance((org.bukkit.entity.Entity) source);
        } else if (source instanceof org.bukkit.projectiles.BlockProjectileSource) {
            return (ProjectileSource) this
                    .createTileEntityInstance(
                            ((org.bukkit.projectiles.BlockProjectileSource) source).getBlock().getState())
                    .orElseThrow(() -> new RuntimeException(
                            "Unknown projectile source of " + source.getClass().getSimpleName()));
        }
        throw new RuntimeException("Unknown projectile source of " + source.getClass().getSimpleName());
    }

    public org.bukkit.projectiles.ProjectileSource getBukkitProjectileSource(ProjectileSource source) {
        if (source instanceof LiveTileEntity) {
            return (org.bukkit.projectiles.ProjectileSource) ((BAbstractPosition<Integer>) ((Positionable<SyncBlockPosition>) source).getPosition()).toBukkitBlock();
        } else if (source instanceof BLiveEntity) {
            return (org.bukkit.projectiles.ProjectileSource) ((BLiveEntity<?>) source).getBukkitEntity();
        }
        throw new RuntimeException("Unknown projectile source of " + source.getClass().getSimpleName());
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
            return TranslateCore.getConsole();
        }
        if (sender instanceof org.bukkit.entity.Player) {
            return new BLivePlayer((org.bukkit.entity.Player) sender);
        }
        throw new RuntimeException("Unknown command source of " + sender.getName());
    }

    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createSnapshot(EntityType<E, ? extends S> type,
                                                                                          SyncExactPosition position) {
        if (type.equals(EntityTypes.PLAYER.get()) || type.equals(EntityTypes.HUMAN.get())) {
            return Optional.empty();
        }
        try {
            return Optional.of(type.getSnapshotClass().getConstructor(SyncExactPosition.class).newInstance(position));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public LiveEntity createEntityInstance(org.bukkit.entity.Entity entity) {
        Optional<Map.Entry<Class<? extends org.bukkit.entity.Entity>, Class<? extends LiveEntity>>> opEntry = this.entityToEntity
                .entrySet()
                .stream()
                .filter(e -> e.getKey().isInstance(entity))
                .findAny();
        if (!opEntry.isPresent()) {
            return new UnknownLiveEntity<>(entity);
        }
        Class<? extends LiveEntity> bdclass = opEntry.get().getValue();
        try {
            return bdclass.getConstructor(org.bukkit.entity.Entity.class).newInstance(entity);
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(
                "Something went very wrong with entity " + entity.getName() + " | " + entity.getType().name());
    }

    public Optional<LiveTileEntity> createTileEntityInstance(org.bukkit.block.BlockState state) {
        Optional<Map.Entry<Class<? extends org.bukkit.block.BlockState>, Class<? extends LiveTileEntity>>> opEntry = this.blockStateToTileEntity
                .entrySet()
                .stream()
                .filter(e -> e.getKey().isInstance(state))
                .findAny();
        if (!opEntry.isPresent()) {
            if (state instanceof org.bukkit.block.Container) {
                return Optional.of(new BLiveUnknownContainerTileEntity((org.bukkit.block.Container) state));
            }
            return Optional.empty();
        }
        Class<? extends LiveTileEntity> bdclass = opEntry.get().getValue();
        try {
            return Optional.of(bdclass.getConstructor(org.bukkit.block.BlockState.class).newInstance(state));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private org.bukkit.Material getMaterial(String id) {
        return Arrays
                .stream(Material.values())
                .parallel()
                .filter(material -> material.getKey().toString().equals(id))
                .findAny()
                .orElseThrow(() -> new RuntimeException(
                        "Unknown material with id of '" + id + "' is your plugin too " + "new?"));
    }

    @Override
    public @NotNull PlatformDetails getImplementationDetails() {
        return new BukkitTranslatePlatformDetails();
    }

    @Override
    public @NotNull Collection<PlatformUpdate<?>> getUpdateCheckers() {
        return this.updateServices;
    }

    @Override
    public @NotNull Singleton<BossColour> get(BossColours colours) {
        return new Singleton<>(() -> Stream
                .of(BarColor.values())
                .filter(c -> c.name().equalsIgnoreCase(colours.getName()))
                .findAny()
                .map(BBossColour::new)
                .orElseThrow(() -> new RuntimeException("Could not find bar colour of " + colours.getName())));
    }

    @Override
    @Deprecated
    public <T> UnspecificParser<T> get(UnspecificParsers<T> parsers) {
        return (UnspecificParser<T>) this
                .getUnspecifiedParser(parsers.getId())
                .orElseThrow(() -> new IllegalStateException("Could not find " + parsers.getId()));
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
        return new Singleton<>(() -> new BItemType(this.getMaterial(itemId.getId())));
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
        return new Singleton<>(() -> this.entityTypes
                .stream()
                .filter(t -> t.getId().equals(entityId.getId()))
                .findAny()
                .map(e -> (EntityType<E, S>) e)
                .orElseThrow(() -> new RuntimeException("Could not find entity of " + entityId.getId())));
    }

    @Override
    public <E extends LiveEntity> Optional<EntityType<E, ? extends EntitySnapshot<E>>> getEntityType(String id) {
        Optional<EntityType<? extends LiveEntity, ? extends EntitySnapshot<? extends LiveEntity>>> opEntity = this.entityTypes
                .stream()
                .filter(t -> t.getId().equals(id))
                .findAny();
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
    public Optional<DyeType> getDyeType(String id) {
        for (org.bukkit.DyeColor colour : org.bukkit.DyeColor.values()) {
            Identifiable dyeType = new BItemDyeType(colour);
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
        return this.getApplyPhysics().stream().filter(f -> f.getId().equals(id)).findAny();
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
    public Collection<DyeType> getDyeTypes() {
        Collection<DyeType> set = new HashSet<>();
        for (org.bukkit.DyeColor colour : org.bukkit.DyeColor.values()) {
            set.add(new BItemDyeType(colour));
        }
        return set;
    }

    @Override
    public Collection<PatternLayerType> getPatternLayerTypes() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public Collection<BlockGroup> getBlockGroups() {
        return this.blockGroups;
    }

    @Override
    public Collection<BossColour> getBossColours() {
        return Stream.of(BarColor.values()).map(BBossColour::new).collect(Collectors.toSet());
    }

    @Override
    public Collection<ParrotType> getParrotType() {
        return Stream.of(Parrot.Variant.values()).map(BParrotType::new).collect(Collectors.toSet());
    }

    @Override
    public Collection<ApplyPhysicsFlag> getApplyPhysics() {
        Collection<ApplyPhysicsFlag> list = new ArrayList<>();
        list.add(BApplyPhysicsFlag.DEFAULT);
        list.add(BApplyPhysicsFlag.NONE);
        return Collections.unmodifiableCollection(list);
    }

    @Override
    public Collection<Permission> getPermissions() {
        return Bukkit
                .getServer()
                .getPluginManager()
                .getPermissions()
                .parallelStream()
                .map(p -> new BukkitPermission(p.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Structure> getStructures() {
        if (this.structurePlatform == null) {
            return Collections.emptyList();
        }
        return this.structurePlatform.getStructure();
    }

    @Override
    public @NotNull Structure register(StructureBuilder builder) {
        if (this.structurePlatform == null) {
            throw new RuntimeException(
                    "Structure support requires Bukkit 1.17 that was build after the 5th Oct 2021. In "
                            + "particular the commit of c01e2f07e99");
        }
        return this.structurePlatform.register(builder);
    }

    @Override
    public @NotNull Structure register(StructureFileBuilder builder) throws IOException {
        if (this.structurePlatform == null) {
            throw new RuntimeException(
                    "Structure support requires Bukkit 1.17 that was build after the 5th Oct 2021. In "
                            + "particular the commit of c01e2f07e99");
        }
        return this.structurePlatform.register(builder);
    }

    @Override
    public @NotNull Permission register(@NotNull String permissionNode) {
        return this.register(new CorePermission(false, permissionNode.split("\\.")));
    }

    @Override
    public @NotNull CorePermission register(CorePermission permissionNode) {
        if (Bukkit.getServer().getPluginManager().getPermission(permissionNode.getPermissionValue()) == null) {
            org.bukkit.permissions.Permission permission = new org.bukkit.permissions.Permission(
                    permissionNode.getPermissionValue());
            permission.setDefault(permissionNode.shouldDefaultHave() ? PermissionDefault.TRUE : PermissionDefault.OP);
            Bukkit.getServer().getPluginManager().addPermission(permission);
        }
        return permissionNode;
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
    public CorePluginVersion getMinecraftVersion() {
        String version = Bukkit.getServer().getVersion();
        try {
            version = version.split("MC: ")[1];
            version = version.substring(0, version.length() - 1);
            if (version.contains(" ")) {
                version = version.split(" ")[0];
            }
            String[] versionString = version.split(Pattern.quote("."));
            int major = Integer.parseInt(versionString[0]);
            int minor = Integer.parseInt(versionString[1]);
            int patch = 0;
            if (versionString.length >= 3) {
                patch = Integer.parseInt(versionString[2]);
            }
            return new CorePluginVersion(major, minor, patch);
        } catch (ArrayIndexOutOfBoundsException e) {
            //fix for Pukkit (Pocket Edition of Spigot)
            if (version.startsWith("v")) {
                String[] versionString = version.substring(1).split(Pattern.quote("."));
                return new CorePluginVersion(Integer.parseInt(versionString[0]), Integer.parseInt(versionString[1]),
                                             Integer.parseInt(versionString[2]));
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
        return Arrays.stream(Bukkit.getPluginManager().getPlugins()).map(BPlugin::new).collect(Collectors.toSet());
    }

    @Override
    public File getPlatformPluginsFolder() {
        return new File("plugins");
    }

    @Override
    public File getPlatformConfigFolder() {
        return this.getPlatformPluginsFolder();
    }

    @Override
    public <E extends CustomEvent> E callEvent(E event) {
        return BukkitListener.call(EventPriority.IGNORE, event);
    }
}
