package de.tubeof.tubetils.api.cache.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CacheContainerAddEvent extends Event {

    private final HandlerList handlers = new HandlerList();

    public CacheContainerAddEvent() {}

    @Override
    public HandlerList getHandlers() {
        return null;
    }

}