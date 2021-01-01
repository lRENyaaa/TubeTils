package de.tubeof.tubetils.api.title.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TitleClearEvent extends Event {

    private final HandlerList handlers = new HandlerList();
    private final Player player;
    private Boolean cancelled;

    public TitleClearEvent(Player player) {
        this.player = player;
        this.cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}