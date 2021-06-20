package net.minez2.CraftZChests;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import net.minez2.CraftZChests.commands.Commands;
import net.minez2.CraftZChests.data.ConfigManager;
import net.minez2.CraftZChests.data.LangManager;
import net.minez2.CraftZChests.data.RegenDataManager1_13;
import net.minez2.CraftZChests.data.RegenDataManager1_14;
import net.minez2.CraftZChests.data.TierManager;
import net.minez2.CraftZChests.listener.BlockBreakListener;
import net.minez2.CraftZChests.listener.BlockPlaceListener;
import net.minez2.CraftZChests.listener.InventoryCloseListener1_12;
import net.minez2.CraftZChests.listener.InventoryCloseListener1_13;
import net.minez2.CraftZChests.listener.RightClickListener1_13;
import net.minez2.CraftZChests.listener.RightClickListener1_14;
import net.minez2.CraftZChests.sql.SQLEditor;
import net.minez2.CraftZChests.sql.SQLSetup;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class Main extends JavaPlugin {
	
	@Override
	public void onEnable() {
		
		//Check version
		if(!this.getVersion().contains("1_12") && !this.getVersion().contains("1_13") && !this.getVersion().contains("1_14") &&
				!this.getVersion().contains("1_15") && !this.getVersion().contains("1_16")) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "CraftZChests Disabled! Server version must be 1.12-1.16 ONLY!");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
        new UpdateChecker(this, 91592).getVersion(version -> {
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                Bukkit.getConsoleSender().sendMessage("[CraftZChests] You are using the latest version available for CraftZChests.");
            } else {
                Bukkit.getConsoleSender().sendMessage("[CraftZChests] There is a new update available for CraftZChests!");
            }
        });
		
		//Register events
		if(this.getVersion().contains("1_12") || this.getVersion().contains("1_13")) {
		Bukkit.getServer().getPluginManager().registerEvents(new RightClickListener1_13(), this);
		}
		else {
			Bukkit.getServer().getPluginManager().registerEvents(new RightClickListener1_14(), this);
		}
		Bukkit.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
		
		if(this.getVersion().contains("1_12")) {
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryCloseListener1_12(), this);
		}
		else {
			Bukkit.getServer().getPluginManager().registerEvents(new InventoryCloseListener1_13(), this);
		}

		this.getCommand("chestregen").setExecutor(new Commands());
		
		
		//Load files
		this.saveDefaultConfig();
		LangManager lmang = new LangManager();
		lmang.setupLang();
		ConfigManager configMang = ConfigManager.getConfigMang();
		configMang.setupConfig();
		TierManager tierMang = TierManager.getTierMang();
		tierMang.createTierFolder();
		
		//Setup MySQL
		SQLSetup SQL = SQLSetup.getSetup();
		SQLEditor Editor = SQLEditor.getEditor();
		try {
			SQL.connect();
		} catch (ClassNotFoundException | SQLException e) {
			//e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Database is not connected. Check datastorage section in config.yml. Plugin disabled");
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		if(SQL.isConnected()) {
			Bukkit.getLogger().info("Database is connected!");
			Editor.createTable();
		}
		
		//Schedule timers from file
		if(this.getVersion().contains("1_12") || this.getVersion().contains("1_13")) {
		RegenDataManager1_13 regenMang = RegenDataManager1_13.getRegenMang();
		regenMang.rescheduleTimers();
		}
		else {
			RegenDataManager1_14 regenMang = RegenDataManager1_14.getRegenMang();
			regenMang.rescheduleTimers();
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', lmang.getMessage("plugin_enabled")));
		
	}

	@Override
	public void onDisable() {
		
		if(this.getVersion().contains("1_12") || this.getVersion().contains("1_13")) {
		RegenDataManager1_13 regenMang = RegenDataManager1_13.getRegenMang();
		regenMang.copyRegenTimesToFile();
		}
		else {
			RegenDataManager1_14 regenMang = RegenDataManager1_14.getRegenMang();
			regenMang.copyRegenTimesToFile();
		}
		

		
	}
	
	public String getVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}


}
