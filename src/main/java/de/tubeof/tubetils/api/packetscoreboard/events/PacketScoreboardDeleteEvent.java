package de.tubeof.tubetils.api.packetscoreboard.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketScoreboardDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;

    public PacketScoreboardDeleteEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return this.handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
