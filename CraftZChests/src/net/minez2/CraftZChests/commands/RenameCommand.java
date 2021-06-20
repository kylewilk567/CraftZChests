package net.minez2.CraftZChests.commands;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.LangManager;
import net.minez2.CraftZChests.data.TierManager;

public class RenameCommand implements CommandExecutor {
	
	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	TierManager tierMang = TierManager.getTierMang();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("cr.rename")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("no_permission")));
				return true;
			}
		}
		

		
		if(args.length != 3) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cr rename <oldTierName> <newTierName>"));
			return true;
		}
		
		//Check first tier argument
		if(!tierMang.isTier(args[1])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_tier_not_exists")));
			return true;
		}
		
		//Check second tier argument
		if(tierMang.isTier(args[2])) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_tier_exists")));
			return true;
		}
		
		//Rename the file
		tierMang.renameTier(args[1], args[2]);
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("tier_renamed") + args[2]));
		
		
		
		return false;
	}

}
