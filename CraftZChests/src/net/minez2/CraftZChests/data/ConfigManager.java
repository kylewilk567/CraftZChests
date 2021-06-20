package net.minez2.CraftZChests.data;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;
import net.minez2.CraftZChests.Main;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class ConfigManager {

	private Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	File configFile;
	FileConfiguration config;
	
	private static ConfigManager configMang;
	
	
	/*
	 * gets the instance of ConfigManager used in this plugin
	 */
	public static ConfigManager getConfigMang() {
		if(configMang == null) {
			configMang = new ConfigManager();
		}
		return configMang;
	}
	
	/*
	 * create file if it does not exist and load file
	 */
	public void setupConfig() {
	 configFile = new File(plugin.getDataFolder(), "config.yml");
	
	if(!configFile.exists()) {
			plugin.saveResource("config.yml", false);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("config_created")));
	}
	
	config = YamlConfiguration.loadConfiguration(configFile);
	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("config_loaded")));
}
	
	/*
	 * get config file
	 */
	public YamlConfiguration getConfig() {
		configFile = new File(plugin.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		return (YamlConfiguration) config;
	}
	
	/*
	 * Reload config file
	 */
	public void reloadConfig() {
		configFile = new File(plugin.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("config_loaded")));
	}
	
}
