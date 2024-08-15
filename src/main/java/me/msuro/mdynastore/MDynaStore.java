// todo cache all items
package me.msuro.mdynastore;

import me.msuro.mdynastore.commands.MDS;
import me.msuro.mdynastore.commands.Reload;
import me.msuro.mdynastore.commands.Shop;
import me.msuro.mdynastore.guis.InvUtil;
import me.msuro.mdynastore.listener.guis.CategoryInventoryClick;
import me.msuro.mdynastore.listener.guis.CategorySelectionClick;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class MDynaStore extends JavaPlugin {

    private static MDynaStore instance;
    private Logger logger;
    private static Economy economy = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        logger = instance.getLogger();
        InvUtil.instance = instance;
        // bStats metrics (https://bstats.org/plugin/bukkit/mDynaStore/22960)
        new Metrics(this, 22960);
        // Vault setup
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            logger.severe("Vault not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            logger.severe("Economy not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        economy = rsp.getProvider();
        // Config setup
        ConfigUtil.instance = instance;
        ConfigUtil.init();
        // Commands setup
        getCommand("mds").setExecutor(new MDS(new Reload(), new Shop()));
        // Events setup
        getServer().getPluginManager().registerEvents(new CategorySelectionClick(this), this);
        getServer().getPluginManager().registerEvents(new CategoryInventoryClick(this), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MDynaStore getInstance() {
        return instance;
    }
}
