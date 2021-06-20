package net.minez2.CraftZChests.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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

public class TierManager {
	
	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	
	
	
	private static TierManager tierMang;
	
	/*
	 * Returns instance of tierMang used in this plugin
	 */
	public static TierManager getTierMang() {
		if(tierMang == null) tierMang = new TierManager();
		
		return tierMang;
	}
	
	
	/*
	 * Creates Tier folder if not exists
	 */
	public void createTierFolder() {
		File folder = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers");
		if(!folder.exists()) {
			folder.mkdirs();
			Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("tier_folder_created")));
			}
		
		File exampleTier = new File(plugin.getDataFolder().getPath() + File.separator + "exampleTier.yml");

		if(!exampleTier.exists()) {
				plugin.saveResource("exampleTier.yml", false);
				Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("example_tier_created")));
		}
		YamlConfiguration.loadConfiguration(exampleTier);
			return;		
	}
	
	/*
	 * Create the tier FILE. Return false if file already exists or error
	 */
	public Boolean createTier(String tier) {
		File file = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
		
		if(!file.exists()) {
		try {
		file.createNewFile();
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.set("ID", this.generateNewID());
		config.set("max_item_count", 3);
		config.set("min_item_count", 1);
		config.set("allow_item_repeats", true);
		config.set("sound.enabled", true);
		config.set("sound.effect", "DEFAULT");
		config.set("particles.enabled", true);
		config.set("particles.effect", "DEFAULT");
		config.set("particles.shape", "NORMAL");
		config.set("invisible", false);
		config.set("max_regen_seconds", 300);
		config.set("min_regen_seconds", 60);
		config.save(file);
		return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		}
		return false;
	}
	
	/*
	 * Deletes the file for the specified tier
	 */
	public Boolean deleteTierFile(String tier) {
		
		File file = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
		if(file.exists()) {
			file.delete();
			return true;
		}
		
		return false;
	}
	
	/*
	 * Returns the paths of all files in the tiers folder. If none, returns null
	 */
	public List<String> getAllTiers() {
		File file = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers");
		File[] allTiers = file.listFiles();
		if(allTiers.length < 1) {
			return null;
		}
		List<String> tierNames = new ArrayList<String>();
		for(File f : allTiers) {
			String name = f.getPath();
			//Take the file path, start from the end, and modify the name to be the part between Tiers/ and .yml
			for(int i = name.length() - 5; i < name.length() && i >= 0; --i) {
				if(name.substring(i, i+5).equalsIgnoreCase("Tiers")){
					name = name.substring(i + 6, name.length() - 4);
					tierNames.add(name);
					break;
				}
			}
		}
		return tierNames;
	}
	
	/*
	 * Returns T/F whether string is already a tier or not
	 */
	public Boolean isTier(String tier) {
		List<String> allTiers = this.getAllTiers();
		if(this.getAllTiers()==null) return false;
		for(String s : allTiers) {
			if(tier.equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Renames a tier. Returns True if successfully renamed.
	 */
	public Boolean renameTier(String oldTier, String newTier) {
		
		if(this.isTier(oldTier)) {
			//Rename the file to the newTier
			File oldFile = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + oldTier + ".yml");
			File newFile = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + newTier + ".yml");
			if(newFile.exists()) return false;
			try {
				oldFile.renameTo(newFile);
				return true;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	/*
	 * gets the name of a Tier from the ID
	 */
	public String getTierFromID(String id) {
		String tier = "";
		File folder = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers");
		File[] allTiers = folder.listFiles();
		for(File tierFile : allTiers) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(tierFile);
			String tierID = config.getString("ID");
			if(tierID.equals(id)) return tierFile.getPath().substring(27, tierFile.getPath().length() - 4);
		}
		
		return tier;
	}
	
	/*
	 * Gets the ID from the tier specified. Else returns null
	 */
	public String getIDFromTier(String tier) {
		String id = "";
		File file = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
		if(file.exists()) {
			YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
			id = config.getString("ID");
			if(id != null) return id;
		}

		return null;
	}
	
	
	/*
	 * Generates a new random ID
	 */
	public String generateNewID() {
		int idNum;
		String id = "";
		for(Boolean i = false; i == false; ++idNum) {
			//Creates a random ID
			idNum = (int) (Math.random() * 9999);
			id = "CZC" + idNum;
			
			//Check if id already exists
			if(!this.isTierId(id)) break;
		}

		return id;
	}
	
	/*
	 * Returns T/F depending on if given ID is already being used or not
	 */
	public Boolean isTierId(String id) {
		List<String> allTiers = this.getAllTiers();
		for(String tier : allTiers) {
			File tierFile = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
			YamlConfiguration tierConfig = YamlConfiguration.loadConfiguration(tierFile);
			String testId = tierConfig.getString("ID");
			if(testId == null) continue;
			if(testId.equals(id)) return true;
		}
		return false;
		
		
	}
	
	

}
