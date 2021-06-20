package net.minez2.CraftZChests.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.lang3.tuple.MutablePair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.sql.SQLEditor;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class RegenDataManager1_13 {

	
	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	SQLEditor Editor = SQLEditor.getEditor();
	TierManager tierMang = TierManager.getTierMang();
	 //Stores chest Location as String (same as SQLEditor) and time (Long) chest should be regenerated and Runnable TaskID (int)
	private static HashMap<String, MutablePair<Integer, Long>> chestTimes = new HashMap<String, MutablePair<Integer,Long>>();
	
	File regenDataFile = new File(plugin.getDataFolder().getPath() + File.separator + "regenData.yml");
	
	
	private static RegenDataManager1_13 regenMang;
	
	/*
	 * Returns instance of regenMang used in this plugin
	 */
	public static RegenDataManager1_13 getRegenMang() {
		if(regenMang == null) regenMang = new RegenDataManager1_13();
		return regenMang;
	}
	
	
	/*
	 * Schedule timers for regen chests
	 */
	public void rescheduleTimers() {
		File file = new File(plugin.getDataFolder().getPath() + File.separator + "regenData.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		config.getKeys(false).forEach(location ->{
			
			
			Long delay = config.getLong(location) / 50;
			
			
			int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

	@Override
	public void run() {
		//Reset the type
		String id = Editor.getChestTier(Editor.getLocationFromString(location));
		String tier = tierMang.getTierFromID(id);
		File tierFile = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
		YamlConfiguration tierConfig = YamlConfiguration.loadConfiguration(tierFile);
		Material material;
		if(tierConfig.getBoolean("invisible")) material = Material.BARRIER;
		else {
			material = Material.CHEST;
		}
		
		
		Editor.getLocationFromString(location).getBlock().setType(material);
		//Remove from regenDataManager
		chestTimes.remove(location);
		
	}
				
			}, delay);
			Long milliseconds = config.getLong(location);
			chestTimes.put(location, new MutablePair<Integer, Long>(taskID, (milliseconds + System.currentTimeMillis())));
		});
		file.delete();
	}
	
	/*
	 * Used in the onDisable. Adds chestTimes to data file
	 */
	public void copyRegenTimesToFile() {
		//Copy String and TIMELEFT to file!

		if(!regenDataFile.exists()) {
			try {
				regenDataFile.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
				return;
			}
		}
		YamlConfiguration config = YamlConfiguration.loadConfiguration(regenDataFile);
		chestTimes.entrySet().forEach(entry -> {
			config.set(entry.getKey(), (entry.getValue().right - System.currentTimeMillis()));
		});
		try {
			config.save(regenDataFile);	
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/*
	 * Gets the instance of chestTimes
	 */
	public HashMap<String, MutablePair<Integer, Long>> getChestTimes(){
		return chestTimes;
	}
	
}
