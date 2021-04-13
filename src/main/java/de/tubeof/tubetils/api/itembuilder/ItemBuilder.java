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

@SuppressWarnings({"FieldCanBeLocal", "unused", "deprecation"})
public class ItemBuilder {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    public ItemBuilder(String itemBuilderName) {
        if (data.isDebuggingEnabled()) ccs.sendMessage(data.getPrefix() + "Created new ItemBuilder with name: " + itemBuilderName);
    }

    /**
     * Will create and return a normal ItemStack.
     * Deprecated! Use simpleItemStack instead please.
     * @param material The material
     * @param id Item Sub-ID for older versions
     * @param name Name of the ItemStack. Colorcodes can be used.
     * @return The final ItemStack
     */
    @Deprecated
    public ItemStack normalItem(Material material, int id, String name) {
        ItemStack item = new ItemStack(material, 1, (short)id);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Will create and return a normal ItemStack.
     * @param material The material
     * @param amount Amount of Items
     * @param id Item Sub-ID for older versions
     * @param name Name of the ItemStack. Colorcodes can be used
     * @return The final ItemStack
     */
    public ItemStack simpleItemStack(Material material, int amount, int id, String name) {
        ItemStack item = new ItemStack(material, amount, (short)id);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates and returns an ItemStack with 2 lores.
     * Deprecated! Use dynamicLoreItem instead please.
     * @param material The material
     * @param id Item Sub-ID for older versions
     * @param name Name of the ItemStack. Colorcodes can be used
     * @param lore1 The first lore
     * @param lore2 The second lore
     * @return The final ItemStack
     */
    @Deprecated
    public ItemStack twoLoreItem(Material material, int id, String name, String lore1, String lore2) {
        ItemStack item = new ItemStack(material, 1, (short)id);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(lore1);
        lore.add(lore2);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates and returns an ItemStack with an arbitrary number of lores.
     * @param material The material
     * @param amount Amount of Items
     * @param id Item Sub-ID for older versions
     * @param name Name of the ItemStack. Colorcodes can be used
     * @param lores An ArrayList with the type String, in which the lores are passed
     * @return The final ItemStack
     */
    public ItemStack dynamicLoreItem(Material material, int amount, int id, String name, ArrayList<String> lores) {
        ItemStack item = new ItemStack(material, amount, (short)id);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        meta.setLore(lores);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Deprecated! Use normalPlayerSkullItem instead please.
     * Creates and returns a player head with the respective skin (head).
     * @param skullOwnerName The player name, which current skin is set for the head
     * @param name Name of the ItemStack. Colorcodes can be used
     * @return The final ItemStack
     */
    @Deprecated
    public ItemStack normalSkullItem(String skullOwnerName, String name) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setOwner(skullOwnerName);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates and returns a player head with the respective skin (head).
     * @param skullOwnerName The player name, which current skin is set for the head
     * @param amount Amount of Items
     * @param name Name of the ItemStack. Colorcodes can be used
     * @return The final ItemStack
     */
    public ItemStack normalPlayerSkullItem(String skullOwnerName, int amount, String name) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount, (short)3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setOwner(skullOwnerName);
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Deprecated! Use dynamicLorePlayerSkullItem instead please.
     *
     * @param skullOwnerName
     * @param name
     * @param lore1
     * @param lore2
     * @return
     */
    @Deprecated
    public ItemStack loreSkullItem(String skullOwnerName, String name, String lore1, String lore2) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1, (short)3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setOwner(skullOwnerName);
        meta.setDisplayName(name);
        ArrayList<String> Lore = new ArrayList<>();
        Lore.add(lore1);
        Lore.add(lore2);
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     *
     * @param skullOwnerName
     * @param amount
     * @param name
     * @param lores
     * @return
     */
    public ItemStack dynamicLorePlayerSkullItem(String skullOwnerName, int amount, String name, ArrayList<String> lores) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount, (short)3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        assert meta != null;
        meta.setOwner(skullOwnerName);
        meta.setDisplayName(name);
        meta.setLore(lores);
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
