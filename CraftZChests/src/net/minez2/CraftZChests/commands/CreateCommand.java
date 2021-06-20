package net.minez2.CraftZChests.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.LangManager;
import net.minez2.CraftZChests.data.TierManager;
import net.minez2.CraftZChests.util.TierEditorInventory;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class CreateCommand implements CommandExecutor {
	
	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	TierEditorInventory tierEditor = new TierEditorInventory();
	TierManager tierMang = TierManager.getTierMang();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("player_only_cmd")));
			return true;
		}
		
		Player player = (Player) sender;
		if(!player.hasPermission("cr.create")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("no_permission")));
			return true;
		}
		
		if(args.length == 1 || args.length > 2) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cr create <tier>"));
			return true;
		}
		
		//Check tier argument
		if(tierMang.isTier(args[1])) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_tier_exists")));
			return true;
		}
		
		Inventory inv = tierEditor.createTierGUI(args[1]);
		player.openInventory(inv);
		
		
		
		return false;
	}

}
