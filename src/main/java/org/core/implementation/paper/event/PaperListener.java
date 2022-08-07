package org.core.implementation.paper.event;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.core.event.EventPriority;
import org.core.implementation.bukkit.event.BukkitListener;
import org.core.implementation.bukkit.world.position.block.details.blocks.BBlockDetails;
import org.core.implementation.bukkit.world.position.impl.sync.BBlockPosition;
import org.core.implementation.paper.event.events.block.PBlockChangeEvent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.impl.sync.SyncBlockPosition;

public class PaperListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockDestroyEvent event) {
        SyncBlockPosition position = new BBlockPosition(event.getBlock());
        BlockDetails details = new BBlockDetails(event.getNewState(), false);
        PBlockChangeEvent.BlockBreakEvent event2 = new PBlockChangeEvent.BlockBreakEvent(position,
                position.getBlockDetails(), details);
        BukkitListener.call(EventPriority.IGNORE, event2);
        event.setCancelled(event2.isCancelled());
    }
}
