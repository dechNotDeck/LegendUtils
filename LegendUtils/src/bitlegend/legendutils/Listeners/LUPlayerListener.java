package bitlegend.legendutils.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

	public LUPlayerListener(LegendUtils instance) {
		plugin = instance;
		database = plugin.config.readString("DB_Name");
		dbuser = plugin.config.readString("DB_User");
		dbpass = plugin.config.readString("DB_Pass");
	}

	public void onPlayerLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection("jdbc:mysql://bitlegend.com/" + database, dbuser, dbpass);
			statement = connect.createStatement();
			//System.out.println("INSERT INTO Players (username) values('" + player.getDisplayName() + "');");
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

	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection("jdbc:mysql://bitlegend.com/" + database, dbuser, dbpass);
			statement = connect.createStatement();
			int playerId = player.getEntityId();
			String name = "";
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				//I do this because now that the player is logged in
				//returning player.getDisplayName() will also include
				//color codes.
				if (p.getEntityId() == playerId) name = p.getName();
			}
			//System.out.println("DELETE FROM Players WHERE username = '" + name + "';");
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