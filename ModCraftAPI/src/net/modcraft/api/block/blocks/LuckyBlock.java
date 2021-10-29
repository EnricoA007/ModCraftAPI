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

public class LuckyBlock extends Block {
	
	public LuckyBlock() {
		super("LuckyBlock", Material.SPONGE, new ResourcePackModel(ResourceType.BLOCK, "LuckyBlock", Resource.readResource("luckyblock.png")));
		Blocks.registerItemListener(this, new ItemBuilder("luckyblock").material(Material.ITEM_FRAME).amount(1).name("§eLuckyBlock").addLore("§6Ein magischer Block").nbt(nbt -> nbt.setInt("CustomModelData", this.getModel().hashCode())).build());
	}
	
	@Override
	public void onDestroyBlock(BlockElement block) {
		super.onDestroyBlock(block);
		
		Random r = new Random();
		Material m = Material.values()[r.nextInt(Material.values().length)];
		try {
			block.getLocation().getWorld().dropItem(block.getLocation(), new ItemStack(m,1+r.nextInt(16)));
		}catch(Exception e) {
			
		}
	}


}
