package bitlegend.legendutils.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

import bitlegend.legendutils.LegendUtils;
import bitlegend.legendutils.Statistics.*;

public class LUEntityListener extends EntityListener {
	private final LegendUtils plugin;

	public LUEntityListener(LegendUtils instance) {
		plugin = instance;
	}
	
	public void onEntityDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e = (EntityDamageByEntityEvent)event;
			if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
				Player killed = (Player)e.getEntity();
				Player killer = (Player)e.getDamager();
				
				if ((killed.getHealth() - e.getDamage()) <= 0) {
					MostRecentDeath mrd = new MostRecentDeath(plugin, killed, killer);
					mrd.outputToDatabase();
				}
			}
		}
	}
}
