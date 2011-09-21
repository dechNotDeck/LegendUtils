package bitlegend.legendutils.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

import bitlegend.legendutils.LegendUtils;

public class LUEntityListener extends EntityListener {
	private final LegendUtils plugin;

	public LUEntityListener(LegendUtils instance) {
		plugin = instance;
	}
	
	public void onEntityDeath(EntityDeathEvent event) {
		try {
			//Entity killer = ((EntityDamageByEntityEvent)event.getEntity().getLastDamageCause()).getDamager();
			double modifier = plugin.config.readDouble("XP_Modifier");
			int xp = 0;
			if (event.getEntity() instanceof Player) {
				Player p = (Player)event.getEntity();
				int playerxp = p.getExperience();
				xp = (int)(playerxp * 0.1);
			} else {
				xp = event.getDroppedExp();
			}
			event.setDroppedExp((int)(xp * modifier));
		} catch (Exception e) {
			//
		}
	}
}
