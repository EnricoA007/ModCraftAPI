package net.modcraft.api.resourcepack.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.item.Items;

public class ResourcePackModel {

	private BufferedImage north,south,east,west,up,down;
	private BufferedImage texture;
	private String name;
	private ResourceType type;
	private final int hash;
	
	/**
	 * Create a block
	 * @param name name of the block
	 * @param north side of the block
	 * @param south side of the block
	 * @param east side of the block 
	 * @param west side of the block
	 * @param up side of the block 
	 * @param down side of the block
	 */
	public ResourcePackModel(String name, BufferedImage north, BufferedImage south, BufferedImage east, BufferedImage west, BufferedImage up, BufferedImage down) {
		check(name.toUpperCase());
		this.north=north;
		this.south=south;
		this.east=east;
		this.west=west;
		this.up=up;
		this.down=down;
		this.name=name.toUpperCase();
		this.type=ResourceType.BLOCK;
		hash=Items.getHashCode(this);
	}
	

	/**
	 * Create a item/or block with one side
	 * @param type select BLOCK or ITEM
	 * @param name name of the item/block
	 * @param all the textures of the block on all sides or the item texture
	 */
	public ResourcePackModel(ResourceType type, String name, BufferedImage all) {
		check(name.toUpperCase());
		if(type == ResourceType.BLOCK) {
			this.north=all;
			this.south=all;
			this.east=all;
			this.west=all;
			this.up=all;
			this.down=all;
		}else {
			this.texture=all;
		}
		
		this.name=name.toUpperCase();
		this.type=type;
		hash=Items.getHashCode(this);
	}
	
	public BufferedImage getNorth() {
		return north;
	}
	
	public BufferedImage getSouth() {
		return south;
	}
	
	public BufferedImage getEast() {
		return east;
	}
	
	public BufferedImage getWest() {
		return west;
	}
	
	public BufferedImage getUp() {
		return up;
	}
	
	public BufferedImage getDown() {
		return down;
	}
	
	public String getName() {
		return name;
	}
	
	public ResourceType getType() {
		return type;
	}
	
	public BufferedImage getItemTexture() {
		return texture;
	}
	
	private void check(String n) {
		if(ModCraftAPI.resourcePackManager.hasRegisteredResourceModel(n)) {
			throw new RuntimeException("Model name already registered \"" + n + "\"");
		}
	}
	
	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public String toString() {
		return Base64.getEncoder().encodeToString((type + ":" + name.toUpperCase()).getBytes());
	}
	
	
	public static byte[] imageToByte(BufferedImage img, String format) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(img, format, out);
		} catch (IOException e) {
			return null;
		}
		return out.toByteArray();
		
	}
	
	public static BufferedImage base64ToImage(String b64) {
		try {
			return ImageIO.read(new ByteArrayInputStream(Base64.getDecoder().decode(b64)));
		} catch (IOException e) {
			return null;
		}
	}

}
