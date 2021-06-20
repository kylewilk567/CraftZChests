package net.minez2.CraftZChests.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.minez2.CraftZChests.data.LangManager;
import net.minez2.CraftZChests.sql.SQLEditor;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class BlockBreakListener implements Listener {
	
	SQLEditor Editor = SQLEditor.getEditor();
	LangManager lmang = LangManager.getLmang();
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		
		Player player = event.getPlayer();
		
		//Cancel event if it is a chest and NOT crouched!
		if(!player.isSneaking() && Editor.chestExists(event.getBlock().getLocation())) {
			event.setCancelled(true);
			return;
		}
		
		
		//Delete the chest if player has perms and is crouching
		if(player.hasPermission("cr.destroy")) {
		try {
		if(Editor.chestExists(event.getBlock().getLocation())) {
			Editor.deleteChest(event.getBlock().getLocation());
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("chest_destroyed")));
		}
		} catch(Exception e) {
			
		}
	}
	}

}
