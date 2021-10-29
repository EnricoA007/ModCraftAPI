package net.modcraft.api.block.blocks;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.modcraft.api.block.BlockElement;
import net.modcraft.api.block.Block;
import net.modcraft.api.block.Blocks;
import net.modcraft.api.database.Resource;
import net.modcraft.api.item.ItemBuilder;
import net.modcraft.api.resourcepack.model.ResourcePackModel;
import net.modcraft.api.resourcepack.model.ResourceType;

public class RubyOre extends Block {
	
	public RubyOre() {
		super("RubyOre", Material.OBSIDIAN, new ResourcePackModel(ResourceType.BLOCK, "RubyOre", Resource.readResource("ruby_ore.png")));
		Blocks.registerItemListener(this, ItemBuilder.createBlockSample(this).name("§4Rubinerz").build());
	}

	@Override
	public void onDestroyBlock(BlockElement block) {
		super.onDestroyBlock(block);
		ItemStack ruby =new ItemBuilder("ruby").build();
		ruby.setAmount(1 + new Random().nextInt(3));
		block.getLocation().getWorld().dropItem(block.getLocation(), ruby);
	}
	
}
