package de.tubeof.tubetils.api.title;

import de.tubeof.tubetils.api.title.events.TitleClearEvent;
import de.tubeof.tubetils.api.title.events.TitleSendEvent;
import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.main.TubeTils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

@SuppressWarnings("ALL")
public class TitleSender {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    public TitleSender(String titleSenderName) {
        if (data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Created new TitleSender with name: " + titleSenderName);
    }


    /**
     *
     * @param player The player the title is for
     * @param fadeIn The fade-in time in ticks
     * @param stay The time for how long the title should be displayed in ticks
     * @param fadeOut The fade-out time in ticks
     * @param title Your Title
     * @param subtitle Your Subtitle
     */
    public void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        TitleSendEvent titleSendEvent = new TitleSendEvent(player, title, subtitle);
        Bukkit.getPluginManager().callEvent(titleSendEvent);
        if(titleSendEvent.isCancelled()) return;
        title = titleSendEvent.getTitle();
        subtitle = titleSendEvent.getSubtitle();

        try {
            if (title != null) {
                Object e = getNMS("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatTitle = getNMS("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
                Constructor subtitleConstructor = getNMS("PacketPlayOutTitle").getConstructor(new Class[] { getNMS("PacketPlayOutTitle").getDeclaredClasses()[0], getNMS("IChatBaseComponent"), int.class, int.class, int.class });
                Object titlePacket = subtitleConstructor.newInstance(new Object[] { e, chatTitle, fadeIn, stay, fadeOut });
                sendPacket(player, titlePacket);

                e = getNMS("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                chatTitle = getNMS("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
                subtitleConstructor = getNMS("PacketPlayOutTitle").getConstructor(new Class[] { getNMS("PacketPlayOutTitle").getDeclaredClasses()[0], getNMS("IChatBaseComponent") });
                titlePacket = subtitleConstructor.newInstance(new Object[] { e, chatTitle });
                sendPacket(player, titlePacket);
            }

            if (subtitle != null) {
                Object e = getNMS("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatSubtitle = getNMS("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + title + "\"}" });
                Constructor subtitleConstructor = getNMS("PacketPlayOutTitle").getConstructor(new Class[] { getNMS("PacketPlayOutTitle").getDeclaredClasses()[0], getNMS("IChatBaseComponent"), int.class, int.class, int.class });
                Object subtitlePacket = subtitleConstructor.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
                sendPacket(player, subtitlePacket);

                e = getNMS("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                chatSubtitle = getNMS("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
                subtitleConstructor = getNMS("PacketPlayOutTitle").getConstructor(new Class[] { getNMS("PacketPlayOutTitle").getDeclaredClasses()[0], getNMS("IChatBaseComponent"), int.class, int.class, int.class });
                subtitlePacket = subtitleConstructor.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
                sendPacket(player, subtitlePacket);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }
    }

    /**
     * Sends an empty title to a player
     * @param player The player the title is for
     */
    public void clearTitle(Player player) {
        TitleClearEvent titleClearEvent = new TitleClearEvent(player);
        Bukkit.getPluginManager().callEvent(titleClearEvent);
        if(titleClearEvent.isCancelled()) return;

        sendTitle(player, 0, 0, 0, "", "");
    }

    private Class<?> getNMS(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMS("Packet") }).invoke(playerConnection, new Object[] { packet });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
