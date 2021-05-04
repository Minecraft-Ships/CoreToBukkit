package org.ships.implementation.bukkit.world.position.block.details.blocks.data.keyed.attachableworkarounds;

import org.bukkit.block.data.BlockData;
import org.core.world.direction.Direction;
import org.core.world.direction.FourFacingDirection;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.BlockTypes;
import org.core.world.position.block.blocktypes.post.BlockTypes1V13;
import org.core.world.position.block.grouptype.versions.BlockGroups1V13;
import org.ships.implementation.bukkit.world.position.block.details.blocks.BBlockDetails;
import org.ships.implementation.bukkit.world.position.block.details.blocks.data.keyed.BAttachableKeyedData;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum CommonAttachableWorkAround implements BAttachableKeyedData.AttachableBlockWorkAround {

    CARPET(b -> FourFacingDirection.DOWN, e -> {}, BlockTypes1V13.BLACK_CARPET.getLike()),
    FIRE(b -> FourFacingDirection.DOWN, e -> {}, BlockTypes.FIRE),
    REDSTONE(b -> FourFacingDirection.DOWN, e -> {}, BlockTypes.REDSTONE_WIRE),
    REPEATER(b -> FourFacingDirection.DOWN, e -> {}, BlockTypes1V13.REPEATER),
    SNOW(b -> FourFacingDirection.DOWN, e -> {}, BlockTypes.SNOW),
    RAIL(b -> FourFacingDirection.DOWN,
            e -> {}, BlockTypes.RAIL,
            BlockTypes.ACTIVATOR_RAIL,
            BlockTypes.DETECTOR_RAIL,
            BlockTypes1V13.POWERED_RAIL),
    STANDING_TORCH(b -> FourFacingDirection.DOWN, e -> {},
            BlockGroups1V13.STANDING_TORCH.getGrouped()),
    LADDER(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOOCK_DATA,
            BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION,
            BlockTypes.LADDER),
    LEVER(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOOCK_DATA,
            BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION,
            BlockTypes.LEVER),
    WALL_TORCH(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOOCK_DATA,
            BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION,
            BlockGroups1V13.WALL_TORCH.getGrouped()),
    BUTTON(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOOCK_DATA,
            BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION,
            BlockGroups1V13.BUTTON.getGrouped()),
    PISTON(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOOCK_DATA,
            BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION,
            BlockTypes.MOVING_PISTON, BlockTypes.PISTON_HEAD),
    SIGN(BAttachableKeyedData.AttachableBlockWorkAround.GET_DIRECTION_FROM_BLOOCK_DATA,
            BAttachableKeyedData.AttachableBlockWorkAround.SET_BLOCK_DATA_FROM_DIRECTION,
            BlockTypes.OAK_WALL_SIGN.getLike().stream()
                    .filter(st -> ((BBlockDetails)st.getDefaultBlockDetails()).getBukkitData() instanceof org.bukkit.block.data.Directional)
                    .collect(Collectors.toSet()));

    private Collection<BlockType> collection;
    private Function<BlockData, Direction> functionDirection;
    private Consumer<Map.Entry<BlockData, Direction>> consumer;

    CommonAttachableWorkAround(Function<BlockData, Direction> getDir, Consumer<Map.Entry<BlockData, Direction>> setDir, BlockType... types){
        this(getDir, setDir, Arrays.asList(types));
    }

    CommonAttachableWorkAround(Function<BlockData, Direction> getDir, Consumer<Map.Entry<BlockData, Direction>> setDir, Collection<BlockType> blockTypes){
        this.consumer = setDir;
        this.functionDirection = getDir;
        this.collection = blockTypes;
    }

    @Override
    public Collection<BlockType> getTypes() {
        return this.collection;
    }

    @Override
    public Direction getAttachedDirection(BlockData data) {
        return this.functionDirection.apply(data);
    }

    @Override
    public BlockData setAttachedDirection(BlockData data, Direction direction) {
        this.consumer.accept(new Map.Entry(){

            @Override
            public Object getKey() {
                return data;
            }

            @Override
            public Object getValue() {
                return direction;
            }

            @Deprecated
            @Override
            public Object setValue(Object o) {
                return o;
            }
        });
        return data;
    }
}
