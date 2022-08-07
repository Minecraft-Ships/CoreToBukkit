package org.core.implementation.bukkit.inventory.part.dispenser;

import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Grid3x3;
import org.core.inventory.parts.Slot;
import org.core.inventory.parts.snapshot.Grid3x3Snapshot;

import java.util.*;

public abstract class DispenserBasedGrid implements Grid3x3 {

    protected final List<DispenserSlot> slots = new ArrayList<>();

    public DispenserBasedGrid() {
        for (int A = 0; A < 9; A++) {
            this.slots.add(new DispenserSlot(A));
        }
    }

    protected abstract org.bukkit.block.Container getContainer();

    @Override
    public Set<Slot> getSlots() {
        return new HashSet<>(this.slots);
    }

    @Override
    public Grid3x3Snapshot createSnapshot() {
        return new Grid3x3Snapshot(this);
    }

    public class DispenserSlot implements Slot {

        private final int position;

        public DispenserSlot(int pos) {
            this.position = pos;
        }

        @Override
        public Optional<Integer> getPosition() {
            return Optional.of(this.position);
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack is = DispenserBasedGrid.this
                    .getContainer()
                    .getInventory()
                    .getItem(this.position);
            if (is == null) {
                return Optional.empty();
            }
            ItemStack stack = new BLiveItemStack(is);
            return Optional.of(stack);
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.block.Container container = DispenserBasedGrid.this.getContainer();
            if (stack == null) {
                container.getSnapshotInventory().setItem(this.position, null);
                container.update();
                return this;
            }
            container.getSnapshotInventory().setItem(this.position, ((BAbstractItemStack) stack).getBukkitItem());
            container.update();
            return this;
        }
    }
}
