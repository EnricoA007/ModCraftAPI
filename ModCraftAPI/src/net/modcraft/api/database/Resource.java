package net.modcraft.api.database;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.modcraft.api.main.ModCraftMain;
import net.modcraft.api.resourcepack.model.ResourcePackModel;

public class Resource {
	
	public static BufferedImage readResource(String resource) {
		try {
			return ImageIO.read(new File(ModCraftMain.resources + "/" + resource));
		}catch(Exception e) {
			return ResourcePackModel.base64ToImage("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAA2SURBVDhPY/h/ko8kBNZQr46J3sqoYKJRDWgITSkEQTWgiUIQmn4oGtWAitCVQhBEAwnoJB8A5D6CnAQs018AAAAASUVORK5CYII=");
		}
	}

}
