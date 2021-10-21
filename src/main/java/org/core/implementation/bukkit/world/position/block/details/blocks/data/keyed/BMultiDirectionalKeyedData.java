package org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed;

import org.core.implementation.bukkit.utils.DirectionUtils;
import org.core.world.direction.Direction;
import org.core.world.position.block.details.data.keyed.MultiDirectionalKeyedData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

public class BMultiDirectionalKeyedData implements MultiDirectionalKeyedData {

    protected org.bukkit.block.data.MultipleFacing data;

    public BMultiDirectionalKeyedData(org.bukkit.block.data.MultipleFacing data) {
        this.data = data;
    }

    @Override
    public Optional<Collection<Direction>> getData() {
        Collection<Direction> collection = new HashSet<>();
        this.data.getFaces().forEach(d -> collection.add(DirectionUtils.toDirection(d)));
        return Optional.of(Collections.unmodifiableCollection(collection));
    }

    @Override
    public void setData(Collection<Direction> value) {
        this.data.getAllowedFaces().forEach(d -> {
            Direction dir = DirectionUtils.toDirection(d);
            if(value.stream().anyMatch(direction -> direction.equals(dir))){
                this.data.setFace(d, true);
            }else{
                this.data.setFace(d, false);
            }
        });
    }

    @Override
    public Collection<Direction> getSupportedDirections() {
        Collection<Direction> collection = new HashSet<>();
        this.data.getAllowedFaces().forEach(d -> collection.add(DirectionUtils.toDirection(d)));
        return Collections.unmodifiableCollection(collection);
    }
}
