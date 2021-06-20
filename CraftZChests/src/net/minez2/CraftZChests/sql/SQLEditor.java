package net.minez2.CraftZChests.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import net.minez2.CraftZChests.Main;
import net.minez2.CraftZChests.data.TierManager;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class SQLEditor {

	Main plugin = Main.getPlugin(Main.class);
	SQLSetup SQL = SQLSetup.getSetup();
	TierManager tierMang = TierManager.getTierMang();
	static SQLEditor Editor;
	
	/*
	 * Returns instance of this class used in this plugin
	 */
	public static SQLEditor getEditor() {
		if(Editor == null) {
			Editor = new SQLEditor();
		}
		return Editor;
	}
	
	/*
	 * Create table if it does not already exist
	 */
		public void createTable() {
			PreparedStatement ps; //Be sure to use JAVA Sql imports
			try {
				ps = SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.getConfig().getString("datastorage.tableprefix")
						+ "chestdata" + " (LOCATION VARCHAR(100),TIER VARCHAR(100),PRIMARY KEY (LOCATION))");
				ps.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		/*
		 * Returns T/F if chest location is in table already or not
		 */
		public boolean chestExists(Location loc) {
			try {
				//Select EVERYTHING (*) from table where UUID is equal
				PreparedStatement ps = SQL.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("datastorage.tableprefix")
						+ "chestdata" + " WHERE LOCATION=?");
				ps.setString(1, this.getStringFromLocation(loc));
				ResultSet results = ps.executeQuery();
				if(results.next()) {
					
					//chest is found - return true
					return true;
				}
				return false;
				
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		/*
		 * Creates chest in data table if it does not exist
		 */
		public void createChest(Location loc, String tier) {
			String id = tierMang.getIDFromTier(tier);
			try {
				if(!chestExists(loc)) {
					PreparedStatement ps2 = SQL.getConnection().prepareStatement("INSERT IGNORE INTO " + plugin.getConfig().getString("datastorage.tableprefix") + "chestdata" +  
				"(LOCATION,TIER) VALUES (?,?)");
					ps2.setString(1, this.getStringFromLocation(loc));
					ps2.setString(2, id);
					ps2.executeUpdate();
					return;
				}

			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * Deletes chest in data table
		 */
		public void deleteChest(Location loc) {
			if(!chestExists(loc)) return;
			
			try {
				PreparedStatement ps = SQL.getConnection().prepareStatement("DELETE FROM " + plugin.getConfig().getString("datastorage.tableprefix") + "chestdata" + 
			" WHERE LOCATION=?");
				ps.setString(1,  this.getStringFromLocation(loc));
				ps.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		
		/*
		 * Retrieves a chest's tier type. Returns null if not found
		 */
		public String getChestTier(Location loc) {
			try {
				PreparedStatement ps = SQL.getConnection().prepareStatement("SELECT TIER FROM " +
			plugin.getConfig().getString("datastorage.tableprefix") + "chestdata" +  " WHERE LOCATION=?");
				ps.setString(1, this.getStringFromLocation(loc));
				ResultSet results = ps.executeQuery();
				String tier = "";
				if(results.next()) {
					tier = results.getString("TIER");
					return tier;
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/*
		 * Gets all chests of a certain tier and deletes them
		 */
		public void deleteChestTier(String tier) {
			String id = tierMang.getIDFromTier(tier);
			
			try {
				PreparedStatement ps = SQL.getConnection().prepareStatement("SELECT * FROM " + plugin.getConfig().getString("datastorage.tableprefix") + "chestdata" +
						" WHERE TIER=?");
				ps.setString(1, id);
				ResultSet results = ps.executeQuery();
				while(results.next()) {
					Location loc = this.getLocationFromString(results.getString("LOCATION"));
					loc.getBlock().setType(Material.AIR);
				}
				ps = SQL.getConnection().prepareStatement("DELETE FROM " + plugin.getConfig().getString("datastorage.tableprefix") + "chestdata" +
						" WHERE TIER=?");
				ps.setString(1, id);
				ps.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		/*
		 * Returns an array of locations where regen chests are within a certain x/z radius of given location
		 * Usage: /cr destroyall and /cr regenall commands
		 */
		public List<Location> getNearbyChests(Location loc, int radius){
			
			List<Location> nearbyChests = new ArrayList<Location>();
			
			try {
				PreparedStatement ps = SQL.getConnection().prepareStatement("SELECT LOCATION FROM " + plugin.getConfig().getString("datastorage.tableprefix") + "chestdata");
				ResultSet results = ps.executeQuery();
				while(results.next()) {
					Location chestLoc = this.getLocationFromString(results.getString("LOCATION"));
					if((chestLoc.getBlockX() + radius) > loc.getBlockX() && chestLoc.getBlockX() <= loc.getBlockX() || (chestLoc.getBlockX() - radius) < loc.getBlockX() && chestLoc.getBlockX() >= loc.getBlockX()) {
						if((chestLoc.getBlockZ() + radius) > loc.getBlockZ() && chestLoc.getBlockZ() <= loc.getBlockZ()|| (chestLoc.getBlockZ() - radius) < loc.getBlockZ() && chestLoc.getBlockZ() >= loc.getBlockZ()) {
							nearbyChests.add(chestLoc);
						}
					}
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			
			return nearbyChests;
		}
		
		/*
		 * Returns an array of ALL chest locations
		 */
		public List<Location> getAllChests(){
			List<Location> allChests = new ArrayList<Location>();
			
			try {
				PreparedStatement ps = SQL.getConnection().prepareStatement("SELECT LOCATION FROM " + plugin.getConfig().getString("datastorage.tableprefix") + "chestdata");
				ResultSet results = ps.executeQuery();
				while(results.next()) {
					Location chestLoc = this.getLocationFromString(results.getString("LOCATION"));
					allChests.add(chestLoc);
		}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return allChests;
		}
		
		/*
		 * Returns an array of all chest locations of a specific tier
		 */
		public List<Location> getAllChestsOfTier(String tier){
			List<Location> allChestsofTier = new ArrayList<Location>();
			String id = tierMang.getIDFromTier(tier);
			
			try {
				PreparedStatement ps = SQL.getConnection().prepareStatement("SELECT LOCATION FROM " + plugin.getConfig().getString("datastorage.tableprefix") + "chestdata"
						+ " WHERE TIER=?");
				ps.setString(1, id);
				ResultSet results = ps.executeQuery();
				while(results.next()) {
					Location chestLoc = this.getLocationFromString(results.getString("LOCATION"));
					allChestsofTier.add(chestLoc);
		}
			} catch(SQLException e) {
				e.printStackTrace();
			}
			return allChestsofTier;
		}
		
		/*
		 * Create string from location
		 */
		public String getStringFromLocation(Location loc) {
			String s = "";
			s = s + "x" + loc.getBlockX();
			s = s + "y" + loc.getBlockY();
			s = s + "z" + loc.getBlockZ();
			s = s + "W" + loc.getWorld().getName();
			
			return s;
		}
		
		/*
		 * Create location from string. String is of the form x###y##z### where there are unknown amounts of numbers
		 */
		public Location getLocationFromString(String s) {
			Location loc = new Location(Bukkit.getWorld("world"), 0.0, 50.0, 0.0);
			char[] charString = s.toCharArray();
			
			String xCoord = "";
			int j = 1;
			//Get x coord
			for(int i = j; charString[j] != 'y'; ++j) {
				xCoord += charString[j];
			}
			int x = Integer.parseInt(xCoord);	
			loc.setX(x);
			
			String yCoord = "";
			++j;
			//Get y coord
			for(int i = j; charString[j] != 'z'; ++j) {
				yCoord += charString[j];
			}
			int y = Integer.parseInt(yCoord);	
			loc.setY(y);
			
			String zCoord = "";
			++j;
			//Get z coord
			for(int i = j; charString[j] != 'W'; ++j) {
				zCoord += charString[j];
			}
			int z = Integer.parseInt(zCoord);	
			loc.setZ(z);
			
			String name = s.substring(j + 1);
			World world = Bukkit.getWorld(name);
			if(world != null) loc.setWorld(world);
			
			return loc;
		}
	
}
