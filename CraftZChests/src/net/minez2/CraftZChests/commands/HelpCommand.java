package net.minez2.CraftZChests.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.LangManager;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class HelpCommand implements CommandExecutor {
	
	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	
	private String[] helpMessages = {"&9&lCraftZChests by &3kwilk &9Help Menu (1/2)"
			+ "\n&bCurrently running v&3" + plugin.getDescription().getVersion() + 
			"\n&b&lSupport Discord: &bhttps://discord.gg/9WBfs7Wc52\n " +
			"\n&b/cr help <page> - &3displays the help menu.\n&b/cr reload - &3Reloads the config and lang files."
			+ "\n&b/cr list <tiers/chests> - &3Shows some current info tiers available or placed chests.\n&b/cr get <tier> <amount> - "
			+ "&3replaces hand with chest of that tier ",
			
			"&9&lCraftZChests by &3kwilk &9Help Menu (2/2)" + "\n&b\n&b/cr near <radius> - &3See all chest locations near you (including ones in regen)."
					+ "\n&b/cr regen <radius> - &3Regenerates all broken chests within radius.\n&b/cr destroy <radius> confirm delete - "
					+ "&3permanently deletes all chests within radius \n&b/cr regenall confirm - &3Regenerates ALL chests in the database. Useful after crashes.\n"
					+ "&b/cr create <tier> - &3Opens GUI to create new tier.\n&b/cr delete <tier> - &3Deletes a tier's file AND all chest locations.\n&b/cr rename "
					+ "<oldname> <newname> - &3Renames a tier to the new name and updates filename."};
					
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("cr.help")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("no_permission")));
				return true;
			}
		}
		
		//Send the message
		if(args.length == 1) { // /cr help (page 1)
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getHelpMessage(1)));
			return true;
		}
		//Check integer argument
		int page;
		try{
			page = Integer.parseInt(args[1]);
		} catch(Exception e) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_help_usage")));
			return true;
		}
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getHelpMessage(page)));
		
		
		return false;
	}
	
	private String getHelpMessage(int page) {
		if(page > helpMessages.length - 1) page = 2;
		if(page <= 0) page = 1;
		return helpMessages[page - 1];
		
	}

}
