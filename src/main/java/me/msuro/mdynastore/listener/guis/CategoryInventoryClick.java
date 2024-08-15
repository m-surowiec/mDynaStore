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
import org.bukkit.persistence.PersistentDataType;

public class CategoryInventoryClick implements Listener {

    private MDynaStore instance;

    public CategoryInventoryClick(MDynaStore instance) {
        this.instance = instance;

    }

    @EventHandler
    public void onCategoryInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder() instanceof CategoryInventory) || event.getCurrentItem() == null)
            return;
        event.setCancelled(true);

        ItemStack item = event.getCurrentItem();
        String function = InvUtil.getPersistentData(item, "function");
        System.out.println("function: " + function);
        String category = InvUtil.getPersistentData(item, "category");
        ItemMeta im = item.getItemMeta();
        if (im == null || function == null || category == null) return;
        int page = InvUtil.getPersistentDataInt(item, "page");
        int highestPage = InvUtil.getPersistentDataInt(item, "highest_page");
        if (function.equalsIgnoreCase("go_back")) {
            String lastPage = InvUtil.getPersistentData(item, "last_page");
            switch (lastPage) {
                case "category_selection":
                    event.getWhoClicked().openInventory(new CategorySelection().getInventory());
                    break;
                default:
                    event.getWhoClicked().openInventory(new CategoryInventory(category, 1).getInventory());
            }
        }
        if (function.equalsIgnoreCase("previous_page")) {
            if (page - 1 < 1) {
                // todo send message
                return;
            }
            event.getWhoClicked().openInventory(new CategoryInventory(category, page - 1).getInventory());
        }
        if (function.equalsIgnoreCase("next_page")) {
            if (page + 1 > highestPage) {
                // todo send message
                return;
            }
            event.getWhoClicked().openInventory(new CategoryInventory(category, page + 1).getInventory());
        }
    }


}
