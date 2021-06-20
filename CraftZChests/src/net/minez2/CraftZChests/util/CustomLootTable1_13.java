package net.minez2.CraftZChests.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.ConfigManager;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class CustomLootTable1_13 {
	
	 private Main plugin = Main.getPlugin(Main.class);
	 ConfigManager configMang = ConfigManager.getConfigMang();
	 private Collection<ItemStack> items = new ArrayList<ItemStack>();





	@SuppressWarnings("deprecation")
	public Collection<ItemStack> populateLoot(Random random, String tier) {
		
		File file = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		//Create ItemStack and Weight Hashmap
		HashMap<ItemStack, Integer> loottable = new HashMap<ItemStack, Integer>();
		config.getConfigurationSection("items").getKeys(false).forEach(key -> {
			
			ItemStack item;
			
			//Command integration! Create an itemstack where the display name is the KEY in the config - easier than completely redoing this algo
			if(config.getString("items." + key + ".command") != null) {
				item = new ItemStack(Material.REDSTONE);
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName("CZCcmd-items." + key + ".command");
				item.setItemMeta(meta);
			}
			else {

			item = new ItemStack(Material.valueOf(config.getString("items." + key + ".material")), config.getInt("items." + key + ".amount"));
			

			

			if(item.getType().equals(Material.POTION) || item.getType().equals(Material.SPLASH_POTION) || item.getType().equals(Material.LINGERING_POTION)) {
				PotionMeta meta = (PotionMeta) item.getItemMeta();
				meta.addCustomEffect(new PotionEffect(PotionEffectType.getByName(config.getString("items." + key + ".potion_effect")),
						config.getInt("items." + key + ".potion_duration"), config.getInt("items." + key + ".potion_level")), true);
				if(config.getInt("items." + key + ".potion_color.red") != 0 && config.getInt("items." + key + ".potion_color.green") != 0 &&
						config.getInt("items." + key + ".potion_color.blue") != 0) {
				meta.setColor(Color.fromRGB(config.getInt("items." + key + ".potion_color.red"), config.getInt("items." + key + ".potion_color.green"),
						config.getInt("items." + key + ".potion_color.blue")));
				}
				if(config.getBoolean("items." + key + ".potion_hide_effects"))	meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
				if(config.getString("items." + key + ".display_name") != null) {
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("items." + key + ".display_name")));
				}
				item.setItemMeta(meta);
			}
			else {
			ItemMeta meta = item.getItemMeta();
			//Set enchants if necessary
			if(config.getConfigurationSection("items." + key + ".enchants") != null) {
			config.getConfigurationSection("items." + key + ".enchants").getKeys(false).forEach(enchantment -> {
				@SuppressWarnings("deprecation")
				Enchantment enchant = Enchantment.getByName(config.getString("items." + key + ".enchants." + enchantment + ".name"));
				int level = config.getInt("items." + key + ".enchants." + enchantment + ".level");
				meta.addEnchant(enchant, level, true);
			});
			}
			if(config.getString("items." + key + ".display_name") != null && !item.getType().equals(Material.POTION) && !item.getType().equals(Material.SPLASH_POTION) && !item.getType().equals(Material.LINGERING_POTION)) {
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("items." + key + ".display_name")));
			}
			
			//Set Durability if necessary
			if(config.getDouble("items." + key + ".max_durability") != 0 && config.getDouble("items." + key + ".min_durability") != 0) {
				Damageable meta2 = (Damageable) meta;
				meta2.setDamage(this.getDurability(key, tier, item.getType()));
				item.setItemMeta((ItemMeta) meta2);
			} 
			else {
			item.setItemMeta(meta);
			}
			}
		}

			int weight = config.getInt("items." + key + ".weight");
			loottable.put(item, weight);
		});
		
		
		//Choose items
		int maxNumItems = (int) Math.round(((Math.random() * (config.getInt("max_item_count") - config.getInt("min_item_count"))) + config.getInt("min_item_count")));
		int amount = 0;
		while(amount < maxNumItems && !loottable.isEmpty()) {
			
			//Initialize total weight
			int totalWeight = 0;
			Iterator<Entry<ItemStack, Integer>> iterator = loottable.entrySet().iterator();
			while(iterator.hasNext()) {
				totalWeight += iterator.next().getValue();
			}
			
			//Choose random number between totalWeight and 0
			int chosenItemNum = (int) Math.round((Math.random() * (totalWeight - 1)));
			
			
			//Traverse loottable until addedWeights > chosenItemNum to choose the item!
			int addedWeights = 0;
			ItemStack chosenItem = new ItemStack(Material.REDSTONE_BLOCK); //default material if plugin errors
			
			Iterator<Entry<ItemStack, Integer>> iterator2 = loottable.entrySet().iterator();
			while(iterator2.hasNext() && addedWeights <= chosenItemNum) {
				Entry<ItemStack, Integer> entry = iterator2.next();
				chosenItem = entry.getKey();
				addedWeights += entry.getValue();
			}
			
			//Update itemTypes and item amount
			++amount;
			if(!config.getBoolean("allow_item_repeats")) loottable.remove(chosenItem);
			items.add(chosenItem);
		}
		
		return items;
		
	}
	
	private short getDurability(String key, String tier, Material material) {
		
		File file = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		double max = config.getDouble("items." + key + ".max_durability");
		double min = config.getDouble("items." + key + ".min_durability");
		
		double durability = ((Math.random() * (max - min)) + min);
		int maxDura = (int) material.getMaxDurability();
		maxDura *= durability;
		
		return (short) maxDura;
		
	}

}
