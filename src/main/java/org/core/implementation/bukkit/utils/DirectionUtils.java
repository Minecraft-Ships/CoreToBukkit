package org.core.implementation.bukkit.utils;

import org.bukkit.block.BlockFace;
import org.core.world.direction.Direction;
import org.core.world.direction.SixteenFacingDirection;
import org.jetbrains.annotations.NotNull;

public final class DirectionUtils {

    private DirectionUtils() {
        throw new RuntimeException("Should not be constructed");
    }

    public static @NotNull org.bukkit.block.BlockFace toFace(Direction direction) {
        if (direction.equals(SixteenFacingDirection.NORTH)) {
            return BlockFace.NORTH;
        } else if (direction.equals(SixteenFacingDirection.EAST)) {
            return BlockFace.EAST;
        } else if (direction.equals(SixteenFacingDirection.SOUTH)) {
            return BlockFace.SOUTH;
        } else if (direction.equals(SixteenFacingDirection.WEST)) {
            return BlockFace.WEST;
        } else if (direction.equals(SixteenFacingDirection.NORTH_EAST)) {
            return BlockFace.NORTH_EAST;
        } else if (direction.equals(SixteenFacingDirection.EAST_NORTH_EAST)) {
            return BlockFace.EAST_NORTH_EAST;
        } else if (direction.equals(SixteenFacingDirection.EAST_SOUTH_EAST)) {
            return BlockFace.EAST_SOUTH_EAST;
        } else if (direction.equals(SixteenFacingDirection.NORTH_NORTH_EAST)) {
            return BlockFace.NORTH_NORTH_EAST;
        } else if (direction.equals(SixteenFacingDirection.NORTH_WEST)) {
            return BlockFace.NORTH_WEST;
        } else if (direction.equals(SixteenFacingDirection.SOUTH_EAST)) {
            return BlockFace.SOUTH_EAST;
        } else if (direction.equals(SixteenFacingDirection.SOUTH_WEST)) {
            return BlockFace.SOUTH_WEST;
        } else if (direction.equals(SixteenFacingDirection.WEST_NORTH_WEST)) {
            return BlockFace.WEST_NORTH_WEST;
        } else if (direction.equals(SixteenFacingDirection.WEST_SOUTH_WEST)) {
            return BlockFace.WEST_SOUTH_WEST;
        } else if (direction.equals(SixteenFacingDirection.SOUTH_SOUTH_WEST)) {
            return BlockFace.SOUTH_SOUTH_WEST;
        } else if (direction.equals(SixteenFacingDirection.SOUTH_SOUTH_EAST)) {
            return BlockFace.SOUTH_SOUTH_EAST;
        } else if (direction.equals(SixteenFacingDirection.UP)) {
            return BlockFace.UP;
        } else if (direction.equals(SixteenFacingDirection.DOWN)) {
            return BlockFace.DOWN;
        }
        throw new RuntimeException("Unknown convertion for direction " + direction.getName());
    }

    public static @NotNull Direction toDirection(org.bukkit.block.BlockFace face) {
        switch (face) {
            case NORTH:
                return SixteenFacingDirection.NORTH;
            case EAST:
                return SixteenFacingDirection.EAST;
            case SOUTH:
                return SixteenFacingDirection.SOUTH;
            case WEST:
                return SixteenFacingDirection.WEST;
            case UP:
                return SixteenFacingDirection.UP;
            case DOWN:
                return SixteenFacingDirection.DOWN;
            case NORTH_EAST:
                return SixteenFacingDirection.NORTH_EAST;
            case NORTH_WEST:
                return SixteenFacingDirection.NORTH_WEST;
            case SOUTH_EAST:
                return SixteenFacingDirection.SOUTH_EAST;
            case SOUTH_WEST:
                return SixteenFacingDirection.SOUTH_WEST;
            case WEST_NORTH_WEST:
                return SixteenFacingDirection.WEST_NORTH_WEST;
            case NORTH_NORTH_WEST:
                return SixteenFacingDirection.NORTH_NORTH_WEST;
            case NORTH_NORTH_EAST:
                return SixteenFacingDirection.NORTH_NORTH_EAST;
            case EAST_NORTH_EAST:
                return SixteenFacingDirection.EAST_NORTH_EAST;
            case EAST_SOUTH_EAST:
                return SixteenFacingDirection.EAST_SOUTH_EAST;
            case SOUTH_SOUTH_EAST:
                return SixteenFacingDirection.SOUTH_SOUTH_EAST;
            case SOUTH_SOUTH_WEST:
                return SixteenFacingDirection.SOUTH_SOUTH_WEST;
            case WEST_SOUTH_WEST:
                return SixteenFacingDirection.WEST_SOUTH_WEST;
            case SELF:
                throw new IllegalArgumentException("SELF is not a direction");
        }
        throw new RuntimeException("Unknown conversion for block face of " + face.name());
    }
}
