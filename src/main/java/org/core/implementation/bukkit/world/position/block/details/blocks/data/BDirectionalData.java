package org.core.implementation.bukkit.world.position.block.details.blocks.data;

import org.bukkit.block.BlockFace;
import org.core.exceptions.DirectionNotSupported;
import org.core.implementation.bukkit.utils.DirectionUtils;
import org.core.world.direction.Direction;
import org.core.world.position.block.details.data.DirectionalData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class BDirectionalData implements DirectionalData {

    protected final org.bukkit.block.data.Directional data;

    public BDirectionalData(org.bukkit.block.data.Directional data) {
        this.data = data;
    }

    @Override
    public Direction getDirection() {
        return DirectionUtils.toDirection(this.data.getFacing());
    }

    @Override
    public Direction[] getSupportedDirections() {
        List<BlockFace> set = new ArrayList<>(this.data.getFaces());
        Direction[] directions = new Direction[set.size()];
        for (int A = 0; A < set.size(); A++) {
            directions[A] = DirectionUtils.toDirection(set.get(A));
        }
        return directions;
    }

    @Override
    public DirectionalData setDirection(Direction direction) throws DirectionNotSupported {
        if (Stream.of(this.getSupportedDirections()).noneMatch(d -> d.equals(direction))) {
            throw new DirectionNotSupported(direction, this.data.getAsString());
        }
        this.data.setFacing(DirectionUtils.toFace(direction));
        return this;
    }
}
