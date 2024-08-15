package me.msuro.mdynastore.guis;

import me.msuro.mdynastore.ConfigUtil;
import me.msuro.mdynastore.Constants;
import me.msuro.mdynastore.MDynaStore;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;


public class CategoryInventory implements InventoryHolder {

    private final Inventory inventory;
    private final MDynaStore instance;
    private final String category;
    private final Integer page;

    public CategoryInventory(String category, Integer page) {
        this.instance = MDynaStore.getInstance();
        this.category = category;
        this.page = page;

        int size = ConfigUtil.getInt("sections.yml", category + ".gui.rows") * 9;
        String title = ConfigUtil.getMessage("sections.yml", category + ".gui.title");
        Inventory myInv = Bukkit.createInventory(this, size, title);

        ConfigurationSection items = ConfigUtil.getCategoryConfig(category);
        int highestPage = 1;
        for (String key : items.getKeys(true)) {
            String[] split = key.split("\\.");
            highestPage = Integer.max(highestPage, Integer.parseInt(split[0].replace("page_", "")));
            if(Integer.parseInt(split[0].replace("page_", "")) == page) {
                if(split.length >= 2) {
                    Integer slot = Integer.parseInt(split[1]);
                    ItemStack is = ConfigUtil.getItemStack("categories/" + category + ".yml", "page_" + page + "." + slot);
                    InvUtil.addPriceLore(is, category, page, slot);
                    myInv.setItem(slot, is);
                }
            }
        }
        inventory = InvUtil.addNavbar(myInv, highestPage, category, page, "category_selection");
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
