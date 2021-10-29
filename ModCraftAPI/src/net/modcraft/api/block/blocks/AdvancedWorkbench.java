package net.modcraft.api.block.blocks;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

public class AdvancedWorkbench extends Block {
	
	public AdvancedWorkbench() {
		super("AdvancedWorkbench", Material.BARRIER, new ResourcePackModel("AdvancedWorkbench", Resource.readResource("/AdvancedWorkbench/north.png"), Resource.readResource("/AdvancedWorkbench/south.png"), Resource.readResource("/AdvancedWorkbench/east.png"), Resource.readResource("/AdvancedWorkbench/west.png"), Resource.readResource("/AdvancedWorkbench/up.png"), Resource.readResource("/AdvancedWorkbench/down.png")));
		Blocks.registerItemListener(this, new ItemBuilder("advancedworkbench").material(Material.ITEM_FRAME).amount(1).name("§cErweiterte Werkbank").addLore("§eEine besondere Werkbank").nbt(nbt -> nbt.setInt("CustomModelData", this.getModel().hashCode())).build());
	}
	
	@Override
	public void onPlaceBlock(BlockElement block, ItemStack itemUsed) {
		Inventory inv = Bukkit.createInventory(null, 54, "§cErweiterte Werkbank");
		
		for(int i = 0; i<45; i++) {
			inv.setItem(i, new ItemBuilder().material(Material.GRAY_STAINED_GLASS_PANE).name("§8-/-").build());
		}
		
		inv.setItem(10, new ItemStack(Material.AIR));
		inv.setItem(11, new ItemStack(Material.AIR));
		inv.setItem(12, new ItemStack(Material.AIR));
		
		inv.setItem(19, new ItemStack(Material.AIR));
		inv.setItem(20, new ItemStack(Material.AIR));
		inv.setItem(21, new ItemStack(Material.AIR));
		
		inv.setItem(24, new ItemStack(Material.AIR));
		
		inv.setItem(28, new ItemStack(Material.AIR));
		inv.setItem(29, new ItemStack(Material.AIR));
		inv.setItem(30, new ItemStack(Material.AIR));
		
		for(int i = 45; i<54; i++) {
			inv.setItem(i, new ItemBuilder().material(Material.RED_STAINED_GLASS_PANE).name("§cKein gültiges Rezept").build());
		}
	
		this.getElements().put(Database.fromLocation(block.getLocation()), InventoryUtil.inventoryToJson(inv, "§cErweiterte Werkbank"));
	
		super.onPlaceBlock(block, itemUsed);
	}
	
	@Override
	public NoThrowableCallable<Inventory> getInventoryTemplate(BlockElement block) {
		return InventoryUtil.getInventoryFromBlock(block);
	}
	
	@Override
	public void onInventoryClose(InventoryCloseEvent e, InventoryPlayer p) {
		Inventory inv = e.getInventory();
		HashMap<Integer, ItemStack> map =InventoryUtil.getInventoryChanges(InventoryUtil.getInventoryFromBlock(p.getAsBlockElement()).call(), inv);
		
		map.forEach((x,i) ->{
			if(x != 24 && i.getType() != Material.AIR) {
				p.getLocation().getWorld().dropItem(p.getLocation().clone().add(0,1,0), i);
			}
		});
	}
	
	@Override
	public void onInventoryClick(InventoryClickEvent e, InventoryPlayer p) {
		e.setCancelled(true);
		
		
		
	}

}
