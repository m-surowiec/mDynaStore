package me.msuro.mdynastore;

import me.msuro.mdynastore.colors.ColorAPI;
import me.msuro.mdynastore.guis.InvUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigUtil {

    private static String prefix = "#08FBF1&lm#15FBE8&lD#22FBDF&ly#2FFBD6&ln#3CFBCD&la#4AFAC3&lS#57FABA&lt#64FAB1&lo#71FAA8&lr#7EFA9F&le &8→&r";
    private static YamlConfiguration config;
    private static YamlConfiguration lang;
    public static MDynaStore instance;

    public static String getPrefix() {
        return prefix;
    }

    /**
     * Save the config
     *
     * @param type The type of the config to save
     *             0 = main config
     *             1 = lang config
     * @throws RuntimeException if the config file could not be saved
     */
    public static void saveConfig(int type) {
        try {
            switch (type) {
                case 0:
                    config.save(instance.getDataFolder() + "/config.yml");
                    break;
                case 1:
                    lang.save(instance.getDataFolder() + "/lang/" + getString("lang") + ".yml");
                    break;
            }
            instance.reloadConfig();
        } catch (IOException e) {
            throw new RuntimeException("Could not save config file!", e);
        }
    }

    public static void init() {
        // Initialize the main config.yml file
        if (!new File(instance.getDataFolder(), "config.yml").exists()) {
            instance.saveResource("config.yml", false);
            instance.getServer().getConsoleSender().sendMessage(ColorAPI.process(prefix + " &aConfig file created!"));
        }
        config = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "config.yml"));
        // Initialize the currently used language file
        prefix = getString("prefix");
        if (!new File(instance.getDataFolder(), "lang/" + getString("lang") + ".yml").exists()) {
            instance.saveResource("lang/" + getString("lang") + ".yml", false);
            instance.getServer().getConsoleSender().sendMessage(ColorAPI.process(prefix + " &a&lActive&a language file created!" + " &7(" + getString("lang") + ")"));
        }
        lang = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "lang/" + getString("lang") + ".yml"));
        // Initialize rest of the language files
        for(String lang : Constants.LANGS) {
            if (!new File(instance.getDataFolder(), "lang/" + lang + ".yml").exists()) {
                instance.saveResource("lang/" + lang + ".yml", false);
                instance.getServer().getConsoleSender().sendMessage(ColorAPI.process(prefix + " &aLanguage file created!" + " &7(" + lang + ")"));
            }
        }
        // Initialize default shop categories
        for(String category : Constants.CATEGORIES) {
            if(!new File(instance.getDataFolder(), "categories/" + category + ".yml").exists()) {
                instance.saveResource("categories/" + category + ".yml", false);
                instance.getServer().getConsoleSender().sendMessage(ColorAPI.process(prefix + " &aCategory file created!" + " &7(" + category + ")"));
            }
        }
        // Initialize the sections.yml file
        if(!new File(instance.getDataFolder(), "sections.yml").exists()) {
            instance.saveResource("sections.yml", false);
            instance.getServer().getConsoleSender().sendMessage(ColorAPI.process(prefix + " &aSections file created!"));
        }
    }

    public static void init(CommandSender sender) {
        // Initialize the main config.yml file
        if (!new File(instance.getDataFolder(), "config.yml").exists()) {
            instance.saveResource("config.yml", false);
            sender.sendMessage(ColorAPI.process(prefix + " &aConfig file created!"));
        }
        config = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "config.yml"));
        // Initialize the currently used language file
        prefix = getString("prefix");
        if (!new File(instance.getDataFolder(), "lang/" + getString("lang") + ".yml").exists()) {
            instance.saveResource("lang/" + getString("lang") + ".yml", false);
            sender.sendMessage(ColorAPI.process(prefix + " &a&lActive&a language file created!" + " &7(" + getString("lang") + ")"));
        }
        lang = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "lang/" + getString("lang") + ".yml"));
        // Initialize rest of the language files
        for(String lang : Constants.LANGS) {
            if (!new File(instance.getDataFolder(), "lang/" + lang + ".yml").exists()) {
                instance.saveResource("lang/" + lang + ".yml", false);
                sender.sendMessage(ColorAPI.process(prefix + " &aLanguage file created!" + " &7(" + lang + ")"));
            }
        }
        if(!new File(instance.getDataFolder(), "categories").exists()) {
            new File(instance.getDataFolder(), "categories").mkdir();
            sender.sendMessage(ColorAPI.process(prefix + " &aCategories folder created!"));
            // Initialize default shop categories
            for(String category : Constants.CATEGORIES) {
                if(!new File(instance.getDataFolder(), "categories/" + category + ".yml").exists()) {
                    instance.saveResource("categories/" + category + ".yml", false);
                    sender.sendMessage(ColorAPI.process(prefix + " &aCategory file created!" + " &7(" + category + ")"));
                }
            }
        }
        // Initialize the sections.yml file
        if(!new File(instance.getDataFolder(), "sections.yml").exists()) {
            instance.saveResource("sections.yml", false);
            sender.sendMessage(ColorAPI.process(prefix + " &aSections file created!"));
        }

    }

    public static YamlConfiguration getConfig() {
        return config;
    }

    public static YamlConfiguration getCategoryConfig(String category) {
        return YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), "categories/" + category + ".yml"));
    }

    public static YamlConfiguration getLang() {
        return lang;
    }

    //todo remember to add config option to don't send messages to player instead of always sending the error message
    public static String getMessage(String key) {
        String message = lang.getString(key);
        if (message == null) {
            instance.getServer().getConsoleSender().sendMessage(ColorAPI.process(prefix + " &cMessage not found! [config: lang/" + getString("lang") + ".yml, key: " + key + "]"));
            return null;
        }
        message = message.replace("{P}", prefix);
        return ColorAPI.process(message);
    }

    public static List<String> getMessageList(String key) {
        List<String> val = lang.getStringList(key);
        if (val.isEmpty()) {
            instance.getServer().getConsoleSender().sendMessage(ColorAPI.process(prefix + " &cMessage not found! [config: lang/" + getString("lang") + ".yml, key: " + key + "]"));
            return null;}
        for (int i = 0; i < val.size(); i++) {
            val.set(i, val.get(i).replace("{P}", prefix));
            val.set(i, ColorAPI.process(val.get(i)));
        }
        return val;
    }

    public static List<String> getStringList(String key) {
        List<String> val = config.getStringList(key);
        if (val.isEmpty()) {
            throw new RuntimeException("Could not find key " + key + " in config!");
        }
        return config.getStringList(key);
    }

    public static String getString(String key) {
        String val = config.getString(key);
        if (val == null) {
            throw new RuntimeException("Could not find key " + key + " in config!");
        }
        return val;
    }

    public static String getMainMessage(String key) {
        String message = config.getString(key);
        if (message == null) {
            instance.getServer().getConsoleSender().sendMessage(ColorAPI.process(prefix + " &cMessage not found! [config: config.yml, key: " + key + "]"));
            return null;
        }
        message = message.replace("{P}", prefix);
        return ColorAPI.process(message);
    }

    public static Integer getInt(String key) {
        return config.getInt(key);
    }

    public static String getString(String cfg, String key) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), cfg));
        return configuration.getString(key);
    }

    public static String getMessage(String cfg, String key) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), cfg));
        String message = configuration.getString(key);
        if (message == null) {
            instance.getServer().getConsoleSender().sendMessage(ColorAPI.process(prefix + " &cMessage not found! [config: " + cfg + ", key: " + key + "]"));
            return null;
        }
        message = message.replace("{P}", prefix);
        return ColorAPI.process(message);
    }

    public static Integer getInt(String cfg, String key) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), cfg));
        return configuration.getInt(key);
    }

    public static ItemStack getItemStack(String cfg, String key) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(), cfg));
        ConfigurationSection section = configuration.getConfigurationSection(key);
        String material = section.getString("material");
        String name = section.getString("name");
        boolean glowing = section.getBoolean("glowing");
        String[] enchantments = section.getStringList("enchantments").toArray(new String[0]);
        String[] lore = section.getStringList("lore").toArray(new String[0]);
        ItemStack is = InvUtil.createItem(name, material, 1, lore);
        if (material.equalsIgnoreCase("PLAYER_HEAD")) {
            String texture = section.getString("texture");
            is = InvUtil.createSkull(name, texture, 1, lore);
        }
        if (glowing) {
            is.addUnsafeEnchantment(Enchantment.LUCK, 1);
            ItemMeta im = is.getItemMeta();
            im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            is.setItemMeta(im);
        }
        for(String enchantment : enchantments) {
            String[] enchantmentSplit = enchantment.split(":");
            is.addUnsafeEnchantment(Enchantment.getByName(enchantmentSplit[0]), Integer.parseInt(enchantmentSplit[1]));
        }

        return is;
    }

}
