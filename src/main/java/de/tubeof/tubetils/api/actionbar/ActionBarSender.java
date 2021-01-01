package de.tubeof.tubetils.api.actionbar;

import de.tubeof.tubetils.api.actionbar.events.ActionBarMessageEvent;
import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.main.TubeTils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActionBarSender {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    private String nmsver;
    private Boolean useOldMethods = false;

    public ActionBarSender(String actionBarSenderName) {
        if (data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Created new ActionBarSender with name: " + actionBarSenderName);

        nmsver = Bukkit.getServer().getClass().getPackage().getName();
        nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
        if (data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "");

        if (nmsver.equalsIgnoreCase("v1_8_R1") || nmsver.startsWith("v1_7_")) {
            useOldMethods = true;
        }
    }

    private void sendActionBar(Player player, String message) {
        ActionBarMessageEvent actionBarMessageEvent = new ActionBarMessageEvent(player, message);
        Bukkit.getPluginManager().callEvent(actionBarMessageEvent);
        message = actionBarMessageEvent.getMessage();
        if (actionBarMessageEvent.isCancelled()) return;

        try {
            Object packet;
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);

            Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
            Class<?> packetClass = Class.forName("net.minecraft.server." + nmsver + ".Packet");
            if (useOldMethods) {
                Class<?> chatSerializerClass = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
                Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                Method m3 = chatSerializerClass.getDeclaredMethod("a", new Class[]{String.class});
                Object cbc = iChatBaseComponentClass.cast(m3.invoke(chatSerializerClass, new Object[]{"{\"text\": \"" + message + "\"}"}));
                packet = packetPlayOutChatClass.getConstructor(new Class[]{iChatBaseComponentClass, byte.class}).newInstance(new Object[]{cbc, Byte.valueOf((byte) 2)});
            } else {
                Class<?> chatComponentTextClass = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
                Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
                try {
                    Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + nmsver + ".ChatMessageType");
                    Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
                    Object chatMessageType = null;
                    for (Object obj : chatMessageTypes) {
                        if (obj.toString().equals("GAME_INFO")) {
                            chatMessageType = obj;
                        }
                    }
                    Object chatCompontentText = chatComponentTextClass.getConstructor(new Class[]{String.class}).newInstance(new Object[]{message});
                    packet = packetPlayOutChatClass.getConstructor(new Class[]{iChatBaseComponentClass, chatMessageTypeClass}).newInstance(new Object[]{chatCompontentText, chatMessageType});
                } catch (ClassNotFoundException cnfe) {
                    Object chatCompontentText = chatComponentTextClass.getConstructor(new Class[]{String.class}).newInstance(new Object[]{message});
                    packet = packetPlayOutChatClass.getConstructor(new Class[]{iChatBaseComponentClass, byte.class}).newInstance(new Object[]{chatCompontentText, Byte.valueOf((byte) 2)});
                }
            }
            Method craftPlayerHandleMethod = craftPlayerClass.getDeclaredMethod("getHandle", new Class[0]);
            Object craftPlayerHandle = craftPlayerHandleMethod.invoke(craftPlayer, new Object[0]);
            Field playerConnectionField = craftPlayerHandle.getClass().getDeclaredField("playerConnection");
            Object playerConnection = playerConnectionField.get(craftPlayerHandle);
            Method sendPacketMethod = playerConnection.getClass().getDeclaredMethod("sendPacket", new Class[]{packetClass});
            sendPacketMethod.invoke(playerConnection, new Object[]{packet});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void sendActionBarToAllPlayers(String message) {
        for (Player players : Bukkit.getOnlinePlayers())
            sendActionBar(players, message);
    }

    public void sendActionBarToAllPlayers(String message, int duration) {
        for (Player players : Bukkit.getOnlinePlayers())
            sendActionBar(players, message, duration);
    }
}
