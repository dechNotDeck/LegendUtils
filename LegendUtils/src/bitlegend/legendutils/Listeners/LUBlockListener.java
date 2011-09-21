package bitlegend.legendutils.Listeners;

import org.bukkit.event.block.BlockListener;

import bitlegend.legendutils.LegendUtils;;

public class LUBlockListener extends BlockListener {
	@SuppressWarnings("unused")
	private final LegendUtils plugin;

	LUBlockListener(LegendUtils instance) {
		plugin = instance;
	}
	
}
