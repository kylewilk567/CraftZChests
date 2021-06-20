package net.minez2.CraftZChests.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

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

public class InventoryCloseListener1_12 implements Listener {
	
	Main plugin = Main.getPlugin(Main.class);
	LangManager lmang = LangManager.getLmang();
	TierManager tierMang = TierManager.getTierMang();
	TierEditorInventory tierEditor = TierEditorInventory.getTierInven();
	
	@EventHandler
	public void onInvenClose(InventoryCloseEvent event) {
		
		if(ChatColor.stripColor(event.getView().getTitle()).contains("CZC Tier Editor")) {
			String tier = ChatColor.stripColor(event.getView().getTitle()).substring(23);
			if(!tierMang.isTier(tier)) {
				tierMang.createTier(tier);
				tierEditor.updateContents(tier, event.getInventory().getContents());
				event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("tier_created") + tier));
			}
		}
	}
	

}
