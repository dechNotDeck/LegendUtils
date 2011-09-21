package bitlegend.legendutils.Threads;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import bitlegend.legendutils.LegendUtils;

public class OnlineUsers implements Runnable {
	private final LegendUtils plugin;
	private String database = "";
	private String dbuser = "";
	private String dbpass = "";
	private String dbhost = "";
	private Connection connect;
	private Statement statement;
	private ResultSet rs;
	private Thread thread;
	
	public OnlineUsers(LegendUtils instance) {
		plugin = instance;
		database = plugin.config.readString("DB_Name");
		dbuser = plugin.config.readString("DB_User");
		dbpass = plugin.config.readString("DB_Pass");
		dbhost = plugin.config.readString("DB_Host");
		thread = new Thread(this, "userThread");
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public void checkNames() {
		Player[] playerArray = plugin.getServer().getOnlinePlayers();
		List<String> players = new ArrayList<String>();
		List<String> dbPlayers = new ArrayList<String>();
		for (int i = 0; i < playerArray.length; i++)
			players.add(playerArray[i].getDisplayName());
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connect = DriverManager.getConnection("jdbc:mysql://" + dbhost + "/" + database, dbuser, dbpass);
			statement = connect.createStatement();
			rs = statement.executeQuery("SELECT username FROM Players WHERE username='*';");
			while (rs.next()) {
				String name = rs.getString("username");
				if (players.contains(name)) {
					dbPlayers.add(name);
					System.out.println(name + " added to list of online users");
				}
			}
			@SuppressWarnings("unused")
			int execute = statement.executeUpdate("DELETE FROM Players WHERE username='*';");
			for (String s : dbPlayers)
				execute = statement.executeUpdate("INSERT INTO Players (username) values('" + s + "');");
			//System.out.println("Online users checked and fixed (if needed).");
		} catch (Exception e) {
			System.out.println("Unable to connect to database.");
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (statement != null)
					statement.close();
				if (connect != null)
					connect.close();
			} catch (Exception e) {
				System.out.println("Unable to close the connection.");
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			checkNames();
		}
	}
}
