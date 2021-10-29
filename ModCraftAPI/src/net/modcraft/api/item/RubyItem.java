package net.modcraft.api.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.modcraft.api.database.Resource;
import net.modcraft.api.resourcepack.model.ResourcePackModel;
import net.modcraft.api.resourcepack.model.ResourceType;

public class RubyItem extends Item {
	
	public RubyItem() {
		super("ruby", new ResourcePackModel(ResourceType.ITEM, "ruby", Resource.readResource("ruby.png")));
		new ItemBuilder("ruby").material(Material.SCUTE).amount(1).name("§4Rubin").nbt(nbt -> nbt.setInt("CustomModelData", this.getModel().hashCode())).build();
	}
	
	@Override
	public ItemStack getItem() {
		return new ItemBuilder("ruby").build();
	}

}
