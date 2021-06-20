package net.minez2.CraftZChests.listener;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.LangManager;
import net.minez2.CraftZChests.data.TierManager;
import net.minez2.CraftZChests.sql.SQLEditor;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class BlockPlaceListener implements Listener {
	
	Main plugin = Main.getPlugin(Main.class);
	SQLEditor Editor = SQLEditor.getEditor();
	LangManager lmang = LangManager.getLmang();
	TierManager tierMang = TierManager.getTierMang();

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		
		Player player = event.getPlayer();
		
		//Prevent placing a block if a chest will regen there
		if(Editor.chestExists(event.getBlockPlaced().getLocation())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_chest_location_taken")));
			return;
		}
		
		if(event.getBlockPlaced().getType() == Material.CHEST) {
			
			//Check player permission first!
			if(player.hasPermission("cr.place")) {
				

			if(player.getInventory().getItemInMainHand().hasItemMeta()) {
				if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("CZC Regen Chest")) {
				if(player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
					if(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1).contains("Tier:")) {
						
						String tier = this.getTier(player.getInventory().getItemInMainHand().getItemMeta().getLore().get(1));
						if(!tierMang.isTier(tier)) {
							player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_tier_placed_not_exists")));
							event.setCancelled(true);
							return;
						}
						Editor.createChest(event.getBlockPlaced().getLocation(), 
								tier);
						if(isInvisible(tier)) event.getBlockPlaced().setType(Material.BARRIER);
						player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("new_chest_created") + tier));
					}
				}
				}
			}
			
		}
		}
		
		
	}
	
	private boolean isInvisible(String tier) {
		File tierFile = new File(plugin.getDataFolder().getPath() + File.separator + "Tiers" + File.separator + tier + ".yml");
		YamlConfiguration tierConfig = YamlConfiguration.loadConfiguration(tierFile);
		if(tierConfig.getBoolean("invisible")) return true;
		return false;
	}

	private String getTier(String lore) {
		String tier = "";
		
		tier = ChatColor.stripColor(lore).substring(6);		
		return tier;
	}
	

	
	
}
