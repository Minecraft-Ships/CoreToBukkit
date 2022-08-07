package org.core.implementation.bukkit.event;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.adventureText.format.NamedTextColours;
import org.core.entity.Entity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.event.Event;
import org.core.event.EventPriority;
import org.core.event.HEvent;
import org.core.event.events.entity.EntityInteractEvent;
import org.core.implementation.bukkit.entity.scene.live.BLiveDroppedItem;
import org.core.implementation.bukkit.event.events.block.AbstractBlockChangeEvent;
import org.core.implementation.bukkit.event.events.block.AbstractExplosionEvent;
import org.core.implementation.bukkit.event.events.block.tileentity.BSignChangeEvent;
import org.core.implementation.bukkit.event.events.connection.BJoinedEvent;
import org.core.implementation.bukkit.event.events.connection.BKickEvent;
import org.core.implementation.bukkit.event.events.entity.BEntityCommandEvent;
import org.core.implementation.bukkit.event.events.entity.BEntityInteractEvent;
import org.core.implementation.bukkit.event.events.entity.BEntitySpawnEvent;
import org.core.implementation.bukkit.platform.BukkitPlatform;
import org.core.implementation.bukkit.utils.DirectionUtils;
import org.core.implementation.bukkit.world.expload.EntityExplosion;
import org.core.implementation.bukkit.world.expload.EntityExplosionSnapshot;
import org.core.implementation.bukkit.world.position.block.details.blocks.BBlockDetails;
import org.core.implementation.bukkit.world.position.block.details.blocks.BlockStateSnapshot;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.implementation.bukkit.world.position.impl.sync.BExactPosition;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BukkitListener implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent event) {
        LivePlayer player = (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(
                event.getPlayer());
        BJoinedEvent cEvent = new BJoinedEvent(player);
        call(EventPriority.NORMAL, cEvent);
    }

    @EventHandler
    public static void onPlayerPlaceBlock(BlockPlaceEvent event) {
        BlockDetails old = new BBlockDetails(event.getBlockReplacedState().getBlockData(), false);
        BlockDetails new1 = new BBlockDetails(event.getBlock().getBlockData(), false);
        SyncBlockPosition position = new BBlockPosition(event.getBlock());
        LivePlayer player = (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(
                event.getPlayer());
        List<BlockSnapshot<SyncBlockPosition>> collection = Collections.singletonList(new1.createSnapshot(position));
        AbstractBlockChangeEvent.PlaceBlockPlayerPostEvent event2 =
                new AbstractBlockChangeEvent.PlaceBlockPlayerPostEvent(
                        position, old, new1, player, collection);
        call(EventPriority.NORMAL, event2);
        if (event2.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void onPlayerPlaceMultiBlock(BlockMultiPlaceEvent event) {
        BlockDetails old = new BBlockDetails(event.getBlockReplacedState().getBlockData(), false);
        BlockDetails new1 = new BBlockDetails(event.getBlock().getBlockData(), false);
        SyncBlockPosition position = new BBlockPosition(event.getBlock());
        LivePlayer player = (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(
                event.getPlayer());
        List<BlockSnapshot<SyncBlockPosition>> collection = new ArrayList<>();
        event.getReplacedBlockStates().forEach(bs -> collection.add(new BlockStateSnapshot(bs)));

        AbstractBlockChangeEvent.PlaceBlockPlayerPostEvent event2 =
                new AbstractBlockChangeEvent.PlaceBlockPlayerPostEvent(
                        position, old, new1, player, collection);
        call(EventPriority.NORMAL, event2);
        if (event2.isCancelled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void onItemSpawnEvent(ItemSpawnEvent event) {
        org.bukkit.entity.Item item = event.getEntity();
        BEntitySpawnEvent spawnEvent = new BEntitySpawnEvent(new BExactPosition(event.getLocation()),
                new BLiveDroppedItem(item));
        call(EventPriority.NORMAL, spawnEvent);
        boolean cancelled = spawnEvent.isCancelled();
        if (cancelled) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public static void onEntitySpawnEvent(EntitySpawnEvent event) {
        org.bukkit.entity.Entity entity = event.getEntity();
        BEntitySpawnEvent spawnEvent = new BEntitySpawnEvent(new BExactPosition(event.getLocation()),
                ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(entity));
        call(EventPriority.NORMAL, spawnEvent);
        boolean cancelled = spawnEvent.isCancelled();
        if (cancelled) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public static void onPlayerKickedEvent(PlayerKickEvent event) {
        LivePlayer player = (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(
                event.getPlayer());
        AText message = AText.ofLegacy(event.getLeaveMessage());
        BKickEvent kickEvent = new BKickEvent(player, message);
        call(EventPriority.HIGHEST, kickEvent);
        event.setLeaveMessage(kickEvent.getLeavingMessage().toLegacy());
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public static void onPlayerQuitEvent(PlayerQuitEvent event) {
        LivePlayer player = (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(
                event.getPlayer());
        String originalMessage = event.getQuitMessage();

        AText message = null;
        if (originalMessage != null) {
            message = AText.ofLegacy(originalMessage);
        }
        BKickEvent kickEvent = new BKickEvent(player, message);
        call(EventPriority.HIGHEST, kickEvent);
        AText text = kickEvent.getLeavingMessage();
        if (text instanceof AdventureText) {
            try {
                Object component = text.getClass().getMethod("getComponent").invoke(text);
                event.getClass().getMethod("quitMessage", component.getClass()).invoke(event, component);
                return;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
            }
        }
        AText leavingMessage = kickEvent.getLeavingMessage();
        if (leavingMessage != null) {
            event.setQuitMessage(leavingMessage.toLegacy());
        }
    }

    @EventHandler
    public static void onSignChangeEvent(SignChangeEvent event) {
        List<AText> lines = Stream
                .of(event.getLines())
                .map(AText::ofLegacy)
                .collect(Collectors.toList());
        LivePlayer player = (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(
                event.getPlayer());
        SyncBlockPosition position = new BBlockPosition(event.getBlock());
        BSignChangeEvent event1 = new BSignChangeEvent(player, position, lines);
        call(EventPriority.NORMAL, event1);
        for (int A = 0; A < 4; A++) {
            final int B = A;
            event1.getTo().getTextAt(A).ifPresent(l -> event.setLine(B, l.toLegacy()));
        }
        if (event1.isCancelled()) {
            event.setCancelled(event1.isCancelled());
        }
    }

    @EventHandler
    public static void onPlayerInteractWithBlockEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        int action = switch (event.getAction()) {
            case RIGHT_CLICK_BLOCK -> EntityInteractEvent.PRIMARY_CLICK_ACTION;
            case LEFT_CLICK_BLOCK -> EntityInteractEvent.SECONDARY_CLICK_ACTION;
            default -> -1;
        };
        BEntityInteractEvent.PlayerInteractWithBlock event1 = new BEntityInteractEvent.PlayerInteractWithBlock(
                new BBlockPosition(event.getClickedBlock()), action, DirectionUtils.toDirection(event.getBlockFace()),
                (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(event.getPlayer()));
        call(EventPriority.NORMAL, event1);
        if (event1.isCancelled()) {
            event.setCancelled(event1.isCancelled());
        }
    }

    @EventHandler
    public static void onExplode(org.bukkit.event.entity.EntityExplodeEvent event) {
        List<SyncBlockPosition> positions = new ArrayList<>();
        List<Block> list = event.blockList();
        for (Block value : list) {
            positions.add(new BBlockPosition(value));
        }
        Entity<?> entity = ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(event.getEntity());
        EntityExplosion explosion = new EntityExplosion(entity, positions);
        Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            SyncBlockPosition block = new BBlockPosition(iterator.next());
            AbstractBlockChangeEvent.BreakBlockChangeExplode event2 =
                    new AbstractBlockChangeEvent.BreakBlockChangeExplode(
                            block, explosion);
            call(EventPriority.NORMAL, event2);
            if (event2.isCancelled()) {
                iterator.remove();
                event.setCancelled(true);
                return;
            }
        }

        EntityExplosionSnapshot explosionSnapshot = new EntityExplosionSnapshot(explosion);
        AbstractExplosionEvent.Post event3 = new AbstractExplosionEvent.Post(explosionSnapshot);
        call(EventPriority.NORMAL, event3);
    }

    @EventHandler
    public static void onBlockBreakByPlayer(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Material material = player.getInventory().getItemInMainHand().getType();
        if (player.getGameMode() == GameMode.CREATIVE
                && (
                material == Material.WOODEN_SWORD
                        || material == Material.STONE_SWORD
                        || material == Material.IRON_SWORD
                        || material == Material.DIAMOND_SWORD
                        || material == Material.GOLDEN_SWORD)) {
            event.setCancelled(true);
            return;
        }
        AbstractBlockChangeEvent.BreakBlockChangeEventPlayer event1 =
                new AbstractBlockChangeEvent.BreakBlockChangeEventPlayer(
                        new BBlockPosition(event.getBlock()),
                        (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(
                                event.getPlayer()));
        call(EventPriority.NORMAL, event1);
        if (event1.isCancelled()) {
            event.setCancelled(event1.isCancelled());
        }
    }

    public static <E extends Event> E call(EventPriority priority, E event) {
        Set<BEventLaunch> methods = getMethods(priority, event.getClass());
        methods.forEach(m -> m.run(event));
        return event;
    }

    private static Set<BEventLaunch> getMethods(EventPriority priority, Class<? extends Event> classEvent) {
        Set<BEventLaunch> methods = new HashSet<>();
        TranslateCore.getEventManager().getEventListeners().forEach((key, value) -> value.forEach(el -> {
            for (Method method : el.getClass().getDeclaredMethods()) {
                HEvent hEvent = method.getAnnotation(HEvent.class);
                if (hEvent == null) {
                    continue;
                }
                if ((priority != EventPriority.IGNORE)
                        && hEvent.priority() != priority
                        && hEvent.priority() != EventPriority.IGNORE) {
                    continue;
                }
                if (methods.stream().anyMatch(m -> method.getName().contains("$"))) {
                    continue;
                }
                Parameter[] parameters = method.getParameters();
                if (parameters.length == 0) {
                    TranslateCore
                            .getConsole()
                            .sendMessage(
                                    AText
                                            .ofPlain("Failed to know what to do: HEvent found on method, "
                                                    + "but no event on "
                                                    + el.getClass().getName()
                                                    + "."
                                                    + method.getName()
                                                    + "()")
                                            .withColour(NamedTextColours.RED));
                    continue;
                }
                if (!Modifier.isPublic(method.getModifiers())) {
                    continue;
                }
                Class<?> class1 = parameters[0].getType();
                if (!Event.class.isAssignableFrom(classEvent)) {
                    String parameterNames = Arrays
                            .stream(parameters)
                            .map(p -> p.getType().getSimpleName() + " " + p.getName())
                            .collect(Collectors.joining(", "));
                    System.err.println("Failed to know what to do: HEvent found on method, but no known event on " +
                            el.getClass().getName() + "." + method.getName() + "(" +
                            parameterNames + ")");
                }
                if (class1.isAssignableFrom(classEvent)) {
                    methods.add(new BEventLaunch(key, el, method));
                }
            }
        }));
        return methods;
    }

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent event) {
        LivePlayer lPlayer = (LivePlayer) ((BukkitPlatform) TranslateCore.getPlatform()).createEntityInstance(
                event.getPlayer());
        BEntityCommandEvent event1 = new BEntityCommandEvent(lPlayer, event.getCommands());
        call(EventPriority.NORMAL, event1);
    }
}
