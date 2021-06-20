package net.minez2.CraftZChests.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.minez2.CraftZChests.Main;

/*
 * INTELLECTUAL PROPERTY NOTICE!!!
 * 
 * The following code is the property of kwilk. 
 * No one may take, modify, or share the following code without
 * the consent of kwilk under the license of this plugin.
 */

public class SQLSetup {
	
	Main plugin = Main.getPlugin(Main.class);
	static SQLSetup SQLSet;
	

	private String host = plugin.getConfig().getString("datastorage.host");
	private int port = plugin.getConfig().getInt("datastorage.port");
	private String database = plugin.getConfig().getString("datastorage.database");
	private String username = plugin.getConfig().getString("datastorage.username");
	private String password = plugin.getConfig().getString("datastorage.password");
	
	private Connection connection;
	
	
	/*
	 * Returns instance of this class used in this plugin
	 */
	public static SQLSetup getSetup() {
		if(SQLSet == null) {
			SQLSet = new SQLSetup();
		}
		return SQLSet;
	}
	/*
	 * Returns T/F if database is connected
	 */
	public boolean isConnected() {
		return (connection == null ? false : true);
	}
	
	/*
	 * Connects database
	 */
	public void connect() throws ClassNotFoundException, SQLException {
		if(!isConnected()) {
			connection = DriverManager.getConnection("jdbc:mysql://" +
					host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false", username, password);
		}
	}
	/*
	 * Disconnects from database
	 */
	public void disconnect() {
		if(isConnected()) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Gets the connection
	 */
	public Connection getConnection() {
		return connection;
	}

}
