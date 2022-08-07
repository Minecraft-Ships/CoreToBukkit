package org.core.implementation.bukkit.entity.scene.snapshot;

import org.bukkit.entity.Entity;
import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.scene.itemframe.ItemFrameSnapshot;
import org.core.entity.scene.itemframe.LiveItemFrame;
import org.core.exceptions.DirectionNotSupported;
import org.core.implementation.bukkit.entity.BEntitySnapshot;
import org.core.implementation.bukkit.entity.scene.live.BLiveItemFrame;
import org.core.implementation.bukkit.world.position.impl.BAbstractPosition;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;
import org.core.inventory.parts.snapshot.SlotSnapshot;
import org.core.world.direction.Direction;
import org.core.world.direction.FourFacingDirection;
import org.core.world.position.impl.sync.SyncExactPosition;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public class BItemFrameSnapshot extends BEntitySnapshot<LiveItemFrame> implements ItemFrameSnapshot {

    private Direction itemRotation;
    private boolean flip;
    private SlotSnapshot slot = new SlotSnapshot(0, null);
    private Direction direction;

    public BItemFrameSnapshot(LiveItemFrame frame) {
        super(frame);
        this.itemRotation = frame.getItemRotation();
        this.flip = frame.getItemRotationFlip();
        this.slot = frame.getHoldingItem().createSnapshot();
        this.direction = frame.getDirection();
    }

    public BItemFrameSnapshot(ItemFrameSnapshot frame) {
        super(frame);
        this.itemRotation = frame.getItemRotation();
        this.flip = frame.getItemRotationFlip();
        this.slot = frame.getHoldingItem().createSnapshot();
        this.direction = frame.getDirection();
    }

    public BItemFrameSnapshot(SyncExactPosition position) {
        super(position);
    }

    @Override
    public LiveItemFrame spawnEntity() {
        org.bukkit.Location loc = ((BAbstractPosition<Double>) this.getPosition()).toBukkitLocation();
        loc.setPitch((float) this.pitch);
        loc.setYaw((float) this.yaw);
        org.bukkit.entity.ItemFrame frame;
        try {
            frame = loc.getWorld().spawn(loc, org.bukkit.entity.ItemFrame.class);
        } catch (IllegalArgumentException e) {
            Collection<Entity> entities = loc.getWorld().getNearbyEntities(loc, 1, 1, 1);
            if (entities.isEmpty()) {
                throw e;
            }
            Optional<Entity> opItem = entities
                    .stream()
                    .filter(le -> le instanceof org.bukkit.entity.ItemFrame)
                    .findAny();
            if (opItem.isPresent()) {
                frame = (org.bukkit.entity.ItemFrame) opItem.get();
            } else {
                throw e;
            }
        }
        BLiveItemFrame itemFrame = new BLiveItemFrame(frame);
        try {
            this.slot.getItem().ifPresent(itemFrame::setItem);
            itemFrame.setItemRotation(this.itemRotation, this.flip);
        } catch (DirectionNotSupported directionNotSupported) {
            directionNotSupported.printStackTrace();
        }
        return itemFrame;
    }

    @Override
    public EntityType<LiveItemFrame, ItemFrameSnapshot> getType() {
        return EntityTypes.ITEM_FRAME.get();
    }

    @Override
    public EntitySnapshot<LiveItemFrame> createSnapshot() {
        return new BItemFrameSnapshot(this);
    }

    @Override
    public void setItem(ItemStack stack) {
        this.slot.setItem(stack);
    }

    @Override
    public Direction getItemRotation() {
        return this.itemRotation;
    }

    @Override
    public boolean getItemRotationFlip() {
        return this.flip;
    }

    @Override
    public BItemFrameSnapshot setItemRotation(Direction direction, boolean flip) {
        this.itemRotation = direction;
        this.flip = flip;
        return this;
    }

    @Override
    public Slot getHoldingItem() {
        return this.slot;
    }

    @Override
    public Direction[] getDirections() {
        return FourFacingDirection.getFourFacingDirections();
    }

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public BItemFrameSnapshot setDirection(Direction direction) throws DirectionNotSupported {
        if (Stream.of(this.getDirections()).noneMatch(d -> d.getName().equals(direction.getName()))) {
            throw new DirectionNotSupported(direction, "ItemFrameSnapshot");
        }
        this.direction = direction;
        return this;
    }
}
