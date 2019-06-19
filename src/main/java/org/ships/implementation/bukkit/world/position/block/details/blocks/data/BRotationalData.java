package org.ships.implementation.bukkit.world.position.block.details.blocks.data;

import org.core.exceptions.DirectionNotSupported;
import org.core.world.direction.Direction;
import org.core.world.direction.SixteenFacingDirection;
import org.core.world.position.block.details.data.DirectionalData;
import org.ships.implementation.bukkit.utils.DirectionUtils;

public class BRotationalData implements DirectionalData {

    org.bukkit.block.data.Rotatable rotatable;

    public BRotationalData(org.bukkit.block.data.Rotatable rotatable){
        this.rotatable = rotatable;
    }

    @Override
    public Direction getDirection() {
        return DirectionUtils.toDirection(this.rotatable.getRotation());
    }

    @Override
    public Direction[] getSupportedDirections() {
        return SixteenFacingDirection.getSixteenFacingDirections();
    }

    @Override
    public DirectionalData setDirection(Direction direction) throws DirectionNotSupported {
        this.rotatable.setRotation(DirectionUtils.toFace(direction));
        return this;
    }
}
