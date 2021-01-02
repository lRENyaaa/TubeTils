package de.tubeof.tubetils.api.itembuilder;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.main.TubeTils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Base64;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;

@SuppressWarnings("ALL")
public class ItemBuilder {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    public ItemBuilder(String itemBuilderName) {
        if (data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Created new ItemBuilder with name: " + itemBuilderName);
    }

    public ItemStack normalItem(Material Material, int ID, String Name) {
        ItemStack item = new ItemStack(Material, 1, (short)ID);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack twoLoreItem(Material Material, int ID, String Name, String Lore1, String Lore2) {
        ItemStack item = new ItemStack(Material, 1, (short)ID);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(Lore1);
        Lore.add(Lore2);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack customLoresItem(Material Material, int ID, String Name, ArrayList<String> lores) {
        ItemStack item = new ItemStack(Material, 1, (short)ID);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack normalSkullItem(String Owner, String Name) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(Owner);
        meta.setDisplayName(Name);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack loreSkullItem(String Owner, String Name, String Lore1, String Lore2) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwner(Owner);
        meta.setDisplayName(Name);
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(Lore1);
        Lore.add(Lore2);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getSkullFromURL(String url) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        if(url.isEmpty())return head;

        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public ItemStack editExistItem(ItemStack existItem, String Name, String Lore1, String Lore2) {
        ItemStack item = existItem.clone();
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Name);
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(Lore1);
        Lore.add(Lore2);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }
}
