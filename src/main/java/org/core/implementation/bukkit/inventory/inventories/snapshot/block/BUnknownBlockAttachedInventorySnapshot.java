package org.core.implementation.bukkit.inventory.inventories.snapshot.block;

import org.core.inventory.inventories.general.block.UnknownBlockAttachedInventory;
import org.core.inventory.inventories.live.block.LiveUnknownBlockAttachedInventory;
import org.core.inventory.inventories.snapshots.block.UnknownBlockAttachedInventorySnapshot;
import org.core.inventory.parts.Slot;
import org.core.inventory.parts.snapshot.SlotSnapshot;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.impl.sync.SyncBlockPosition;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BUnknownBlockAttachedInventorySnapshot implements UnknownBlockAttachedInventorySnapshot {

    protected final Set<SlotSnapshot> slots = new HashSet<>();
    protected final BlockType[] types;
    protected final SyncBlockPosition position;

    public BUnknownBlockAttachedInventorySnapshot(SyncBlockPosition position, BlockType... types) {
        this.types = types;
        this.position = position;
    }

    public BUnknownBlockAttachedInventorySnapshot(UnknownBlockAttachedInventory inv) {
        this.types = inv.getAllowedBlockType();
        this.position = inv.getPosition();
        inv.getSlots().forEach(i -> BUnknownBlockAttachedInventorySnapshot.this.slots.add(i.createSnapshot()));
    }

    @Override
    public BlockType[] getAllowedBlockType() {
        return this.types;
    }

    @Override
    public SyncBlockPosition getPosition() {
        return this.position;
    }

    @Override
    public Set<Slot> getSlots() {
        return new HashSet<>(this.slots);
    }

    @Override
    public Optional<Slot> getSlot(int slotPos) {
        return this
                .getSlots()
                .stream()
                .filter(s -> s.getPosition().isPresent())
                .filter(s -> s.getPosition().get() == slotPos)
                .findFirst();
    }

    @Override
    public UnknownBlockAttachedInventorySnapshot createSnapshot() {
        return new BUnknownBlockAttachedInventorySnapshot(this);
    }

    @Override
    public void apply() {
        Optional<LiveTileEntity> opTile = this.getPosition().getTileEntity();
        if (!opTile.isPresent()) {
            return;
        }
        LiveTileEntity lte = opTile.get();
        if (!(lte instanceof LiveUnknownBlockAttachedInventory)) {
            return;
        }
        UnknownBlockAttachedInventory lubai = (UnknownBlockAttachedInventory) lte;
        this.apply(lubai);
    }

    @Override
    public void apply(UnknownBlockAttachedInventory ubai) {
        boolean carryOn = false;
        for (BlockType type1 : this.types) {
            for (BlockType type2 : ubai.getAllowedBlockType()) {
                if (type1.getId().equals(type2.getId())) {
                    carryOn = true;
                    break;
                }
            }
            if (carryOn) {
                break;
            }
        }
        if (!carryOn) {
            return;
        }
        for (SlotSnapshot slot : this.slots) {
            slot.getItem().ifPresent(f -> ubai.getSlot(slot.getPosition().get()).get().setItem(f));
        }
    }
}
