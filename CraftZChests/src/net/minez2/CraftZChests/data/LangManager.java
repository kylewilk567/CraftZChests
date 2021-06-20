package net.minez2.CraftZChests.data;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.minez2.CraftZChests.Main;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class LangManager {

	
	private Main plugin = Main.getPlugin(Main.class);
	private static LangManager emang;

	
	public FileConfiguration engConfig;
	public File engFile;
	


	public void setupLang() {
		
	engFile = new File(plugin.getDataFolder(), "eng.yml");

	if(!engFile.exists()) {
			plugin.saveResource("eng.yml", false);
			Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', 
					" &aeng.yml has been created."));
	}
	engConfig = YamlConfiguration.loadConfiguration(engFile);
	
/*	//Spanish file
	engFile = new File(plugin.getDataFolder(), "esp.yml");
	if(!engFile.exists()) {
		plugin.saveResource("esp.yml", false);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', 
				getLang().getString("Prefix") + " &aesp.yml has been created."));
}*/
}


public FileConfiguration getLang() {
	engFile = new File(plugin.getDataFolder(), plugin.getConfig().getString("language") + ".yml");
	engConfig = YamlConfiguration.loadConfiguration(engFile);
	return engConfig;
}

public void saveEng() {
	try {
		getLang().save(engFile);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', 
				getLang().getString("prefix") + " &aLanguage file has been saved."));
	} catch(IOException e ) {
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', 
				getLang().getString("prefix") + " &cCould not save language file!"));
	}
}

public void reloadEng() {
	engFile = new File(plugin.getDataFolder(), plugin.getConfig().getString("language") + ".yml");
	engConfig = YamlConfiguration.loadConfiguration(engFile);
	Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', 
			getLang().getString("prefix") + " &aLanguage has been reloaded."));
	return;
}

/*
 * Returns the instance of language used in this plugin
 */

public static LangManager getLmang() {
	if(emang == null) {
		emang = new LangManager();
	}
	return emang;
}

/*
 * Gets a message from the language file
 */
public String getMessage(String messageID) {
	return this.getLang().getString("prefix") + " " + this.getLang().getString(messageID);
}
}
