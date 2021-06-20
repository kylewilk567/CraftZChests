package net.minez2.CraftZChests.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.ConfigManager;
import net.minez2.CraftZChests.data.LangManager;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class ReloadCommand implements CommandExecutor {

	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	ConfigManager configMang = ConfigManager.getConfigMang();
					
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(!player.hasPermission("cr.reload")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("no_permission")));
				return true;
			}
		}
		
		//Reload the config
		configMang.reloadConfig();
		
		//Reload the lang file
		lmang.reloadEng();
		
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("config_reloaded")));
		
		return false;
	}
	
}
