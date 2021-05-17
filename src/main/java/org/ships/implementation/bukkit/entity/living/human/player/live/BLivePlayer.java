package org.ships.implementation.bukkit.entity.living.human.player.live;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.source.viewer.CommandViewer;
import org.core.text.Text;
import org.core.world.position.impl.BlockPosition;
import org.ships.implementation.bukkit.VaultService;
import org.ships.implementation.bukkit.entity.BLiveEntity;
import org.ships.implementation.bukkit.entity.living.human.player.snapshot.BPlayerSnapshot;
import org.ships.implementation.bukkit.inventory.inventories.live.entity.BLivePlayerInventory;
import org.ships.implementation.bukkit.text.BText;
import org.ships.implementation.bukkit.world.position.impl.sync.BBlockPosition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class BLivePlayer extends BLiveEntity<org.bukkit.entity.Player> implements LivePlayer {

    @Deprecated
    public BLivePlayer(org.bukkit.entity.Entity entity) {
        this((org.bukkit.entity.Player) entity);
    }

    public BLivePlayer(org.bukkit.entity.Player entity) {
        super(entity);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BLivePlayer)) {
            return false;
        }
        BLivePlayer player2 = (BLivePlayer) object;
        return player2.getBukkitEntity().equals(this.getBukkitEntity());
    }

    @Override
    public boolean isViewingInventory() {
        return getBukkitEntity().getOpenInventory() != null;
    }

    @Override
    public PlayerInventory getInventory() {
        return new BLivePlayerInventory(this);
    }

    @Override
    public int getFoodLevel() {
        return getBukkitEntity().getFoodLevel();
    }

    @Override
    public double getExhaustionLevel() {
        return getBukkitEntity().getExhaustion();
    }

    @Override
    public double getSaturationLevel() {
        return getBukkitEntity().getSaturation();
    }

    @Override
    public String getName() {
        return getBukkitEntity().getName();
    }

    @Override
    public UUID getUniqueId() {
        return getBukkitEntity().getUniqueId();
    }

    @Override
    public boolean isSneaking() {
        return getBukkitEntity().isSneaking();
    }

    @Override
    public LivePlayer setFood(int value) throws IndexOutOfBoundsException {
        if (value > 20) {
            throw new IndexOutOfBoundsException();
        }
        getBukkitEntity().setFoodLevel(value);
        return this;
    }

    @Override
    public LivePlayer setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        if (value > 20) {
            throw new IndexOutOfBoundsException();
        }
        getBukkitEntity().setExhaustion((float) value);
        return this;
    }

    @Override
    public LivePlayer setSaturationLevel(double value) throws IndexOutOfBoundsException {
        if (value > 20) {
            throw new IndexOutOfBoundsException();
        }
        getBukkitEntity().setSaturation((float) value);
        return this;
    }

    @Override
    public LivePlayer setSneaking(boolean sneaking) {
        getBukkitEntity().setSneaking(sneaking);
        return this;
    }

    @Override
    public boolean hasPermission(String permission) {
        org.bukkit.entity.Player player = getBukkitEntity();
        String[] blocks = permission.split("\\.");
        for (int A = 0; A < blocks.length; A++) {
            StringBuilder builder = new StringBuilder();
            for (String block : blocks) {
                builder.append(block);
            }
            if (player.hasPermission(builder + ".*")) {
                return true;
            }
        }
        return player.hasPermission(permission);
    }

    @Override
    public Optional<BlockPosition> getBlockLookingAt(int scanLength) {
        Block block = this.getBukkitEntity().getTargetBlockExact(scanLength);
        if (block == null) {
            return Optional.empty();
        }
        return Optional.of(new BBlockPosition(block));
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new BPlayerSnapshot(this);
    }

    @Override
    public BLivePlayer setGravity(boolean check) {
        getBukkitEntity().setGravity(check);
        return this;
    }

    @Override
    public boolean hasGravity() {
        return getBukkitEntity().hasGravity();
    }

    @Override
    @Deprecated
    public CommandViewer sendMessage(Text message, UUID uuid) {
        try {
            this.entity.getClass().getDeclaredMethod("sendMessage", UUID.class, String.class).invoke(this.entity, uuid, ((BText) message).toBukkitString());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            sendMessage(message);
        }
        return this;
    }

    @Override
    @Deprecated
    public CommandViewer sendMessage(Text message) {
        getBukkitEntity().sendMessage(((BText) message).toBukkitString());
        return this;
    }

    @Override
    public CommandViewer sendMessagePlain(String message) {
        getBukkitEntity().sendMessage(org.bukkit.ChatColor.stripColor(message));
        return this;
    }

    @Override
    public CommandViewer sendMessage(AText message, UUID uuid) {
        getBukkitEntity().sendMessage(uuid, message.toLegacy());
        return this;
    }

    @Override
    public CommandViewer sendMessage(AText message) {
        Player player = getBukkitEntity();
        try {
            Class<?> componentClass = Class.forName("net.kyori.adventure.text.Component");
            Method method = player.getClass().getMethod("sendMessage", componentClass);
            method.invoke(player, ((AdventureText) message).getComponent());
        } catch (ClassCastException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            player.sendMessage(message.toLegacy());
        }
        return this;
    }

    @Override
    public boolean sudo(String wholeCommand) {
        return Bukkit.dispatchCommand(getBukkitEntity(), wholeCommand);
    }

    @Override
    public BigDecimal getBalance() {
        return BigDecimal.valueOf(VaultService.getBalance(this.getBukkitEntity()).orElse(0.0));
    }

    @Override
    public void setBalance(BigDecimal decimal) {
        VaultService.setBalance(this.getBukkitEntity(), decimal);
    }
}
