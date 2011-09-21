/*
 * ToggleInventory.java 
 * Cirn9 9/20/2011
 * Last updated: 9/20/2011
 * Allows a moderator to toggle between his moderator and player inventories
 */
package bitlegend.legendutils.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import bitlegend.legendutils.LegendUtils;

public class ToggleInventory implements CommandExecutor {
	private final LegendUtils plugin;
	public ToggleInventory(LegendUtils i){
		plugin = i;
	}
	
	private boolean hasPermission(Player player){
		if(plugin.permissionHandler.has(player, "legendutils.commands.toggleinventory") || plugin.permissionHandler.has(player, "legendutils.commands.*") || plugin.permissionHandler.has(player, "legendutils.*")){
			return true;
		}
		return false;
	}
				
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		if (plugin.getServer().getPluginManager().isPluginEnabled("Permissions")) {
			Player p = (Player) sender;
			if (hasPermission(p)) {
				// /minv/username - contains 0 or 1, determining current state of player's inventory
				// /minv/username.mod - contains moderator inventory information
				// /minv/username.pla - contains moderator inventory information
				
				//Get the current player's name
				@SuppressWarnings("unused")
				String pn = p.getName();
				
				
				
				//check if 
				
				return true;
			}
		}
		return false;
	}
}
