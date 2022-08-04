package de.tubeof.tubetils.api.cache.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CacheContainerGetEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public CacheContainerGetEvent() {}

    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}