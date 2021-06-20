package net.minez2.CraftZChests.util;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.ConfigManager;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class TierEditorInventory {
	
	Main plugin = Main.getPlugin(Main.class);
	ConfigManager configMang = ConfigManager.getConfigMang();
	private static TierEditorInventory tierInven;
	
	public static TierEditorInventory getTierInven() {
		if(tierInven == null) tierInven = new TierEditorInventory();
		return tierInven;
	}
	
	public Inventory createTierGUI(String tier) {
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.RED + "CZC Tier Editor - Tier " + tier);
		
		
		return inv;
		
	}
	
	
	public void updateContents(String tier, ItemStack[] items) {
		File file = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		int i = 0;
		for(ItemStack item : items) {
			if(item == null) continue;
			i++;
			config.set("items." + "item" + i + ".material", item.getType().name());
			config.set("items." + "item" + i + ".amount", item.getAmount());
			config.set("items.item" + i + ".weight" , 10);

		}
		try {
			config.save(file);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
