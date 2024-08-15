package me.msuro.mdynastore.listener.guis;

import me.msuro.mdynastore.MDynaStore;
import me.msuro.mdynastore.guis.CategoryInventory;
import me.msuro.mdynastore.guis.CategorySelection;
import me.msuro.mdynastore.guis.InvUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class CategorySelectionClick implements Listener {

    private MDynaStore instance;

    public CategorySelectionClick(MDynaStore instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onCategorySelectionClick(InventoryClickEvent event) {
        if(!(event.getInventory().getHolder() instanceof CategorySelection))
            return;
        event.setCancelled(true);
        ItemStack item = event.getCurrentItem();
        if(item == null || !item.hasItemMeta() || item.getItemMeta() == null)
            return;
        String category = InvUtil.getPersistentData(item, "category");
        if(category == null)
            return;

        event.getWhoClicked().openInventory(new CategoryInventory(category, 1).getInventory());
    }

}
