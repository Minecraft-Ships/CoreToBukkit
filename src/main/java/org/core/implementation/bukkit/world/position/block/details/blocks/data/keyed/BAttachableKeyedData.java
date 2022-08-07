package org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed;

import org.bukkit.block.data.BlockData;
import org.core.TranslateCore;
import org.core.implementation.bukkit.utils.DirectionUtils;
import org.core.implementation.bukkit.world.position.block.details.blocks.IBBlockDetails;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.attachableworkarounds.AttachableWorkAround1D14;
import org.core.implementation.bukkit.world.position.block.details.blocks.data.keyed.attachableworkarounds.CommonAttachableWorkAround;
import org.core.platform.plugin.details.CorePluginVersion;
import org.core.world.direction.Direction;
import org.core.world.position.block.BlockType;
import org.core.world.position.block.details.data.keyed.AttachableKeyedData;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class BAttachableKeyedData implements AttachableKeyedData {

    public static final Collection<AttachableBlockWorkAround> workArounds = new HashSet<>();

    static {
        workArounds.addAll(Arrays.asList(CommonAttachableWorkAround.values()));
        CorePluginVersion mcVersion = TranslateCore.getPlatform().getMinecraftVersion();
        if (mcVersion.getMinor() >= 14) {
            workArounds.addAll(Arrays.asList(AttachableWorkAround1D14.values()));
        }
    }

    private final IBBlockDetails details;
    private final AttachableBlockWorkAround workAround;
    private BAttachableKeyedData(IBBlockDetails details, AttachableBlockWorkAround workAround) {
        this.details = details;
        this.workAround = workAround;
    }

    public static Optional<BAttachableKeyedData> getKeyedData(IBBlockDetails details) {
        Optional<AttachableBlockWorkAround> opWork = workArounds
                .stream()
                .filter(w -> w.getTypes().stream().anyMatch(b -> details.getType().equals(b)))
                .findAny();
        return opWork.map(attachableBlockWorkAround -> new BAttachableKeyedData(details, attachableBlockWorkAround));
    }

    @Override
    public Optional<Direction> getData() {
        return Optional.of(this.workAround.getAttachedDirection(this.details.getBukkitData()));
    }

    @Override
    public void setData(Direction value) {
        this.details.setBukkitData(this.workAround.setAttachedDirection(this.details.getBukkitData(), value));
    }

    public interface AttachableBlockWorkAround {

        Function<BlockData, Direction> GET_DIRECTION_FROM_BLOCK_DATA = b -> DirectionUtils
                .toDirection(((org.bukkit.block.data.Directional) b).getFacing())
                .getOpposite();
        Consumer<Map.Entry<BlockData, Direction>> SET_BLOCK_DATA_FROM_DIRECTION =
                e -> ((org.bukkit.block.data.Directional) e.getKey()).setFacing(
                DirectionUtils.toFace(e.getValue().getOpposite()));

        Collection<BlockType> getTypes();

        Direction getAttachedDirection(BlockData data);

        org.bukkit.block.data.BlockData setAttachedDirection(org.bukkit.block.data.BlockData data, Direction direction);
    }
}
