package net.modcraft.api.block;

import java.util.HashMap;

import javax.annotation.OverridingMethodsMustInvokeSuper;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.block.inventory.InventoryPlayer;
import net.modcraft.api.block.inventory.InventoryUtil;
import net.modcraft.api.database.Database;
import net.modcraft.api.item.ItemBuilder;
import net.modcraft.api.resourcepack.model.ResourcePackModel;
import net.modcraft.api.resourcepack.model.ResourceType;
import net.modcraft.api.util.NoThrowableCallable;

public abstract class Block {

	private String name;
	private ResourcePackModel model;
	private Material defaultBlock;
	private HashMap<String, String> elements = null;
	
	public Block(String name, Material defaultBlock, ResourcePackModel model) {
		if(model.getType()==ResourceType.ITEM) throw new IllegalArgumentException("The model is an item not a block");
		ModCraftAPI.resourcePackManager.registerResourceModel(model);
		this.name=name;
		this.model=model;
		this.defaultBlock=defaultBlock;
		elements = Database.getDatabase(name);
		
	}
	
	@OverridingMethodsMustInvokeSuper
	public void onDestroyBlock(BlockElement block) {
		block.getLocation().getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2).stream().filter(e -> e.getType() == EntityType.ARMOR_STAND).filter(e -> e.getName().equals(Database.fromLocation(block.getLocation()))).findFirst().get().remove();
		elements.remove(Database.fromLocation(block.getLocation()));
	}
	
	@OverridingMethodsMustInvokeSuper
	@SuppressWarnings("deprecation")
	public void onPlaceBlock(BlockElement block, ItemStack itemUsed) {
		block.getLocation().getBlock().setType(defaultBlock);
		
		ArmorStand a = (ArmorStand) block.getLocation().getWorld().spawnEntity(block.getLocation().clone().add(0.5,1,0.5), EntityType.ARMOR_STAND);
		
		a.setGravity(false);
		a.setAI(false);
		a.setInvisible(true);
		a.setMarker(true);
		a.setHeadPose(new EulerAngle(0d, 0d, 0d));
		a.setHelmet(new ItemBuilder().material(Material.ITEM_FRAME).nbt(nbt -> nbt.setInt("CustomModelData", this.getModel().hashCode())).build());
		a.setCustomName(Database.fromLocation(block.getLocation()));
		
		if(!elements.containsKey(Database.fromLocation(block.getLocation()))) {
			elements.put(Database.fromLocation(block.getLocation()), "");			
		}
	}
	
	public void onRightClickBlock(BlockElement block) {
		if(getInventoryTemplate(block) != null) {
			try {
				Inventory inv =this.getInventoryTemplate(block).call();
				block.getPlayer().openInventory(inv);
				InventoryUtil.registerOpenInventory(this, block.getPlayer(), inv, block.getLocation());
			} catch (Exception e) {	}
		}
	}
	
	public void onLeftClickBlock(BlockElement block) {
		
	}
	
	public void onTick(BlockElement[] blocks) {
		
	}

	public void onInventoryClick(InventoryClickEvent e, InventoryPlayer p) {
		
	}
	
	public void onInventoryClose(InventoryCloseEvent e, InventoryPlayer p) {
		
	}
	
	/**
	 * Overwrite this method to add a inventory
	 * @return Inventory (default = null)
	 */
	public NoThrowableCallable<Inventory> getInventoryTemplate(BlockElement block) {
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public ResourcePackModel getModel() {
		return model;
	}
	
	public HashMap<String, String> getElements() {
		return elements;
	}
	
	public Material getDefaultBlock() {
		return defaultBlock;
	}
	
}
