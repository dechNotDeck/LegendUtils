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
	private final String[] permissions = {
				"legendutils.commands.xpcleanup", 
				"legendutils.commands.*",
				"legendutils.*"
			};

	public XpCleanup(LegendUtils instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		boolean r = false;
		if ((plugin.getServer().getPluginManager().isPluginEnabled("Permissions") || (sender.isOp()))) {
			Player player = (Player) sender;
			if ((hasPermission(player, permissions) || player.isOp())) {
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
				r = true;
			}
		}
		return r;
	}
	
	private boolean hasPermission(Player player, String[] perms) {
		boolean r = false;
		for (String s : perms) {
			if (plugin.permissionHandler.has(player, s))
				r = true;
		}
		return r;
	}
}
