package bitlegend.legendutils.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import bitlegend.legendutils.LegendUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class LUPlayerListener extends PlayerListener {
	private final LegendUtils plugin;
	private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	private String database = "";
	private String dbuser = "";
	private String dbpass = "";
	private String dbhost = "";

	public LUPlayerListener(LegendUtils instance) {
		plugin = instance;
		database = plugin.config.readString("DB_Name");
		dbuser = plugin.config.readString("DB_User");
		dbpass = plugin.config.readString("DB_Pass");
		dbhost = plugin.config.readString("DB_Host");
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		if (plugin.config.readBoolean("Debug_Mode") == true) // Remove me
			return;
		Player player = event.getPlayer();
		
		if (!checkForPlayer(player)) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connect = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + database, dbuser, dbpass);
				statement = connect.createStatement();
				@SuppressWarnings("unused")
				int result = statement.executeUpdate("INSERT INTO Players (username) values('" + player.getDisplayName() + "');");
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				try {
					if (resultSet != null)
						resultSet.close();
					if (statement != null)
						statement.close();
					if (connect != null)
						connect.close();
				} catch (Exception e) {

				}
			}
		}
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		if (plugin.config.readBoolean("Debug_Mode") == true) // Remove me
			return;
		Player player = event.getPlayer();
		if (checkForPlayer(player)) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connect = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + database, dbuser, dbpass);
				statement = connect.createStatement();
				int playerId = player.getEntityId();
				String name = "";
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					//I do this because now that the player is logged in
					//returning player.getDisplayName() will also include
					//color codes.
					if (p.getEntityId() == playerId) name = p.getName();
				}
				@SuppressWarnings("unused")
				int result = statement.executeUpdate("DELETE FROM Players WHERE username = '" + name + "';");
			} catch (Exception e) {
				System.out.println(e);
			} finally {
				try {
					if (resultSet != null)
						resultSet.close();
					if (statement != null)
						statement.close();
					if (connect != null)
						connect.close();
				} catch (Exception e) {

				}
			}
		}
	}
	
	private boolean checkForPlayer(Player player) {
		if (plugin.config.readBoolean("Debug_Mode") == true) // Remove me
			return false;
		else {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				connect = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + database, dbuser, dbpass);
				statement = connect.createStatement();
				ResultSet rs = statement.executeQuery("SELECT username FROM Players WHERE username='" + player.getDisplayName() + "';");
				int count = 0;
				while (rs.next())
					count++;
				if (count > 0)
					return true;
				else
					return false;
			} catch (Exception e) {
				System.out.println("Unable to connect to database.");
			}
			return false;
		}
	}
	
	public int clearPlayerList() {
		if (plugin.config.readBoolean("Debug_Mode") == false) // Remove me
			return 0;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + database, dbuser, dbpass);
			statement = connect.createStatement();
			int result = statement.executeUpdate("DELETE FROM Players WHERE username='*';");
			return result;
		} catch (Exception e) {
			return 0;
		}
	}
}