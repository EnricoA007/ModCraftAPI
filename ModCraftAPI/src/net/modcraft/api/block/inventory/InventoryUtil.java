package net.modcraft.api.block.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.modcraft.api.block.Block;
import net.modcraft.api.block.BlockElement;
import net.modcraft.api.block.Blocks;
import net.modcraft.api.database.Database;
import net.modcraft.api.util.AtomicObject;
import net.modcraft.api.util.NoThrowableCallable;

public class InventoryUtil {

	private static HashMap<Block, List<InventoryPlayer>> invMap = new HashMap<>();
	
	public static String inventoryToJson(Inventory inv, String title) {
		if(inv.getType() != InventoryType.CHEST) throw new IllegalArgumentException("Only chest inventories are allowed to be encoded into json");
		
		JsonArray items = new JsonArray();

		for(int x = 0; x<inv.getSize(); x++) {
			try {
				ItemStack item =inv.getItem(x);
				String json =itemstackToJson(item);
				
				JsonObject itemInfo =new JsonObject();
				itemInfo.addProperty("Item", json);
				itemInfo.addProperty("Position", ""+x);
				
				items.add(itemInfo);
			}catch(Exception ex) {
				continue;
			}
		}
		
		JsonObject obj = new JsonObject();
		obj.addProperty("Title", title);
		obj.addProperty("Size", inv.getSize());
		obj.add("Items", items);
		
		return Database.gsonPretty.toJson(obj);
	}
	
	public static Inventory jsonToInventory(String json) {
		JsonObject obj = Database.gsonPretty.fromJson(json, JsonObject.class);
		String title = obj.get("Title").getAsString();
		int size = obj.get("Size").getAsInt();
		JsonArray items = obj.get("Items").getAsJsonArray();
		
		Inventory inv = Bukkit.createInventory(null, size, title);
		
		items.forEach(itemInfoL -> {
			JsonObject itemInfo = itemInfoL.getAsJsonObject();
			
			inv.setItem(itemInfo.get("Position").getAsInt(), jsonToItem(itemInfo.get("Item").getAsString()));
		});
		
		return inv;
	}
	
	@SuppressWarnings("deprecation")
	public static String itemstackToJson(ItemStack item) {
		JsonObject obj = new JsonObject();
		obj.addProperty("Material", item.getType().name());
		obj.addProperty("Amount", item.getAmount());
		obj.addProperty("Durability", item.getDurability());
		boolean b = false;
		try {
			if(CraftItemStack.asNMSCopy(item).hasTag()) {
				obj.addProperty("Tag", CraftItemStack.asNMSCopy(item).getTag().toString());	
				b=true;
			}
		}catch(Exception ex) {}
		if(!b) obj.addProperty("Tag", "null");
		return Database.gsonPretty.toJson(obj);
	}
	
	public static ItemStack jsonToItem(String json) {
		JsonObject obj = Database.gsonPretty.fromJson(json, JsonObject.class);
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(Material.valueOf(obj.get("Material").getAsString()), obj.get("Amount").getAsInt(), obj.get("Durability").getAsShort());
		String tag = obj.get("Tag").getAsString();
		
		if(tag.equals("null")) return item;
		NBTTagCompound nbt;
		try {
			nbt = MojangsonParser.parse(tag);
		} catch (CommandSyntaxException e) {
			return item;
		}
		
		return Blocks.nbt(item, nbt);
	}
	
	public static void registerOpenInventory(Block b, Player p, Inventory inv, Location loc) {
		ArrayList<InventoryPlayer> array = invMap.containsKey(b) ? (ArrayList<InventoryPlayer>) invMap.get(b) : new ArrayList<>();
		array.add(new InventoryPlayer(p, inv, loc, b));
		if(invMap.containsKey(b)) invMap.remove(b);
		invMap.put(b, array);
	}
	
	public static void unregisterOpenInventory(InventoryPlayer player) {
		if(!invMap.containsKey(player.getBlock())) return;
		ArrayList<InventoryPlayer> array = (ArrayList<InventoryPlayer>) invMap.get(player.getBlock());
		if(!array.contains(player)) return;
		array.remove(player);
	}
	
	public static InventoryPlayer getOpenedInventoryPlayer(Block b, Player p) {
		if(!invMap.containsKey(b)) return null;
		ArrayList<InventoryPlayer> array = (ArrayList<InventoryPlayer>) invMap.get(b);
		try {
			return array.stream().filter(t->t.getPlayer().equals(p)).findFirst().get();
		}catch(NullPointerException e) {
			return null;
		}
	}
	
	public static InventoryPlayer getOpenedInventoryPlayer(Player p) {
		AtomicObject<InventoryPlayer> a = new AtomicObject<>();
		
		invMap.values().forEach(list -> {
			if(a.hasElement()) return;
			list.forEach(player -> {
				if(a.hasElement()) return;
				if(player.getPlayer().equals(p)) {
					a.setElement(player);
				}
			});
		});
		
		return a.getElement();
	}

	public static NoThrowableCallable<Inventory> getInventoryFromBlock(BlockElement block) {
		return () -> {
			return jsonToInventory(block.getBlock().getElements().get(block.getRowLocation()));
		};
	}
	
	/**
	 * @param inv The inventory
	 * @returnReturns a map with the ItemStack and the position/slot of the itemstack in the inventory
	 */
	public static HashMap<Integer, ItemStack> getItemsFromInventory(Inventory inv){
		if(inv.getType() != InventoryType.CHEST) throw new IllegalArgumentException("The entered inventory isn't a chest");
		
		HashMap<Integer, ItemStack> map = new HashMap<>();
		for(int x = 0; x<inv.getSize(); x++) {
			try {
				ItemStack item =inv.getItem(x);
				map.put(x, item);
			}catch(Exception ex) {
				map.put(x, new ItemStack(Material.AIR));
				continue;
			}
		}
		
		return map;
	}
	
	/**
	 * @param original The original inventory
	 * @param sample The inventory to test
	 * @return Returns a map with the ItemStack and the position/slot of the itemstack in the inventory
	 */
	public static HashMap<Integer, ItemStack> getInventoryChanges(Inventory original, Inventory sample){
		HashMap<Integer, ItemStack> map = new HashMap<>();
		
		if(original.getSize() == sample.getSize() && original.getType() == sample.getType()) throw new IllegalArgumentException("The original and the sample aren't the same type or they haven't the same size");
		
		HashMap<Integer, ItemStack> oriItems = getItemsFromInventory(original);
		HashMap<Integer, ItemStack> sampleItems = getItemsFromInventory(sample);
		
		sampleItems.forEach((p,i) -> {
			
			if(!oriItems.containsKey(p)) {
				map.put(p, i);
				return;
			}
			
			if(!oriItems.get(p).equals(i)) {
				map.put(p, i);
				return;
			}
			
		});
		
		return map;
	}

}
