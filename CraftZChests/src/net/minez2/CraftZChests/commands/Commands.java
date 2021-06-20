package net.minez2.CraftZChests.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.TierManager;
import net.minez2.CraftZChests.sql.SQLEditor;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class Commands implements CommandExecutor {
	
	Main plugin = Main.getPlugin(Main.class);
	SQLEditor Editor = SQLEditor.getEditor();
	TierManager tierMang = TierManager.getTierMang();


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "/cr help <page> for help");
			return true;
		}
		
		if(args[0].equalsIgnoreCase("help")) {
			new HelpCommand().onCommand(sender, cmd, label, args);
			return true;
		}
		
		
		if(args[0].equalsIgnoreCase("create")) {
			new CreateCommand().onCommand(sender, cmd, label, args);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("delete")) {
			new DeleteCommand().onCommand(sender, cmd, label, args);
			return true;			
		}
		
		if(args[0].equalsIgnoreCase("get")) {
			new GetCommand().onCommand(sender, cmd, label, args);
			return true;			
		}
		
		if(args[0].equalsIgnoreCase("rename")) {
			new RenameCommand().onCommand(sender, cmd, label, args);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("reload")) {
			new ReloadCommand().onCommand(sender, cmd, label, args);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("regen")) {
			if(plugin.getVersion().contains("1_12") || plugin.getVersion().contains("1_13")) new RegenCommand1_13().onCommand(sender, cmd, label, args);
			else {
				new RegenCommand1_14().onCommand(sender, cmd, label, args);
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("regenall")) {
			if(plugin.getVersion().contains("1_12") || plugin.getVersion().contains("1_13")) new RegenAllCommand1_13().onCommand(sender, cmd, label, args);
			else {
				new RegenAllCommand1_14().onCommand(sender, cmd, label, args);
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("destroy")) {
			new DestroyCommand().onCommand(sender, cmd, label, args);
			return true;
		}
		
		if(args[0].equalsIgnoreCase("list")) {
			new ListCommand().onCommand(sender, cmd, label, args);
			return true;			
		}
		
		if(args[0].equalsIgnoreCase("near")) {
			new NearCommand().onCommand(sender, cmd, label, args);
			return true;
		}
		
		
			sender.sendMessage(ChatColor.RED + "/cr help <page> for help");
		
		
		
		return false;
	}

}
