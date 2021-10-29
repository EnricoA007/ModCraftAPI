package net.modcraft.api.block.blocks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.modcraft.api.block.Block;
import net.modcraft.api.block.BlockElement;
import net.modcraft.api.block.Blocks;
import net.modcraft.api.block.inventory.InventoryPlayer;
import net.modcraft.api.block.inventory.InventoryUtil;
import net.modcraft.api.database.Database;
import net.modcraft.api.database.Resource;
import net.modcraft.api.item.ItemBuilder;
import net.modcraft.api.resourcepack.model.ResourcePackModel;
import net.modcraft.api.util.NoThrowableCallable;

public class TravelChest extends Block {
	
	public TravelChest() {	
		super("TravelChest", Material.DIRT, new ResourcePackModel("TravelChest", Resource.readResource("travelchest.png"), Resource.readResource("travelchest.png"),Resource.readResource("travelchest.png"),Resource.readResource("travelchest.png"),Resource.readResource("travelchest_special.png"),Resource.readResource("travelchest_special.png")));
		Blocks.registerItemListener(this, new ItemBuilder("travelchest").material(Material.ITEM_FRAME).amount(1).name("§3Travelchest").addLore("§aPlatz für 45 Items").nbt(nbt -> nbt.setInt("CustomModelData", getModel().hashCode())).build());
	}
	
	@Override
	public void onPlaceBlock(BlockElement block, ItemStack usedItem) {
		NBTTagCompound b =Blocks.getNBT(usedItem);
		if(!b.hasKey("SpecialNBT")) {
			this.getElements().put(Database.fromLocation(block.getLocation()), InventoryUtil.inventoryToJson(Bukkit.createInventory(null, 45, "§6TravelChest"), "§6TravelChest"));
		}else {
			this.getElements().put(Database.fromLocation(block.getLocation()), b.getCompound("SpecialNBT").getString("Data"));
		}
		
		super.onPlaceBlock(block,usedItem);
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent e, InventoryPlayer p) {
		super.onInventoryClose(e, p);
		this.getElements().remove(Database.fromLocation(p.getLocation()));
		this.getElements().put(Database.fromLocation(p.getLocation()), InventoryUtil.inventoryToJson(p.getInventory(), "§6TravelChest"));
	}
	
	@Override
	public void onDestroyBlock(BlockElement block) {
		String e = this.getElements().get(Database.fromLocation(block.getLocation()));
	
		ItemStack b = new ItemBuilder("travelchest").build();
		b = Blocks.nbtManipulator(b, nbt -> {
			NBTTagCompound list = new NBTTagCompound();
			list.setString("Data",e);
			nbt.set("SpecialNBT", list);
			
			return nbt;
		});
		
		super.onDestroyBlock(block);
		
		block.getLocation().getWorld().dropItem(block.getLocation(), b);
	}
	
	@Override
	public NoThrowableCallable<Inventory> getInventoryTemplate(BlockElement block) {
		return () -> {
			return InventoryUtil.jsonToInventory(
				this.getElements().get(Database.fromLocation(block.getLocation()))
			);
		};
	}
	
}
