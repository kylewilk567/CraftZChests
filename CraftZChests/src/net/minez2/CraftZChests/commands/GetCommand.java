package net.minez2.CraftZChests.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.LangManager;
import net.minez2.CraftZChests.data.TierManager;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class GetCommand implements CommandExecutor {
	
	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
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
		
		if(args.length == 1 || args.length > 3) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /cr get <tier> <amount>"));
			return true;
		}
		
		//Check tier argument
		if(!tierMang.isTier(args[1])) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_tier_not_exists")));
			return true;
		}
		
		//Check integer if specified
		int amount = 1;
		if(args.length == 3) {

			try {
				amount = Integer.parseInt(args[2]);
				if(amount > 64) amount = 64;
			} catch(Exception e) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_not_a_number")));
				return true;
			}			
		}
		
		//Check inventory space
		if(player.getInventory().firstEmpty() == -1) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("error_inventory_full")));
			return true;
		}
		
		//Give player the chest for tier specified
		player.getInventory().setItem(player.getInventory().firstEmpty(), this.getChest(args[1], amount));
		
		
		
		
		return false;
	}

	private ItemStack getChest(String tier, int amount) {
		
		ItemStack chest = new ItemStack(Material.CHEST, amount);
		ItemMeta meta = chest.getItemMeta();
		List<String> lore = new ArrayList<String>();
		lore.add(" ");
		lore.add(ChatColor.AQUA + "Tier: " + ChatColor.DARK_AQUA + tier);
		meta.setDisplayName(ChatColor.RED + "CZC Regen Chest");
		meta.setLore(lore);
		meta.addEnchant(Enchantment.DURABILITY, 1, false);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		chest.setItemMeta(meta);
		
		return chest;
		
	}

}
