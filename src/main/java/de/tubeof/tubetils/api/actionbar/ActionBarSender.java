package de.tubeof.tubetils.api.actionbar;

import de.tubeof.tubetils.api.actionbar.events.ActionBarMessageEvent;
import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.main.TubeTils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class ActionBarSender {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    public ActionBarSender(String actionBarSenderName) {
        if (data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Created new ActionBarSender with name: " + actionBarSenderName);
    }

    /**
     * Sends a message to the player as an action bar
     * @param player The player
     * @param message Message, which will be displayed
     */
    private void sendActionBar(Player player, String message) {
        ActionBarMessageEvent actionBarMessageEvent = new ActionBarMessageEvent(player, message);
        Bukkit.getPluginManager().callEvent(actionBarMessageEvent);
        message = actionBarMessageEvent.getMessage();
        if (actionBarMessageEvent.isCancelled()) return;

        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }


    /**
     * Sends a message to the player as an actionbar
     * @param player The player the actionbar is for
     * @param message Your message
     * @param duration The duration of how long the action bar is displayed
     */
    public void sendActionBar(Player player, String message, Integer duration) {
        sendActionBar(player, message);

        if (duration >= 0) {
            (new BukkitRunnable() {
                public void run() {
                    sendActionBar(player, "");
                }
            }).runTaskLater(TubeTils.getInstance(), (duration + 1));
        }

        while (duration > 40) {
            duration -= 40;
            (new BukkitRunnable() {
                public void run() {
                    sendActionBar(player, message);
                }
            }).runTaskLater(TubeTils.getInstance(), duration);
        }
    }

    /** Sends a message to all players as an action bar
     * @param message Your message
     */
    public void sendActionBarToAllPlayers(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> sendActionBar(player, message));
    }

    /**
     * Sends a message to all players as an action bar
     * @param message Your message
     * @param duration The duration of how long the action bar is displayed
     */
    public void sendActionBarToAllPlayers(String message, int duration) {
        Bukkit.getOnlinePlayers().forEach(player -> sendActionBar(player, message, duration));
    }
}
