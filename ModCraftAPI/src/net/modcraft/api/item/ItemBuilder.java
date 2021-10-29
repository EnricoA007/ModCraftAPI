package net.modcraft.api.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.modcraft.api.block.Block;

public class ItemBuilder {
	
	private static HashMap<String, ItemStack> markedItems = new HashMap<>();
	
	private Material material = Material.BARRIER;
	private int amount = 1;
	private short durability = 0;
	private String name;
	private Consumer<NBTTagCompound> nbtManipulating;
	private List<String> lore = new ArrayList<>();
	private String mark;
	
	public static ItemBuilder createBlockSample(Block b) {
		return new ItemBuilder(b.getName()).material(Material.ITEM_FRAME).amount(1).name("§aDefault Blockname").nbt(nbt -> nbt.setInt("CustomModelData", b.getModel().hashCode()));
	}
	
	public ItemBuilder() {}
	
	public ItemBuilder(String mark) {
		this.mark=mark;
	}

	public ItemBuilder material(Material material) {
		this.material = material;
		return this;
	}
	
	public ItemBuilder name(String name) {
		this.name=name;
		return this;
	}
	
	public ItemBuilder amount(int amount) {
		this.amount=amount;
		return this;
	}
	
	public ItemBuilder durability(int dura) {
		this.durability=(short)dura;
		return this;
	}
	
	public ItemBuilder addLore(String line) {
		lore.add(line);
		return this;
	}
	
	public ItemBuilder clearLore() {
		lore.clear();
		return this;
	}
	
	public ItemBuilder nbt(Consumer<NBTTagCompound> nbt) {
		this.nbtManipulating=nbt;
		return this;
	}
	
	public ItemStack build() {
		
		if(mark != null) {
			if(markedItems.containsKey(mark)) {
				return markedItems.get(mark);
			}
		}
		
		@SuppressWarnings("deprecation")
		ItemStack item = new ItemStack(material,amount,durability);
		ItemMeta meta = item.getItemMeta();
		if(name != null) meta.setDisplayName(name);
		if(!lore.isEmpty()) meta.setLore(lore);
		item.setItemMeta(meta);
		
		if(nbtManipulating != null) {
			item = nbtp(item);
		}
		
		if(mark != null) {
			if(!markedItems.containsKey(mark)) {
				markedItems.put(mark, item);
			}
		}
		
		return item;
	}
	
	private ItemStack nbtp(ItemStack i) {
		net.minecraft.server.v1_16_R3.ItemStack nms =CraftItemStack.asNMSCopy(i);
		NBTTagCompound base = new NBTTagCompound();
		if(nms.hasTag()) {
			base=nms.getTag();
		}
		
		this.nbtManipulating.accept(base);
		
		nms.setTag(base);
		return CraftItemStack.asBukkitCopy(nms);
	}
	
}
