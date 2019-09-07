package org.ships.implementation.bukkit.entity.living.human.player.snapshot;

import org.core.entity.living.human.AbstractHuman;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.PlayerSnapshot;
import org.core.inventory.inventories.general.entity.PlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;
import org.core.world.position.ExactPosition;
import org.ships.implementation.bukkit.entity.BEntitySnapshot;

import java.util.UUID;

public class BPlayerSnapshot extends BEntitySnapshot<LivePlayer> implements PlayerSnapshot {

    protected String name;
    protected PlayerInventorySnapshot inventorySnapshot;
    protected int foodLevel;
    protected double exhaustionLevel;
    protected double saturationLevel;
    protected boolean sneaking;

    public BPlayerSnapshot(LivePlayer player){
        super(player);
        setRoll(player.getRoll());
        setExhaustionLevel(player.getExhaustionLevel());
        setFood(player.getFoodLevel());
        setSaturationLevel(player.getSaturationLevel());
        setPitch(player.getPitch());
        setYaw(player.getYaw());
        this.inventorySnapshot = player.getInventory().createSnapshot();
    }

    public BPlayerSnapshot(PlayerSnapshot player){
        super(player);
        setRoll(player.getRoll());
        setExhaustionLevel(player.getExhaustionLevel());
        setFood(player.getFoodLevel());
        setSaturationLevel(player.getSaturationLevel());
        setPitch(player.getPitch());
        setYaw(player.getYaw());
        this.inventorySnapshot = player.getInventory().createSnapshot();
    }

    public BPlayerSnapshot(String name, ExactPosition position) {
        super(position);
        this.name = name;
        this.inventorySnapshot = null; //TODO
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUniqueId() {
        return this.createdFrom.getUniqueId();
    }

    @Override
    public LivePlayer spawnEntity() {
        applyDefaults(this.createdFrom);
        this.createdFrom.setSneaking(this.sneaking);
        this.createdFrom.setExhaustionLevel(this.exhaustionLevel);
        this.createdFrom.setFood(this.foodLevel);
        this.createdFrom.setSaturationLevel(this.saturationLevel);
        this.inventorySnapshot.apply(this.createdFrom);
        return this.createdFrom;
    }

    @Override
    public boolean isViewingInventory() {
        return false;
    }

    @Override
    public PlayerInventory getInventory() {
        return this.inventorySnapshot;
    }

    @Override
    public int getFoodLevel() {
        return this.foodLevel;
    }

    @Override
    public double getExhaustionLevel() {
        return this.exhaustionLevel;
    }

    @Override
    public double getSaturationLevel() {
        return this.saturationLevel;
    }

    @Override
    public boolean isSneaking() {
        return this.sneaking;
    }

    @Override
    public AbstractHuman setFood(int value) throws IndexOutOfBoundsException {
        if(value > 20){
            throw new IndexOutOfBoundsException();
        }
        this.foodLevel = value;
        return this;
    }

    @Override
    public AbstractHuman setExhaustionLevel(double value) throws IndexOutOfBoundsException {
        this.exhaustionLevel = value;
        return this;
    }

    @Override
    public AbstractHuman setSaturationLevel(double value) throws IndexOutOfBoundsException {
        this.saturationLevel = value;
        return this;
    }

    @Override
    public AbstractHuman setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
        return this;
    }

    @Override
    public PlayerSnapshot createSnapshot() {
        return new BPlayerSnapshot(this);
    }

}
