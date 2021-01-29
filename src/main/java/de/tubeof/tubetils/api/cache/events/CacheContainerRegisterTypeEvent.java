package de.tubeof.tubetils.api.cache.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CacheContainerRegisterTypeEvent extends Event {

    private HandlerList handlers = new HandlerList();
    private Class typeClass;

    public CacheContainerRegisterTypeEvent(Class typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public Class getTypeClass() {
        return this.typeClass;
    }

}