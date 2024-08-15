package me.msuro.mdynastore.guis;

import com.saicone.rtag.util.SkullTexture;
import me.msuro.mdynastore.ConfigUtil;
import me.msuro.mdynastore.MDynaStore;
import me.msuro.mdynastore.colors.ColorAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class InvUtil {

    public static MDynaStore instance;

    public static ItemStack createItem(String name, @Nonnull String material, int amount, String... lore) {
        ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(material)), amount);
        ItemMeta meta = item.getItemMeta();
        if(!name.isEmpty() && name != null) meta.setDisplayName(ColorAPI.process(name));
        if(lore.length > 0) meta.setLore(ColorAPI.process(Arrays.asList(lore)));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createSkull(String name, String texture, int amount, String... lore) {
        ItemStack item = createItem(name, "PLAYER_HEAD", amount, lore);
        return SkullTexture.setTexture(item, texture);
    }

    public static Inventory addNavbar(Inventory inventory, int highestPage, String category, int page, String lastPage) {
        int size = inventory.getSize();
        int goBackSlot = ConfigUtil.getInt("config.yml", "navbar.go_back.slot");
        int nextPageSlot = ConfigUtil.getInt("config.yml", "navbar.next_page.slot");
        int previousPageSlot = ConfigUtil.getInt("config.yml", "navbar.previous_page.slot");

        ItemStack filler = ConfigUtil.getItemStack("config.yml", "navbar.filler");
        ItemStack goBackItem = ConfigUtil.getItemStack("config.yml", "navbar.go_back.display-item");
        ItemStack nextItem = ConfigUtil.getItemStack("config.yml", "navbar.next_page.display-item");
        ItemStack previousItem = ConfigUtil.getItemStack("config.yml", "navbar.previous_page.display-item");


        setPersistentData(previousItem, "string", "function:go_back", "category:"+category);
        setPersistentData(previousItem, "int", "page:"+page, "highest_page:"+highestPage);

        setPersistentData(goBackItem, "string", "function:go_back", "category:"+category, "last_page:"+lastPage);
        setPersistentData(goBackItem, "int", "page:"+page, "highest_page:"+highestPage);

        setPersistentData(nextItem, "string", "function:next", "category:"+category);
        setPersistentData(nextItem, "int", "page:"+page, "highest_page:"+highestPage);

        for(int i = size-9; i < size; i++) {
            inventory.setItem(i, filler);

            if(i == previousPageSlot +size-9) {
                inventory.setItem(i, previousItem);
            }
            if(i == goBackSlot +size-9) {
                inventory.setItem(i, goBackItem);
            }
            if(i == nextPageSlot +size-9) {
                inventory.setItem(i, nextItem);
            }
        }
        return inventory;
    }

    public static void setPersistentData(ItemStack item, String type, String... strings) {
        ItemMeta meta = item.getItemMeta();
        for (String s : strings) {
            NamespacedKey key = new NamespacedKey(instance, s.split(":")[0]);
            String value = s.split(":")[1];
            switch (type) {
                case "string":
                    meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value);
                    break;
                case "int":
                    meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, Integer.parseInt(value));
                    break;
            }
        }
        item.setItemMeta(meta);
    }

    public static String getPersistentData(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey nKey = new NamespacedKey(instance, key);
        if(meta == null || !meta.getPersistentDataContainer().has(nKey, PersistentDataType.STRING)) return "";
        return meta.getPersistentDataContainer().get(nKey, PersistentDataType.STRING);
    }

    public static int getPersistentDataInt(ItemStack item, String key) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey nKey = new NamespacedKey(instance, key);
        if(meta == null || !meta.getPersistentDataContainer().has(nKey, PersistentDataType.INTEGER)) return 0;
        return meta.getPersistentDataContainer().get(nKey, PersistentDataType.INTEGER) ;
    }

    public static void addPriceLore(ItemStack item, String category, int page, int slot) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return;
        List<String> oldLore = meta.getLore();
        List<String> lore = ConfigUtil.getMessageList("transaction-screen.lore");
        Integer buyPrice =  ConfigUtil.getInt("categories/" + category  + ".yml", "page_" + page + "." + slot + ".buy");
        Integer sellPrice = ConfigUtil.getInt("categories/" + category  + ".yml", "page_" + page + "." + slot + ".sell");
        if(lore == null) return;
        lore.replaceAll(s -> s
                .replace("{buy-price}", buyPrice.toString())
                .replace("{sell-price}", sellPrice.toString()));
        if(oldLore != null) oldLore.addAll(lore);
        meta.setLore(oldLore);
        item.setItemMeta(meta);

    }


}

