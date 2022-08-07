package org.core.implementation.bukkit.inventory.inventories.live.entity;

import org.bukkit.Material;
import org.core.entity.living.human.player.LivePlayer;
import org.core.implementation.bukkit.entity.living.human.player.live.BLivePlayer;
import org.core.implementation.bukkit.inventory.inventories.snapshot.entity.BPlayerInventorySnapshot;
import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;
import org.core.implementation.bukkit.inventory.item.stack.BLiveItemStack;
import org.core.inventory.inventories.live.entity.LivePlayerInventory;
import org.core.inventory.inventories.snapshots.entity.PlayerInventorySnapshot;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class BLivePlayerInventory implements LivePlayerInventory {

    protected final BLivePlayer player;
    protected final BLivePlayerInventory.PlayerItemSlot offHandSlot;
    protected final BLivePlayerInventory.PlayerArmorSlots armorSlots;
    protected final BLivePlayerInventory.PlayerHotbar hotbar;
    protected final BLivePlayerInventory.PlayerCraftingSlots craftingSlots;
    protected final BLivePlayerInventory.PlayerMainInventory main;
    public BLivePlayerInventory(BLivePlayer player) {
        this.player = player;
        this.hotbar = new BLivePlayerInventory.PlayerHotbar();
        this.armorSlots = new BLivePlayerInventory.PlayerArmorSlots();
        this.offHandSlot = new BLivePlayerInventory.PlayerItemSlot(40);
        this.craftingSlots = new BLivePlayerInventory.PlayerCraftingSlots();
        this.main = new BLivePlayerInventory.PlayerMainInventory();
    }

    @Override
    public ArmorPart getArmor() {
        return this.armorSlots;
    }

    @Override
    public Slot getOffHoldingItem() {
        return this.offHandSlot;
    }

    @Override
    public Hotbar getHotbar() {
        return this.hotbar;
    }

    @Override
    public Grid2x2 getCraftingGrid() {
        return this.craftingSlots;
    }

    @Override
    public MainPlayerInventory getMainInventory() {
        return this.main;
    }

    @Override
    public PlayerInventorySnapshot createSnapshot() {
        return new BPlayerInventorySnapshot(this);
    }

    @Override
    public Optional<LivePlayer> getAttachedEntity() {
        return Optional.of(this.player);
    }

    private class PlayerCraftingSlots implements Grid2x2 {

        final BLivePlayerInventory.PlayerItemSlot slot1 = new BLivePlayerInventory.PlayerItemSlot(80);
        final BLivePlayerInventory.PlayerItemSlot slot2 = new BLivePlayerInventory.PlayerItemSlot(81);
        final BLivePlayerInventory.PlayerItemSlot slot3 = new BLivePlayerInventory.PlayerItemSlot(82);
        final BLivePlayerInventory.PlayerItemSlot slot4 = new BLivePlayerInventory.PlayerItemSlot(83);

        @Override
        public Set<Slot> getSlots() {
            return new HashSet<>(Arrays.asList(this.slot1, this.slot2, this.slot3, this.slot4));
        }
    }

    private class PlayerArmorSlots implements ArmorPart {

        final BLivePlayerInventory.PlayerItemSlot helmetSlot = new BLivePlayerInventory.PlayerItemSlot(103);
        final BLivePlayerInventory.PlayerItemSlot armorSlot = new BLivePlayerInventory.PlayerItemSlot(102);
        final BLivePlayerInventory.PlayerItemSlot leggingsSlot = new BLivePlayerInventory.PlayerItemSlot(101);
        final BLivePlayerInventory.PlayerItemSlot shoeSlot = new BLivePlayerInventory.PlayerItemSlot(100);

        @Override
        public Slot getHelmetSlot() {
            return this.helmetSlot;
        }

        @Override
        public Slot getArmorSlot() {
            return this.armorSlot;
        }

        @Override
        public Slot getLeggingsSlot() {
            return this.leggingsSlot;
        }

        @Override
        public Slot getShoesSlot() {
            return this.shoeSlot;
        }
    }

    private class PlayerMainInventory implements MainPlayerInventory {

        final Set<Slot> slots = new HashSet<>();

        private PlayerMainInventory() {
            for (int A = 9; A < 35; A++) {
                this.slots.add(new BLivePlayerInventory.PlayerItemSlot(A));
            }
        }

        @Override
        public Set<Slot> getSlots() {
            return this.slots;
        }
    }

    private class PlayerHotbar implements Hotbar {

        final Set<Slot> slots = new HashSet<>();

        private PlayerHotbar() {
            for (int A = 0; A < 9; A++) {
                this.slots.add(new BLivePlayerInventory.PlayerItemSlot(A));
            }
        }

        @Override
        public int getSelectedSlotPos() {
            return BLivePlayerInventory.this.player.getBukkitEntity().getInventory().getHeldItemSlot();
        }

        @Override
        public Set<Slot> getSlots() {
            return this.slots;
        }
    }

    private class PlayerItemSlot implements Slot {

        final int position;

        private PlayerItemSlot(int slot) {
            this.position = slot;
        }

        @Override
        public Optional<Integer> getPosition() {
            return Optional.of(this.position);
        }

        @Override
        public Optional<ItemStack> getItem() {
            org.bukkit.inventory.ItemStack stack = BLivePlayerInventory.this.player
                    .getBukkitEntity()
                    .getInventory()
                    .getItem(this.position);
            if (stack == null || stack.getType() == Material.VOID_AIR) {
                return Optional.empty();
            }
            return Optional.of(new BLiveItemStack(stack));
        }

        @Override
        public Slot setItem(ItemStack stack) {
            org.bukkit.inventory.ItemStack bstack = ((BAbstractItemStack) stack).getBukkitItem();
            BLivePlayerInventory.this.player.getBukkitEntity().getInventory().setItem(this.position, bstack);
            return this;
        }

    }

}
