package net.modcraft.api.block.blocks;

import org.bukkit.Material;

import net.modcraft.api.block.Block;
import net.modcraft.api.block.Blocks;
import net.modcraft.api.database.Resource;
import net.modcraft.api.item.ItemBuilder;
import net.modcraft.api.resourcepack.model.ResourcePackModel;

public class ExampleBlock extends Block {
	
	public ExampleBlock() {
		super("ExampleBlock", Material.BARRIER, new ResourcePackModel("ExampleBlock", Resource.readResource("/ExampleBlock/north.png"), Resource.readResource("/ExampleBlock/south.png"), Resource.readResource("/ExampleBlock/east.png"), Resource.readResource("/ExampleBlock/west.png"), Resource.readResource("/ExampleBlock/up.png"), Resource.readResource("/ExampleBlock/down.png")));
		Blocks.registerItemListener(this, new ItemBuilder("exampleblock").material(Material.ITEM_FRAME).amount(1).name("§eBeispiel Block").nbt(nbt -> nbt.setInt("CustomModelData", this.getModel().hashCode())).build());
	}


}
