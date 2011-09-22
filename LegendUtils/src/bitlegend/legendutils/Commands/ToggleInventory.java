/*
 * ToggleInventory.java : Implementation for ToggleInventory command
 * LegendUtils - Cirn9 9/21/2011
 */
package bitlegend.legendutils.Commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import bitlegend.legendutils.LegendUtils;

public class ToggleInventory implements CommandExecutor {
	private final LegendUtils plugin;
	private final String path = "plugins" + File.separator + "LegendUtils" + File.separator + "ToggleInventory" + File.separator;
	
	public ToggleInventory(LegendUtils i){
		plugin = i;
	}
	
	private boolean hasPermission(Player player){
		if(plugin.permissionHandler.has(player, "legendutils.commands.toggleinventory") || plugin.permissionHandler.has(player, "legendutils.commands.*") || plugin.permissionHandler.has(player, "legendutils.*")){
			return true;
		}
		return false;
	}
				
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
		if (plugin.getServer().getPluginManager().isPluginEnabled("Permissions") || sender.isOp()) {
			Player p = (Player) sender;
			if (hasPermission(p) || p.isOp()) {
				try{
					DataInputStream in;
					DataOutputStream out;
					
					int currentState;
				
					//check state file
					try{
						in = new DataInputStream(new FileInputStream(path + p.getName()));
					} catch (FileNotFoundException e){
						//File DNE, so create with default state (0)
						out = new DataOutputStream(new FileOutputStream(path + p.getName()));
						out.writeInt(0);
						out.close();
						
						//now that it is created, set up in again
						in = new DataInputStream(new FileInputStream(path + p.getName()));
					}
					currentState = in.readInt();
					in.close(); //no more need to read the state for the rest of the cycle
					
					//flip the state file's value
					out = new DataOutputStream(new FileOutputStream(path + p.getName()));
					if(currentState == 0){ out.writeInt(1); }
					else { out.writeInt(0); }
					out.close();
					
					//write user's inventory file for current state
						if(currentState == 0){ out = new DataOutputStream(new FileOutputStream(path + p.getName() + ".pla")); }
						else { out = new DataOutputStream(new FileOutputStream(path + p.getName() + ".mod")); }
						
					
						//get their world and location
						//String world = p.getWorld().getName(); //add world support here
						double x = p.getLocation().getX();
						double y = p.getLocation().getY();
						double z = p.getLocation().getZ();
						
							//write their world and location
							//out.writeChars(world); //add world support here
							out.writeDouble(x);
							out.writeDouble(y);
							out.writeDouble(z);
						
						//Check for items in slots; if they have any, write the slot number (i), the item ID, the item Amount, and the item Damage values to the output
						for(int i = 0; i < 36; i++){
							if(p.getInventory().getItem(i) != null){
								if(p.getInventory().getItem(i).getTypeId() != 0){
									out.writeInt(i);
									out.writeInt(p.getInventory().getItem(i).getTypeId());
									out.writeInt(p.getInventory().getItem(i).getAmount());
									out.writeShort(p.getInventory().getItem(i).getDurability());
								}
							}
						}
							//easier to deal with armor if we make up these arbitrary values to represent the slot and just use get/set methods for armors
							int foot, legs, chest, helmet;
							foot = 1111; legs = 2222; chest = 3333; helmet = 4444;
							if(p.getInventory().getBoots() != null && p.getInventory().getBoots().getTypeId() != 0){
								out.writeInt(foot);
								out.writeInt(p.getInventory().getBoots().getTypeId());
								out.writeInt(p.getInventory().getBoots().getAmount());
								out.writeShort(p.getInventory().getBoots().getDurability());
							}
							if(p.getInventory().getLeggings() != null && p.getInventory().getLeggings().getTypeId() != 0){
								out.writeInt(legs);
								out.writeInt(p.getInventory().getLeggings().getTypeId());
								out.writeInt(p.getInventory().getLeggings().getAmount());
								out.writeShort(p.getInventory().getLeggings().getDurability());
							}
							if(p.getInventory().getChestplate() != null && p.getInventory().getChestplate().getTypeId() != 0){
								out.writeInt(chest);
								out.writeInt(p.getInventory().getChestplate().getTypeId());
								out.writeInt(p.getInventory().getChestplate().getAmount());
								out.writeShort(p.getInventory().getChestplate().getDurability());
							}
							if(p.getInventory().getHelmet() != null && p.getInventory().getHelmet().getTypeId() != 0){
								out.writeInt(helmet);
								out.writeInt(p.getInventory().getHelmet().getTypeId());
								out.writeInt(p.getInventory().getHelmet().getAmount());
								out.writeShort(p.getInventory().getHelmet().getDurability());
							}
						out.close(); //we've finished writing the player's inventory state
					
						//By this point, both in and out have been closed.
						//Clear the user inventory
						p.getInventory().clear(); //well that was easy
						
						//armor clearing has a retarded implementation
						p.getInventory().clear(p.getInventory().getSize()); //clear boots
						p.getInventory().clear(p.getInventory().getSize()+1); //clear leggings
						p.getInventory().clear(p.getInventory().getSize()+2); //clear chest
						p.getInventory().clear(p.getInventory().getSize()+3); //clear helmet
						
						
						//check if the user has a state file for the next state
						try{
							if(currentState == 0) { in = new DataInputStream(new FileInputStream(path + p.getName() + ".mod")); }
							else { in = new DataInputStream(new FileInputStream(path + p.getName() + ".pla")); }
							
							//if no exception is thrown by this point, file exists
							
							//load location data
							//add world support here
							double new_x, new_y, new_z;
							new_x = in.readDouble();
							new_y = in.readDouble();
							new_z = in.readDouble();
							
							//read and load items into the player inventory until you reach the end of the file; catch the exception that will be thrown when you reach the end of the file
							try{
								while(true){
									int itemSlot = in.readInt();
									int itemID = in.readInt();
									int itemAmount = in.readInt();
									short itemDurability = in.readShort();
									
									if(itemSlot < 1000 && itemID != 0){
										p.getInventory().setItem(itemSlot, new ItemStack(itemID, itemAmount, itemDurability));
									} else
									{
										if(itemSlot == foot && itemID != 0){ //boots
											p.getInventory().setBoots(new ItemStack(itemID, itemAmount, itemDurability));
										}
										if(itemSlot == legs && itemID != 0){ //leggings
											p.getInventory().setLeggings(new ItemStack(itemID, itemAmount, itemDurability));
										}
										if(itemSlot == chest && itemID != 0){ //chestplate
											p.getInventory().setChestplate(new ItemStack(itemID, itemAmount, itemDurability));
										}
										if(itemSlot == helmet && itemID != 0){ //helmet
											p.getInventory().setHelmet(new ItemStack(itemID, itemAmount, itemDurability));
										}
									}
								}
							}catch(Exception readex){ in.close(); } //this exception WILL be thrown when it reaches the end of the file, so this is a convenient place to close the file
							
							//teleport the player to the loaded location
							p.teleport(new Location(p.getWorld(), new_x, new_y, new_z)); //current implementation does not differentiate between worlds
							
						} catch(FileNotFoundException fnfex){ /*do nothing, on next execution new state file will be created*/ }
						catch(Exception ex){ System.out.println("LegendUtils ToggleInventory generic Exception in next state file: " + ex.getMessage()); return false; }
						
						if(currentState == 0){ p.sendMessage( "Switched to Moderator loadout."); }
						else{ p.sendMessage("Switched to Player loadout."); }
						
				} catch (Exception except){ System.out.println("LegendUtils ToggleInventory generic Exception: " + except.getMessage()); return false; }
				return true;
			}
		}
		return false;
	}
}
