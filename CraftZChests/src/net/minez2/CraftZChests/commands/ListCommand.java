package net.minez2.CraftZChests.commands;

import java.util.List;

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

public class ListCommand implements CommandExecutor {

	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	SQLEditor Editor = SQLEditor.getEditor();
	TierManager tierMang = TierManager.getTierMang();
					
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("cr.list")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("no_permission")));
				return true;
			}
		}
		
		
		//Check args length
		if(args.length != 2) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cr list <'tiers'/'chests'>"));
			return true;
		}
		
		//Check which command we will be running (chests or tiers)
		if(!args[1].equalsIgnoreCase("tiers") && !args[1].equalsIgnoreCase("chests")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cr list <'tiers'/'chests'>"));
			return true;
		}
		
		//Run tiers command
		if(args[1].equalsIgnoreCase("tiers")) {
			List<String> tiers = tierMang.getAllTiers();
			String message = "&9&nList of Current Tiers:\n&b ";
			if(tiers != null) {
				int i = 1;
				for(String tier : tiers) {
					message += "\n&b" + i + ". &3" + tier;
					++i;
				}
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			return true;
		}
		
		//Run chests command
		if(args[1].equalsIgnoreCase("chests")) {
			int numChests = Editor.getAllChests().size();
			String message = "&9&nChest Regen Info:\n&b ";
			message += "\n&bTotal: &3" + numChests;
			List<String> tiers = tierMang.getAllTiers();
			if(tiers != null) {
				for(String tier : tiers) {
					message += "\n&b" + tier + ": &3" + Editor.getAllChestsOfTier(tier).size() + " chests";
				}
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			return true;
		}
		
		
		return false;
	}
	
}
