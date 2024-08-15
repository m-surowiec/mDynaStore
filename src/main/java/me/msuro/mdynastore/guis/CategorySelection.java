package me.msuro.mdynastore.guis;

import me.msuro.mdynastore.ConfigUtil;
import me.msuro.mdynastore.Constants;
import me.msuro.mdynastore.MDynaStore;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;


public class CategorySelection implements InventoryHolder {

    private final Inventory inventory;
    private final MDynaStore instance;

    public CategorySelection() {
        this.instance = MDynaStore.getInstance();
        int size = ConfigUtil.getInt(Constants.MAINGUIROWS) * 9;
        String title = ConfigUtil.getMainMessage(Constants.MAINGUITITLE);
        inventory = Bukkit.createInventory(this, size, title);
        for(String category : Constants.CATEGORIES) {
            ItemStack is = ConfigUtil.getItemStack("sections.yml", category + ".display-item");
            ItemMeta im = is.getItemMeta();
            NamespacedKey key = new NamespacedKey(instance, "category");
            im.getPersistentDataContainer().set(key, PersistentDataType.STRING, category);
            is.setItemMeta(im);
            int slot = ConfigUtil.getInt("sections.yml", category + ".slot");
            inventory.setItem(slot, is);
        }
        ItemStack filler = ConfigUtil.getItemStack("config.yml", "main-gui.filler");
        for(int i = 0; i < size; i++) {
            if(inventory.getItem(i) == null || inventory.getItem(i).getType().isAir()) {
                inventory.setItem(i, filler);
            }
        }
    }

    @Override
    @Nonnull
    public Inventory getInventory() {
        return inventory;
    }
}
