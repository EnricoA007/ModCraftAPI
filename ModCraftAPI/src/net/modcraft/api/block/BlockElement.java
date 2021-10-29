package net.modcraft.api.block;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.modcraft.api.database.Database;

public class BlockElement {
	
	private Location loc;
	private Block block;
	private Player p;
	
	public BlockElement(Location loc, Block block, Player p) {
		this.loc=loc;this.block=block;this.p=p;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public String getRowLocation() {
		return Database.fromLocation(loc);
	}

}
