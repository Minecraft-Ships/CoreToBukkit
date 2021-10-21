package org.core.implementation.bukkit.entity.scene.live;

import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.core.entity.scene.itemframe.LiveItemFrame;
import org.core.exceptions.DirectionNotSupported;
import org.core.implementation.bukkit.entity.BLiveEntity;
import org.core.implementation.bukkit.utils.DirectionUtils;
import org.core.inventory.item.stack.ItemStack;
import org.core.inventory.parts.Slot;
import org.core.world.direction.Direction;
import org.core.world.direction.FourFacingDirection;
import org.core.implementation.bukkit.entity.scene.snapshot.BItemFrameSnapshot;
import org.core.implementation.bukkit.inventory.inventories.live.entity.BLiveItemFrameSlot;
import org.core.implementation.bukkit.inventory.item.stack.BAbstractItemStack;

import java.util.stream.Stream;

public class BLiveItemFrame extends BLiveEntity<ItemFrame> implements LiveItemFrame {

    @Deprecated
    public BLiveItemFrame(Entity entity){
        this((org.bukkit.entity.ItemFrame)entity);
    }

    public BLiveItemFrame(org.bukkit.entity.ItemFrame entity) {
        super(entity);
    }

    /*@Deprecated
    public BLiveItemFrame(ItemFrameSnapshot snapshot){
        super(snapshot);
    }*/

    @Override
    public void setItem(ItemStack stack) {
        this.getBukkitEntity().setItem(((BAbstractItemStack)stack).getBukkitItem());
    }

    @Override
    public Direction getItemRotation() {
        Rotation rotation = getBukkitEntity().getRotation();
        switch(rotation){
            case NONE:
            case FLIPPED:
                return FourFacingDirection.SOUTH;
            case CLOCKWISE_45:
            case FLIPPED_45:
                return FourFacingDirection.EAST;
            case CLOCKWISE:
            case COUNTER_CLOCKWISE:
                return FourFacingDirection.NORTH;
            case CLOCKWISE_135:
            case COUNTER_CLOCKWISE_45:
                return FourFacingDirection.WEST;
        }
        return null;
    }

    @Override
    public boolean getItemRotationFlip() {
        Rotation rotation = getBukkitEntity().getRotation();
        switch (rotation){
            case NONE:
            case CLOCKWISE_135:
            case CLOCKWISE:
            case CLOCKWISE_45:
                return false;
            case FLIPPED:
            case COUNTER_CLOCKWISE_45:
            case COUNTER_CLOCKWISE:
            case FLIPPED_45:
                return true;
        }
        return false;
    }

    @Override
    public BLiveItemFrame setItemRotation(Direction direction, boolean flip) throws DirectionNotSupported {
        if(!Stream.of(getDirections()).anyMatch(d -> d.equals(direction))) {
            throw new DirectionNotSupported(direction, "ItemFrame");
        }
        org.bukkit.Rotation rotation = null;
        if(direction.equals(FourFacingDirection.NORTH)){
            if (flip) {
                rotation = Rotation.FLIPPED;
            }else{
                rotation = Rotation.NONE;
            }
        }else if(direction.equals(FourFacingDirection.EAST)){
            if (flip) {
                rotation = Rotation.FLIPPED_45;
            }else{
                rotation = Rotation.CLOCKWISE_45;
            }
        }else if(direction.equals(FourFacingDirection.SOUTH)){
            if (flip) {
                rotation = Rotation.COUNTER_CLOCKWISE;
            }else{
                rotation = Rotation.CLOCKWISE;
            }
        }else if(direction.equals(FourFacingDirection.WEST)){
            if (flip) {
                rotation = Rotation.COUNTER_CLOCKWISE_45;
            }else{
                rotation = Rotation.CLOCKWISE_135;
            }
        }
        getBukkitEntity().setRotation(rotation);
        return this;
    }

    @Override
    public Slot getHoldingItem() {
        return new BLiveItemFrameSlot(this.getBukkitEntity());
    }

    @Override
    public Direction[] getDirections() {
        return FourFacingDirection.getFourFacingDirections();
    }

    @Override
    public Direction getDirection() {
        return DirectionUtils.toDirection(getBukkitEntity().getAttachedFace());
    }

    @Override
    public BLiveItemFrame setDirection(Direction direction) throws DirectionNotSupported {
        for(Direction dir : getDirections()){
            if(dir.equals(direction)){
                throw new DirectionNotSupported(direction, "ItemFrame");
            }
        }
        getBukkitEntity().setFacingDirection(DirectionUtils.toFace(direction.getOpposite()));
        return this;
    }

    @Override
    public void remove() {
        this.getBukkitEntity().setItem(null);
        this.getBukkitEntity().remove();
    }

    @Override
    public BItemFrameSnapshot createSnapshot() {
        return new BItemFrameSnapshot(this);
    }
}
