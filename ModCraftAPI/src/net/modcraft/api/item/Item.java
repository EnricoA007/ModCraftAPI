package net.modcraft.api.item;

import org.bukkit.inventory.ItemStack;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.resourcepack.model.ResourcePackModel;

public abstract class Item {

	private ResourcePackModel data;
	private String name;
	
	public Item(String name, ResourcePackModel data) {
		this.name=name;this.data=data;
		ModCraftAPI.resourcePackManager.registerResourceModel(data);
	}
	
	public String getName() {
		return name;
	}
	
	public ResourcePackModel getModel() {
		return data;
	}
	
	public abstract ItemStack getItem();
	
	
}
