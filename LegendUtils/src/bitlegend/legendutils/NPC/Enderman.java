package bitlegend.legendutils.NPC;

import java.lang.reflect.Field;

import bitlegend.legendutils.LegendUtils;

import net.minecraft.server.EntityEnderman;

public class Enderman {
	// Alter Enderman pickup list - Cirn9, credit for method to nisovin's
	// EnderNerf 9/19/2011
	
	public static boolean modifyBlocks(LegendUtils plugin) {
		try {
			Field f = EntityEnderman.class.getDeclaredField("b");
			f.setAccessible(true);
			boolean[] a = (boolean[]) f.get(null);
			for (int i = 0; i < a.length; i++) {
				a[i] = false; // can't pick item id i up
			}
			String rawList = plugin.config.readString("Enderman_Blocks");
			String[] parsedList = rawList.split(",");
			for (int i = 0; i < parsedList.length; i++) {
				a[Integer.parseInt(parsedList[i])] = true; //Can pickup item id put in here
			}
			f.set(null, a);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
