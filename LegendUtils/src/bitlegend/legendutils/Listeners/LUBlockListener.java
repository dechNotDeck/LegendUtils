package bitlegend.legendutils.Listeners;

import org.bukkit.event.block.BlockListener;

import bitlegend.legendutils.LegendUtils;;

public class LUBlockListener extends BlockListener {
	private final LegendUtils plugin;

	LUBlockListener(LegendUtils instance) {
		plugin = instance;
	}
	
}
