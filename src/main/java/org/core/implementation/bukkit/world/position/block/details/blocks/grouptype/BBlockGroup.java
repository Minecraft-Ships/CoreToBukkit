package org.core.implementation.bukkit.world.position.block.details.blocks.grouptype;

import org.core.world.position.block.BlockType;
import org.core.world.position.block.grouptype.BlockGroup;

public class BBlockGroup extends BlockGroup {

    public BBlockGroup(String name, BlockType... types) {
        super("minecraft:" + name.toLowerCase(), name, types);
    }

}
