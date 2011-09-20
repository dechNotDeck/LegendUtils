package bitlegend.legendutils.Listeners;

import org.bukkit.event.entity.EntityListener;

import bitlegend.legendutils.LegendUtils;

public class LUEntityListener extends EntityListener {
	private final LegendUtils plugin;

	public LUEntityListener(LegendUtils instance) {
		plugin = instance;
	}
}
