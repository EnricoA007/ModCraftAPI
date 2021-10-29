package net.modcraft.api;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.inventory.ItemStack;

import net.modcraft.api.block.Block;
import net.modcraft.api.config.Config;
import net.modcraft.api.item.Item;
import net.modcraft.api.item.Items;
import net.modcraft.api.main.ModCraftMain;
import net.modcraft.api.resourcepack.ResourcePackManager;
import net.modcraft.api.resourcepack.model.ResourcePackModel;
import net.modcraft.api.web.FileWebServer;

public class ModCraftAPI {

	public static String prefix = "§e» §6ModCraft §e┃ §6";
	public static String version = "0.0.1-ALPHA";
	public static BufferedImage logo = ResourcePackModel.base64ToImage("iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAIAAAAlC+aJAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAHvSURBVGhD7ZhLTsMwEIbtJOUpsQAJkCgqjwKHYFEW3IRzQFXuwU2QUDfcgZegpSCxgBWilCYxJhmgJE3ruFamEfMpamaa5y//9tjhorrD8owF+9xCArAhAdiQAGxIADYkABsSgI3qbPTlxINoIPMHNkTJGLyVREmAfOTKZndoc9nuzOu+C0kyc6dO13mDJJmHG1tFg7qFhp+JYsf/0QcCC4Fx3+u3YRDHYZPd6hokyTi1psfakMSYqqyHgXELDUcwH6KBcLXTFEGwkIC9GXLfB0gANiQAm9wLUC1kpdUJn3/FnfMr+RuEUfE2KzwWlyFJZql1L0fSeDUI/mTTu2U/uHOz0VEpZCa/jSrO+RRLrCLGLZS1J00/TziWsOQW3LnPFhwyiWELLbSuIUnmuVgeZwtlTe4F6Fto9uwjHFh/cJlw63eQJONUNpzYlLS9NwFRSkZpgbFoPf2X8Hm0EkUaJBv0LWQdX0LUAxdCYbllcf7HQpxz73ALkpTotwAX8m2jqC0WfTi7BziSnhEsZHRpq81YdMRRIAHYkABsci9Av5Dx2gVE3zis8FQsDZ4qyyn3YqvhMblw+0UWMv9oG5KUkIWwIQHYZC3A4Go4RH8UkuMJRD2ovJ/2hX0x+VUCBeoD2JAAbEgANiQAGxKAC2OfKOOnmYx3IXkAAAAASUVORK5CYII=");
	public static ResourcePackManager resourcePackManager = new ResourcePackManager();
	public static FileWebServer fileWebServer = null;
	public static Config config = Config.createEmpty(ModCraftMain.config);
	
	private static List<Block> blocks = new ArrayList<>();
	private static byte[] resourcePackData;
	
	public static void main(String[] args) throws IOException {
		
	}

	public static byte[] getResourcePack() {
		return resourcePackData;
	}
	
	public static void setResourcePackData(byte[] resourcePackData) {
		ModCraftAPI.resourcePackData = resourcePackData;
	}
	
	public static void registerBlock(Block block) {
		blocks.add(block);
	}
	
	public static void forEachBlock(Consumer<Block> b) {
		blocks.forEach(b);
	}
	
	public static Block getBlockByName(String name) {
		for(Block b : blocks) {
			if(b.getName().equalsIgnoreCase(name)) {
				return b;
			}
		}
		return null;
	}
	
	public static void registerItem(String name, ItemStack item) {
		Items.itemMap.put(name, item);
	}

	public static void registerItem(Item item) {
		Items.itemMap.put(item.getName(), item.getItem());
	}
	
}
