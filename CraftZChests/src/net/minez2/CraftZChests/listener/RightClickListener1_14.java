package net.minez2.CraftZChests.listener;

import java.io.File;
import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.mojang.datafixers.util.Pair;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.ConfigManager;
import net.minez2.CraftZChests.data.RegenDataManager1_14;
import net.minez2.CraftZChests.data.TierManager;
import net.minez2.CraftZChests.sql.SQLEditor;
import net.minez2.CraftZChests.util.CustomLootTable1_12;
import net.minez2.CraftZChests.util.CustomLootTable1_13;
import net.minez2.CraftZChests.util.ParticleShapes;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class RightClickListener1_14 implements Listener {
	
	Main plugin = Main.getPlugin(Main.class);
	
	SQLEditor Editor = SQLEditor.getEditor();
	TierManager tierMang = TierManager.getTierMang();
	ConfigManager configMang = ConfigManager.getConfigMang();
	RegenDataManager1_14 regenMang = RegenDataManager1_14.getRegenMang();
	
	@EventHandler
	public void onRightClick(PlayerInteractEvent event) {
		
		Player player = event.getPlayer();
		if(event.getClickedBlock() == null || event.getClickedBlock().getType() == null) return;
		if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if(event.getHand().equals(EquipmentSlot.HAND)) { //Prevent two events from firing
				if(event.getClickedBlock().getType().equals(Material.BARRIER) || event.getClickedBlock().getType().equals(Material.CHEST)){
					
					//Check if chest/barrier is a regen chest
					if(Editor.chestExists(event.getClickedBlock().getLocation())) {
						
						//******Find and drop loot here!*****
						Location loc = event.getClickedBlock().getLocation();
						
						//Get chest tier from id
						String id = Editor.getChestTier(loc);
						String tier = tierMang.getTierFromID(id);
						
						//Check version for loottable
						String version = plugin.getVersion();
						Collection<ItemStack> items;
						if(version.contains("1_12")) items = new CustomLootTable1_12().populateLoot(new Random(), tier); //Must come before next step
						else {
							items = new CustomLootTable1_13().populateLoot(new Random(), tier);
						}
						loc.setY(loc.getY() + 1);
						for(ItemStack item : items) {
							if(item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("CZCcmd-")) {
								String path = item.getItemMeta().getDisplayName().substring(7);
								File file = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
								YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
								String command = config.getString(path);
								if(command.contains("{player}")) {
									command = command.replace("{player}", player.getName());
								}
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
								continue;
							}
							event.getClickedBlock().getWorld().dropItemNaturally(loc, item);
						}
						

					
						long delay = this.getRegenTimeDelay(tier) * 20L;

						//Set chest to be regenerated
						Material type = event.getClickedBlock().getType();
						this.scheduleRegenTask(type, event.getClickedBlock().getLocation(), delay);
/*						int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

							@Override
							public void run() {
								
								//Reset the type
								event.getClickedBlock().setType(type);
								//Remove from regenDataManager
								regenMang.getChestTimes().remove(Editor.getStringFromLocation(event.getClickedBlock().getLocation()));
							}
							
						}, delay);
						
						//Add chest to regenDataManager
						regenMang.getChestTimes().put(Editor.getStringFromLocation(event.getClickedBlock().getLocation()), new MutablePair<Integer, Long>(taskID, (System.currentTimeMillis() + 50*delay)));
						*/
						
						File tierFile = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
						YamlConfiguration config = YamlConfiguration.loadConfiguration(tierFile);
						
						//Play sound effect
						if(configMang.getConfig().getBoolean("sounds.enabled") && config.getBoolean("sound.enabled")) {
							//Get sound
							String sound = "";
							if(config.getString("sound.effect").equalsIgnoreCase("default")) sound = configMang.getConfig().getString("sounds.default_effect");
							else {
								sound = config.getString("sound.effect");
							}
							player.getWorld().playSound(player.getLocation(), Sound.valueOf(sound), 1.0F, 1.0F);
						}
							
						//Do particles
						if(configMang.getConfig().getBoolean("particles.enabled") && config.getBoolean("particles.enabled")) {
							//Get particle
							String particle = "";
							if(config.getString("particles.effect").equalsIgnoreCase("default")) particle = configMang.getConfig().getString("particles.default_particle");
							else {
								particle = config.getString("particles.effect");
							}
							//Play particle with desired shape
							ParticleShapes particlePlayer = new ParticleShapes();
							particlePlayer.playParticleShape(particle, config.getString("particles.shape"), event.getClickedBlock().getLocation());
						}
						
						//Set block type air
						event.getClickedBlock().setType(Material.AIR);
						
					}
					
				} 
			}
			
		}
		
	}
	
	private Long getRegenTimeDelay(String tier) {
		File tierFile = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(tierFile);
		double maxSeconds = config.getDouble("max_regen_seconds");
		double minSeconds = config.getDouble("min_regen_seconds");
		
		Long delay = Math.round(((Math.random() * (maxSeconds - minSeconds)) + minSeconds));
		
		
		return delay;
	}
	/*
	 * Schedules the task AND reschedules if respawn conditions aren't met. EXAMPLE OF RECURSION IN JAVA
	 */
	private int scheduleRegenTask(Material type, Location loc, long delay) {

		
		int taskID = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			

			@Override
			public void run() {
				if(configMang.getConfig().getBoolean("respawn_conditions.player_nearby.enabled")) {
					int radius = configMang.getConfig().getInt("respawn_conditions.player_nearby.radius");
					boolean containsPlayer = false;
					for(Entity entity : loc.getWorld().getNearbyEntities(loc, radius, radius, radius)) {
						if(entity.getType() == EntityType.PLAYER) containsPlayer = true;
					}
					if(containsPlayer) {
						regenMang.getChestTimes().put(Editor.getStringFromLocation(loc), new Pair<Integer, Long>(scheduleRegenTask(type, loc, 1200L), (System.currentTimeMillis() + 50*delay)));
						return;
					}
				}

				//Reset the type
				String id = Editor.getChestTier(loc);
				String tier = tierMang.getTierFromID(id);
				File tierFile = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
				YamlConfiguration tierConfig = YamlConfiguration.loadConfiguration(tierFile);
				Material material;
				if(tierConfig.getBoolean("invisible")) material = Material.BARRIER;
				else {
					material = Material.CHEST;
				}
				loc.getBlock().setType(material);
				//Remove from regenDataManager
				regenMang.getChestTimes().remove(Editor.getStringFromLocation(loc));
			}
			
		}, delay);
		
		regenMang.getChestTimes().put(Editor.getStringFromLocation(loc), new Pair<Integer, Long>(taskID, (System.currentTimeMillis() + 50*delay)));
		return taskID;
	}

}
