package bitlegend.legendutils.Commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;

import bitlegend.legendutils.LegendUtils;

public class XpCleanup implements CommandExecutor {
	private final LegendUtils plugin;

	public XpCleanup(LegendUtils instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (plugin.getServer().getPluginManager().isPluginEnabled("Permissions")) {
			Player player = (Player) sender;
			if (plugin.permissionHandler.has(player, "dechutils.xpcleanup")) {
				System.out.println("Cleaning XP Orbs from worlds...");
				double count = 0;
				for (World w : plugin.getServer().getWorlds()) {
					for (Entity e : w.getEntities()) {
						if (e instanceof ExperienceOrb) {
							count++;
							e.remove();
						}
					}
				}
				System.out.println("Removed " + (int)count + " XP Orbs");
				player.sendMessage("Removed " + (int)count + " XP Orbs");
				return true;
			}
		}
		return false;
	}
}
