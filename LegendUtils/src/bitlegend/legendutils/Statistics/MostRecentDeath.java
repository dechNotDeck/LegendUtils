package bitlegend.legendutils.Statistics;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import bitlegend.legendutils.LegendUtils;

public class MostRecentDeath {
	private Player deadplayer;
	private Player thekiller;
	private ItemStack theweapon;
	private final LegendUtils plugin;
	
	public MostRecentDeath(LegendUtils instance, Player dead, Player killer) {
		plugin = instance;
		setDeadPlayer(dead);
		setTheKiller(killer);
		setTheWeapon(killer);
	}
	
	private void setDeadPlayer(Player player) {
		deadplayer = player;
	}
	
	private void setTheKiller(Player player) {
		thekiller = player;
	}
	
	private void setTheWeapon(Player player) {
		ItemStack is = player.getItemInHand();
		theweapon = is;
	}
	
	public Player getDeadPlayer() {
		return deadplayer;
	}
	
	public Player getTheKiller() {
		return thekiller;
	}
	
	public ItemStack getTheWeapon() {
		return theweapon;
	}
	
	public String getTheWeaponString() {
		return theweapon.getType().name();
	}
	
	public void outputToDatabase() {
		String dbname = plugin.config.readString("DB_Name");
		String dbhost = plugin.config.readString("DB_Host");
		String dbuser = plugin.config.readString("DB_User");
		String dbpass = plugin.config.readString("DB_Pass");
		
		//System.out.println(thekiller.getDisplayName() + " killed " + deadplayer.getDisplayName() + " with " + theweapon.getType().name());
	}
}
