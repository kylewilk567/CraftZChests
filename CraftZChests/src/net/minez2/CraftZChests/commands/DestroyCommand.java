package net.minez2.CraftZChests.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.LangManager;
import net.minez2.CraftZChests.data.RegenDataManager1_13;
import net.minez2.CraftZChests.data.TierManager;
import net.minez2.CraftZChests.sql.SQLEditor;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class DestroyCommand implements CommandExecutor {

	
	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	SQLEditor Editor = SQLEditor.getEditor();
	RegenDataManager1_13 regenMang = RegenDataManager1_13.getRegenMang();
	TierManager tierMang = TierManager.getTierMang();
					
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("player_only_cmd")));
			return true;
		}
		
		//check player perms
		Player player = (Player) sender;
		if(!player.hasPermission("cr.destroy")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("no_permission")));
			return true;
		}
		
		//Check args length
		if(args.length < 4) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cr destroy <radius> confirm delete"));
			return true;
		}
		
		if(args.length > 4 || !args[2].equalsIgnoreCase("confirm") || !args[3].equalsIgnoreCase("delete")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cr destroy <radius> confirm delete"));
			return true;
		}
		
		//Check integer argument
		int radius;
		try {
			radius = Integer.parseInt(args[1]);
		} catch(Exception e) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_not_a_number")));
			return true;
		}
		
		//Limit to 128 block radius to prevent crash
		if(radius > 128) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_radius_too_large")));
			return true;
		}
		
		List<Location> locs = Editor.getNearbyChests(player.getLocation(), radius);
		for(Location loc : locs) {
			//If regenMang contains key, remove from hashmap and destroy the chest now.
			//Else just destroy the chest
			if(regenMang.getChestTimes().containsKey(Editor.getStringFromLocation(loc))) {
				Bukkit.getScheduler().cancelTask(regenMang.getChestTimes().get(Editor.getStringFromLocation(loc)).left);
				loc.getBlock().setType(Material.AIR);			
				regenMang.getChestTimes().remove(Editor.getStringFromLocation(loc));
				Editor.deleteChest(loc);
			}
			else {
				Editor.deleteChest(loc);
				loc.getBlock().setType(Material.AIR);
			}
		}
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("chests_destroy_radius_success")));
		
		
		return false;
	}
	
}
