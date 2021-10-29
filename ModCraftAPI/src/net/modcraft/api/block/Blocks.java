package net.modcraft.api.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.modcraft.api.util.ACallback;

public class Blocks {

	private static HashMap<Block, List<ItemStack>> map = new HashMap<>();
	
	public static void registerItemListener(Block b, ItemStack item) {
		List<ItemStack> items = new ArrayList<>();
		if(map.containsKey(b)) {
			items = map.get(b);
			map.remove(b);
		}
		items.add(item);
		map.put(b, items);
	}
	
	public static ItemStack[] getItemsRegisteredOnBlock(Block b) {
		if(map.containsKey(b)) {
			ItemStack[] items = new ItemStack[map.get(b).size()];
			for(int x = 0; x<items.length; x++) {
				items[x] = map.get(b).get(x);
			}
			return items;
		}
		return null;
	}
	
	public static boolean hasBlockItems(Block b) {
		return map.containsKey(b);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean removeItemWhenHas(Player p, ItemStack compare) {
		ItemStack hand =p.getItemInHand().clone();
		NBTTagCompound b = getNBT(hand);
		compare = compare.clone();
		
		int amount = hand.getAmount();
		hand.setAmount(compare.getAmount());
		compare = (ItemStack)clearNBT(compare)[1];
		Object[] info = clearNBT(hand);
		hand = (ItemStack)info[1];
		
		if(hand.equals(compare)) {
			hand.setAmount(amount);
			hand =nbt(hand,b);
			if(p.getGameMode() != GameMode.CREATIVE) {
				if(1 != amount) {
					hand.setAmount(amount-1);
					p.setItemInHand(hand);
				}else {
					p.setItemInHand(new ItemStack(Material.AIR));
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	public static ItemStack nbt(ItemStack item, NBTTagCompound b) {
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		nms.setTag(b);
		return CraftItemStack.asBukkitCopy(nms);
	}
	
	public static NBTTagCompound getNBT(ItemStack item) {
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		return nms.hasTag() ? nms.getTag() : null;
	}
	
	public static ItemStack nbtManipulator(ItemStack item, ACallback<NBTTagCompound, NBTTagCompound> callback) {
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		nms.setTag(callback.callback(nms.getTag()));
		return CraftItemStack.asBukkitCopy(nms);
	}

	public static Object[] clearNBT(ItemStack item) {
		net.minecraft.server.v1_16_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
		
		if(!nms.hasTag()) return new Object[] {null, item};
		NBTTagCompound b = nms.getTag();
		if(b.hasKey("SpecialNBT")) b.remove("SpecialNBT");
		nms.setTag(b);
		
		return new Object[] {nms.getTag(), CraftItemStack.asBukkitCopy(nms)};
	}
	
}
