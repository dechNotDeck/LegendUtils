package bitlegend.legendutils;

import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import bitlegend.legendutils.Listeners.*;
import bitlegend.legendutils.NPC.Enderman;
import bitlegend.legendutils.Commands.*;
import bitlegend.legendutils.Threads.*;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class LegendUtils extends JavaPlugin {
	public PermissionHandler permissionHandler;
	public String enableOnStart = "Enabled On Startup";
	public boolean enabled;
	public Config config = new Config(this);
	
	private final LUPlayerListener playerListener = new LUPlayerListener(this);
	private final LUEntityListener entityListener = new LUEntityListener(this);

	@Override
	public void onDisable() {
		// Cleanup
		playerListener.clearPlayerList();
		PluginDescriptionFile pdfFile = this.getDescription();
		
		// Exit message
		System.out.println(pdfFile.getName() + " has been disabled.");
	}

	@Override
	public void onEnable() {
		// Database, file cleanup
		playerListener.clearPlayerList();

		// Register listeners
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_LOGIN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
		
		// Register commands
		getCommand("ti").setExecutor(new ToggleInventory(this));
		getCommand("xpcleanup").setExecutor(new XpCleanup(this));
		
		// Setup configs, databases
		config.configCheck();
		PluginDescriptionFile pdfFile = this.getDescription();
		
		// Threads
		Thread checkUsers = new OnlineUsers(this).getThread();
		if (config.readBoolean("Debug_Mode") == false)
			checkUsers.start();
		
		// Startup message
		System.out.println(pdfFile.getName() + " " + pdfFile.getVersion() + " started!");
		if (Enderman.modifyBlocks(this))
			System.out.println(pdfFile.getName() + ": Enderman nerfing enabled.");
		else
			System.out.println(pdfFile.getName() + ": Could not get enderman declared field!");
		if (setupPermissions())
			System.out.println(pdfFile.getName() + ": Found and will use plugin " + ((Permissions)
					this.getServer().getPluginManager().getPlugin("Permissions")).getDescription().getFullName());
		else
			System.out.println(pdfFile.getName() + ": Permission system not detected, defaulting to OP");
	}

	private boolean setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
		if (permissionsPlugin == null) {
			return false;
		}
		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		return true;
	}
}