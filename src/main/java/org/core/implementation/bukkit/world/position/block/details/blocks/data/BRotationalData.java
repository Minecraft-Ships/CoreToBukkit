package org.core.implementation.bukkit.world.position.block.details.blocks.data;

import org.core.implementation.bukkit.utils.DirectionUtils;
import org.core.world.direction.Direction;
import org.core.world.direction.SixteenFacingDirection;
import org.core.world.position.block.details.data.DirectionalData;

public class BRotationalData implements DirectionalData {

    final org.bukkit.block.data.Rotatable rotatable;

    public BRotationalData(org.bukkit.block.data.Rotatable rotatable) {
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
    public DirectionalData setDirection(Direction direction) {
        this.rotatable.setRotation(DirectionUtils.toFace(direction));
        return this;
    }
}
