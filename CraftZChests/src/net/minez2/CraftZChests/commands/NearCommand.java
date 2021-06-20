package net.minez2.CraftZChests.commands;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
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

public class NearCommand  implements CommandExecutor {
	
	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	SQLEditor Editor = SQLEditor.getEditor();
	TierManager tierMang = TierManager.getTierMang();
					
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("player_only_cmd")));
			return true;
		}
		
		//check player perms
		Player player = (Player) sender;
		if(!player.hasPermission("cr.near")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("no_permission")));
			return true;
		}
		
		//Check args length
		if(args.length > 2) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cr near <radius>"));
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
		
		
		List<Location> locs = Editor.getNearbyChests(player.getLocation(), radius);
		String message = "&9&nNearby Chests: &b\n ";
		for(Location loc : locs) {
			//Get the coordinates of the chest
			int x = loc.getBlockX();
			int y = loc.getBlockY();
			int z = loc.getBlockZ();
			
			message += "\n&bX: " + x + " Y: " + y + " Z: " + z;
			
		}
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		
		
		return false;
	}
}
