package org.core.implementation.bukkit.event;

import org.core.event.Event;
import org.core.event.EventListener;
import org.core.event.EventManager;
import org.core.event.EventPriority;
import org.core.platform.plugin.Plugin;

import java.util.*;

public class BEventManager implements EventManager {

    private final Map<Plugin, Set<EventListener>> eventListeners = new HashMap<>();
    private final BukkitListener listener = new BukkitListener();

    public BukkitListener getRawListener() {
        return this.listener;
    }

    @Override
    public <E extends Event> E callEvent(E event) {
        return BukkitListener.call(EventPriority.IGNORE, event);
    }

    @Override
    public EventManager register(Plugin plugin, EventListener... listeners) {
        Set<EventListener> listeners1 = this.eventListeners.get(plugin);
        boolean requiresPut = false;
        if (listeners1 == null) {
            requiresPut = true;
            listeners1 = new HashSet<>();
        }
        listeners1.addAll(Arrays.asList(listeners));
        if (requiresPut) {
            this.eventListeners.put(plugin, listeners1);
        } else {
            this.eventListeners.replace(plugin, listeners1);
        }
        return this;
    }

    @Override
    public Map<Plugin, Set<EventListener>> getEventListeners() {
        return this.eventListeners;
    }
}
