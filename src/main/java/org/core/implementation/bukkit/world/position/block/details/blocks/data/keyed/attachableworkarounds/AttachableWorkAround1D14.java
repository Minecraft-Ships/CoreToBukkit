package org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.attachableworkarounds;

import org.bukkit.block.data.BlockData;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.BAttachableKeyedData;
import org.core.world.direction.Direction;
import org.core.world.direction.FourFacingDirection;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.blocktypes.post.BlockTypes1V14;

import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public enum AttachableWorkAround1D14 implements BAttachableKeyedData.AttachableBlockWorkAround {

    LANTERN(d -> {
        try {
            return ((boolean) d
                    .getClass()
                    .getMethod("isHanging")
                    .invoke(d)) ? FourFacingDirection.UP : FourFacingDirection.DOWN;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }, e -> {
        boolean hanging = !e.getValue().equals(FourFacingDirection.DOWN);
        try {
            e.getKey().getClass().getMethod("setHanging", boolean.class).invoke(e.getKey(), hanging);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            ex.printStackTrace();
        }
    }, BlockTypes1V14.LANTERN);

    private final Collection<BlockType> collection;
    private final Function<? super BlockData, ? extends Direction> functionDirection;
    private final Consumer<? super Map.Entry<BlockData, Direction>> consumer;

    AttachableWorkAround1D14(Function<? super BlockData, ? extends Direction> getDir,
            Consumer<? super Map.Entry<BlockData, Direction>> setDir, BlockType... types) {
        this(getDir, setDir, Arrays.asList(types));
    }

    AttachableWorkAround1D14(Function<? super BlockData, ? extends Direction> getDir,
            Consumer<? super Map.Entry<BlockData, Direction>> setDir, Collection<BlockType> blockTypes) {
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
        this.consumer.accept(new AbstractMap.SimpleImmutableEntry<>(data, direction));
        return data;
    }
}
