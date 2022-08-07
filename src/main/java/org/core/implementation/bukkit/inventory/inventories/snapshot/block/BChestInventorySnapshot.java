package org.core.implementation.bukkit.inventory.inventories.snapshot.block;

import org.core.inventory.inventories.general.block.ChestInventory;
import org.core.inventory.inventories.snapshots.block.ChestInventorySnapshot;
import org.core.inventory.parts.Slot;
import org.core.inventory.parts.snapshot.SlotSnapshot;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BChestInventorySnapshot extends ChestInventorySnapshot {

    protected final Set<SlotSnapshot> slots = new HashSet<>();

    public BChestInventorySnapshot() {

    }

    public BChestInventorySnapshot(ChestInventory inventory) {
        this.position = inventory.getPosition();
        this.slots.addAll(inventory.getSlots().stream().map(Slot::createSnapshot).collect(Collectors.toSet()));
    }

    @Override
    public Optional<Slot> getSlot(int slotPos) {
        return this
                .getSlots()
                .stream()
                .filter(s -> s.getPosition().isPresent())
                .filter(s -> s.getPosition().get() == slotPos)
                .findAny();
    }

    @Override
    public Set<Slot> getSlots() {
        return new HashSet<>(this.slots);
    }

    @Override
    public ChestInventorySnapshot createSnapshot() {
        return new BChestInventorySnapshot(this);
    }
}
