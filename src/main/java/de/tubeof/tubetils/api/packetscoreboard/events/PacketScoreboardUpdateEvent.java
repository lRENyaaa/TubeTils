package de.tubeof.tubetils.api.packetscoreboard.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called if a PacketScoreboard are updated.
 */
public class PacketScoreboardUpdateEvent extends Event {

    private final HandlerList handlers = new HandlerList();
    private final Player player;

    public PacketScoreboardUpdateEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public Player getPlayer() {
        return this.player;
    }

}
