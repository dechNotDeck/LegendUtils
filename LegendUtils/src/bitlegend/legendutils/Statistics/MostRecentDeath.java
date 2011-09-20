package bitlegend.legendutils.Statistics;

import org.bukkit.entity.Player;

import bitlegend.legendutils.LegendUtils;

public class MostRecentDeath {
	private Player deadplayer;
	private Player thekiller;
	private final LegendUtils plugin;
	
	public MostRecentDeath(LegendUtils instance, Player dead, Player killer) {
		plugin = instance;
		setDeadPlayer(dead);
		setTheKiller(killer);
	}
	
	private void setDeadPlayer(Player player) {
		deadplayer = player;
	}
	
	private void setTheKiller(Player player) {
		thekiller = player;
	}
	
	public Player getDeadPlayer() {
		return deadplayer;
	}
	
	public Player getTheKiller() {
		return thekiller;
	}
	
	public void outputToDatabase() {
		/*
		 * Create new table in database
		 * Delete current entries
		 * Add new entries
		 */
	}
}
