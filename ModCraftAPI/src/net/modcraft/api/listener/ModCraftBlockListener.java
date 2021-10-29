package net.modcraft.api.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.block.BlockElement;
import net.modcraft.api.block.Blocks;
import net.modcraft.api.block.inventory.InventoryPlayer;
import net.modcraft.api.block.inventory.InventoryUtil;
import net.modcraft.api.database.Database;
import net.modcraft.api.util.AtomicObject;

public class ModCraftBlockListener implements Listener {

	@EventHandler
	public void onBlockPlace(PlayerInteractEvent e) {
		if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if(e.getHand() != EquipmentSlot.HAND) return;
		
		AtomicObject<Boolean> isPlaced =new AtomicObject<>(false);
		
		ModCraftAPI.forEachBlock(block -> {
			if(Blocks.hasBlockItems(block)) {
				for(ItemStack it : Blocks.getItemsRegisteredOnBlock(block)) {
					@SuppressWarnings("deprecation")
					ItemStack hand = e.getPlayer().getItemInHand().clone();
					if(Blocks.removeItemWhenHas(e.getPlayer(), it)) {
						e.setCancelled(true);
						isPlaced.setElement(true);
						block.onPlaceBlock(new BlockElement(e.getClickedBlock().getLocation().add(0,1,0), block, e.getPlayer()), hand);
					}
				}
			}
		});
		
		if(isPlaced.getElement()) return;
		
		AtomicObject<BlockElement> ab = new AtomicObject<BlockElement>();
		ModCraftAPI.forEachBlock(block -> {
			block.getElements().forEach((k,v) -> {
				Location l = Database.toLocation(k);
				if(e.getClickedBlock().getLocation().equals(l)) {
					e.setCancelled(true);
					ab.setElement(new BlockElement(l, block, e.getPlayer()));
				}
			});
		});
		if(ab.hasElement())
			ab.getElement().getBlock().onRightClickBlock(ab.getElement());
	
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		ArrayList<BlockElement> ab = new ArrayList<>();
		ModCraftAPI.forEachBlock(block -> {
			block.getElements().forEach((k,v) -> {
				Location l = Database.toLocation(k);
				if(e.getBlock().getLocation().equals(l)) {
					e.setCancelled(true);
					e.getBlock().setType(Material.AIR);
					ab.add(new BlockElement(l, block, e.getPlayer()));
				}
			});
		});
		ab.forEach(f -> f.getBlock().onDestroyBlock(f));
	}
	
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e) {
		InventoryPlayer player = InventoryUtil.getOpenedInventoryPlayer((Player) e.getPlayer());
		if(player == null) return;
		
		if(!player.getInventory().equals(e.getInventory())) return;
		player.getBlock().onInventoryClose(e, player);
		InventoryUtil.unregisterOpenInventory(player);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		InventoryPlayer player = InventoryUtil.getOpenedInventoryPlayer((Player) e.getWhoClicked());
		if(player == null) return;
		if(!player.getInventory().equals(e.getInventory())) return;
		player.getBlock().onInventoryClick(e, player);
	}
	
	@EventHandler
	public void onPistonExtend(BlockPistonExtendEvent e){
		List<Block> eb = e.getBlocks();
		ModCraftAPI.forEachBlock(block -> {
			block.getElements().forEach((k,v) -> {
				eb.forEach(b -> {
					Location l =Database.toLocation(k);
					if(l.equals(b.getLocation())) {
						e.setCancelled(true);
					}
				});
			});
		});
	}
	
}
