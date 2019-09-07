package org.ships.implementation.bukkit.entity.scene.snapshot;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.EntityTypes;
import org.core.entity.scene.itemframe.ItemFrame;
import org.core.entity.scene.itemframe.ItemFrameSnapshot;
import org.core.entity.scene.itemframe.LiveItemFrame;
import org.core.exceptions.DirectionNotSupported;
import org.core.inventory.parts.Slot;
import org.core.inventory.parts.snapshot.SlotSnapshot;
import org.core.world.direction.Direction;
import org.core.world.direction.FourFacingDirection;
import org.core.world.position.ExactPosition;
import org.ships.implementation.bukkit.entity.BEntitySnapshot;
import org.ships.implementation.bukkit.entity.scene.live.BLiveItemFrame;
import org.ships.implementation.bukkit.world.position.BExactPosition;

import java.util.stream.Stream;

public class BItemFrameSnapshot extends BEntitySnapshot<LiveItemFrame> implements ItemFrameSnapshot {

    private Direction itemRotation;
    private boolean flip;
    private SlotSnapshot slot = new SlotSnapshot(0, null);
    private Direction direction;

    public BItemFrameSnapshot(LiveItemFrame frame){
        super(frame);
        this.itemRotation = frame.getItemRotation();
        this.flip = frame.getItemRotationFlip();
        this.slot = frame.getHoldingItem().createSnapshot();
        this.direction = frame.getDirection();
    }

    public BItemFrameSnapshot(ItemFrameSnapshot frame){
        super(frame);
        this.itemRotation = frame.getItemRotation();
        this.flip = frame.getItemRotationFlip();
        this.slot = frame.getHoldingItem().createSnapshot();
        this.direction = frame.getDirection();
    }

    public BItemFrameSnapshot(ExactPosition position) {
        super(position);
    }

    @Override
    public LiveItemFrame spawnEntity() {
        org.bukkit.Location loc = ((BExactPosition)this.position).getBukkitLocation();
        loc.setPitch((float)this.pitch);
        loc.setYaw((float)this.yaw);
        org.bukkit.entity.ItemFrame frame = loc.getWorld().spawn(loc, org.bukkit.entity.ItemFrame.class);
        BLiveItemFrame itemFrame = new BLiveItemFrame(frame);
        try {
            itemFrame.setItemRotation(this.itemRotation, this.flip);
        } catch (DirectionNotSupported directionNotSupported) {
            directionNotSupported.printStackTrace();
        }
        return itemFrame;
    }

    @Override
    public EntityType<LiveItemFrame, ItemFrameSnapshot> getType() {
        return EntityTypes.ITEM_FRAME;
    }

    @Override
    public EntitySnapshot<LiveItemFrame> createSnapshot() {
        return new BItemFrameSnapshot(this);
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
    public ItemFrame setItemRotation(Direction direction, boolean flip) {
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
        if(!Stream.of(getDirections()).anyMatch(d -> d.getName().equals(direction.getName()))){
            throw new DirectionNotSupported(direction, "ItemFrameSnapshot");
        }
        this.direction = direction;
        return this;
    }
}
