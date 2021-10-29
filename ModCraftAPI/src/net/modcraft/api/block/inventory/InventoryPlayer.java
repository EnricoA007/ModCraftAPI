package net.modcraft.api.block.inventory;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.modcraft.api.block.Block;
import net.modcraft.api.block.BlockElement;

public class InventoryPlayer {
	
	private Player p;
	private Inventory inv;
	private Location loc;
	private Block b;
	
	protected InventoryPlayer(Player p, Inventory inv, Location loc, Block b) {
		this.p=p;
		this.inv=inv;
		this.loc=loc;
		this.b=b;
	}
	
	public Inventory getInventory() {
		return inv;
	}

	public Location getLocation() {
		return loc;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public Block getBlock() {
		return b;
	}
	
	public BlockElement getAsBlockElement() {
		return new BlockElement(this.getLocation(), this.getBlock(), this.getPlayer());
	}

}
