package org.core.implementation.bukkit.entity.living.human.player.live;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.core.adventureText.AText;
import org.core.adventureText.adventure.AdventureText;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.implementation.bukkit.VaultService;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.entity.living.human.player.snapshot.BPlayerSnapshot;
import org.core.implementation.bukkit.inventory.inventories.live.entity.BLivePlayerInventory;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.permission.CorePermission;
import org.core.permission.Permission;
import org.core.source.viewer.CommandViewer;
import org.core.world.position.impl.BlockPosition;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class BLivePlayer extends BLiveEntity<Player> implements LivePlayer {

    @Deprecated
    public BLivePlayer(org.bukkit.entity.Entity entity) {
        this((org.bukkit.entity.Player) entity);
    }

    public BLivePlayer(org.bukkit.entity.Player entity) {
        super(entity);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BLivePlayer player2)) {
            return false;
        }
        return player2.getBukkitEntity().equals(this.getBukkitEntity());
    }

    @Override
    public boolean isViewingInventory() {
        return this.getBukkitEntity().getOpenInventory() != null;
    }

    @Override
    public PlayerInventory getInventory() {
        return new BLivePlayerInventory(this);
    }

    @Override
    public int getFoodLevel() {
        return this.getBukkitEntity().getFoodLevel();
    }

    @Override
    public double getExhaustionLevel() {
        return this.getBukkitEntity().getExhaustion();
    }

    @Override
    public double getSaturationLevel() {
        return this.getBukkitEntity().getSaturation();
    }

    @Override
    public String getName() {
        return this.getBukkitEntity().getName();
    }

    @Override
    public UUID getUniqueId() {
        return this.getBukkitEntity().getUniqueId();
    }

    @Override
    public boolean isSneaking() {
        return this.getBukkitEntity().isSneaking();
    }

    @Override
    public LivePlayer setFood(int value) throws IndexOutOfBoundsException {
        this.getBukkitEntity().setFoodLevel(value);
        return this;
    }

    @Override
    public LivePlayer setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        this.getBukkitEntity().setExhaustion((float) value);
        return this;
    }

    @Override
    public LivePlayer setSaturationLevel(double value) throws IndexOutOfBoundsException {
        this.getBukkitEntity().setSaturation((float) value);
        return this;
    }

    @Override
    public LivePlayer setSneaking(boolean sneaking) {
        this.getBukkitEntity().setSneaking(sneaking);
        return this;
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
    public boolean hasPermission(Permission permission) {
        return this.getBukkitEntity().hasPermission(permission.getPermissionValue());
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new BPlayerSnapshot(this);
    }

    @Override
    public BLivePlayer setGravity(boolean check) {
        this.getBukkitEntity().setGravity(check);
        return this;
    }

    @Override
    public boolean hasGravity() {
        return this.getBukkitEntity().hasGravity();
    }

    @Override
    public CommandViewer sendMessage(AText message, UUID uuid) {
        this.getBukkitEntity().sendMessage(uuid, message.toLegacy());
        return this;
    }

    @Override
    public CommandViewer sendMessage(AText message) {
        Player player = this.getBukkitEntity();
        try {
            Class<?> componentClass = Class.forName("net.kyori.adventure.text.Component");
            Method method = player.getClass().getMethod("sendMessage", componentClass);
            method.invoke(player, ((AdventureText) message).getComponent());
        } catch (ClassCastException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            player.sendMessage(message.toLegacy());
        }
        return this;
    }

    @Override
    public boolean sudo(String wholeCommand) {
        return Bukkit.dispatchCommand(this.getBukkitEntity(), wholeCommand);
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
