package net.minez2.CraftZChests.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

public class DeleteCommand implements CommandExecutor {

	LangManager lmang = LangManager.getLmang();
	TierManager tierMang = TierManager.getTierMang();
	SQLEditor Editor = SQLEditor.getEditor();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("player_only_cmd")));
			return true;
		}
		
		Player player = (Player) sender;
		if(!player.hasPermission("cr.delete")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("no_permission")));
			return true;
		}
		
		if(args.length == 1 || args.length > 2) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cr delete <tier>"));
			return true;
		}
		
		//Check tier argument
		if(!tierMang.isTier(args[1])) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_tier_not_exists")));
			return true;
		}
		
		//Delete all instances of the tier in the world and remove from database
		Editor.deleteChestTier(args[1]);
		
		//Delete this tier's file
		tierMang.deleteTierFile(args[1]);
		
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("tier_deleted") + args[1]));
		
		
		
		return false;
	}
	
}
