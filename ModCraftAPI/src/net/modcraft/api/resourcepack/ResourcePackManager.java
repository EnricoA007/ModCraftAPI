package net.modcraft.api.resourcepack;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.resourcepack.model.ResourcePackModel;
import net.modcraft.api.resourcepack.model.ResourceType;

public class ResourcePackManager {

	private ArrayList<ResourcePackModel> models =new ArrayList<>();
	
	public  void registerResourceModel(ResourcePackModel model) {
		if(!hasRegisteredResourceModel(model)) models.add(model);
	}
	
	public void unregisterResourceModel(ResourcePackModel model) {
		if(hasRegisteredResourceModel(model)) models.remove(model);
	}
	
	public boolean hasRegisteredResourceModel(ResourcePackModel model) {
		return(models.contains(model));
	}
	
	public boolean hasRegisteredResourceModel(String n) {
		for(ResourcePackModel m : models) {
			if(m.getName().equals(n)) {
				return true;
			}
		}
		return false;
	}
	
	public ResourcePackModel getRegisteredResourceModel(String n) {
		for(ResourcePackModel m : models) {
			if(m.getName().equals(n)) {
				return m;
			}
		}
		return null;
	}
	
	public ResourcePackModel[] getResourcePackModels() {
		ResourcePackModel[] p = new ResourcePackModel[models.size()];
		for(int i = 0; i<models.size(); i++) {
			p[i] = models.get(i);
		}
		return p;
	}
	
	public ResourcePackModel[] getResourcePackModels(ResourceType filter) {
		ArrayList<ResourcePackModel> models = new ArrayList<>();
		this.models.stream().filter(m -> m.getType() == filter).forEach(models::add);
		ResourcePackModel []m = new ResourcePackModel[models.size()];
		
		for(int x = 0; x<m.length; x++) {
			m[x]=models.get(x);
		}
		
		return m;
	}
	
	public byte[] createResourcePack() {
		try {
			ByteArrayOutputStream s = new ByteArrayOutputStream();
			ZipOutputStream out = new ZipOutputStream(s);
			
			ZipEntry packMCmeta = new ZipEntry("pack.mcmeta");
			out.putNextEntry(packMCmeta);
			out.write(("{\"pack\":{\"pack_format\":7,\"description\":\"§eModCraft ResourcePack\n§7" + ModCraftAPI.version + "\"}}").getBytes(StandardCharsets.UTF_8));
			out.closeEntry();
			
			ZipEntry packPNG = new ZipEntry("pack.png");
			out.putNextEntry(packPNG);
			out.write(ResourcePackModel.imageToByte(ModCraftAPI.logo, "png"));
			out.closeEntry();

			ZipEntry itemGenerated = new ZipEntry("assets/minecraft/models/item/generated.json");
			out.putNextEntry(itemGenerated);
			out.write("{\"parent\":\"builtin/generated\",\"gui_light\":\"front\",\"display\":{\"ground\":{\"rotation\":[0,0,0],\"translation\":[0,2,0],\"scale\":[0.5,0.5,0.5]},\"head\":{\"rotation\":[0,180,0],\"translation\":[0,13,7],\"scale\":[1,1,1]},\"thirdperson_righthand\":{\"rotation\":[0,0,0],\"translation\":[0,3,1],\"scale\":[0.55,0.55,0.55]},\"firstperson_righthand\":{\"rotation\":[0,-90,25],\"translation\":[1.13,3.2,1.13],\"scale\":[0.68,0.68,0.68]},\"fixed\":{\"rotation\":[0,180,0],\"scale\":[1,1,1]}}}".getBytes(StandardCharsets.UTF_8));
			out.closeEntry();
			
			String elements = "";
			//Create scute.json and item.json
			for(ResourcePackModel item : getResourcePackModels(ResourceType.ITEM)) {
				elements += "{\"predicate\": {\"custom_model_data\":" + item.hashCode() + "}, \"model\": \"item/custom/item/" + item.hashCode() + "\"},";
				
				//Create item.json
				ZipEntry bj = new ZipEntry("assets/minecraft/models/item/custom/item/" + item.hashCode() + ".json");
				out.putNextEntry(bj);
				out.write(("{\"parent\": \"item/generated\",\"textures\": {\"layer0\": \"item/custom/" + item.hashCode() + "\"}}").getBytes(StandardCharsets.UTF_8));
				out.closeEntry();
				
				//Create item.png
				ZipEntry b = new ZipEntry("assets/minecraft/textures/item/custom/" + item.hashCode() + ".png");
				out.putNextEntry(b);
				out.write(ResourcePackModel.imageToByte(item.getItemTexture(), "png"));
				out.closeEntry();				
			}
			
			if(elements.endsWith(",")) elements=elements.substring(0,elements.length()-1);
			ZipEntry e = new ZipEntry("assets/minecraft/models/item/scute.json");
			out.putNextEntry(e);
			out.write(("{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"item/scute\"},\"overrides\": [" + elements + "]}").getBytes(StandardCharsets.UTF_8));
			out.closeEntry();
			elements = "";
			
			//Create item_frame.json and block.json
			for(ResourcePackModel block : getResourcePackModels(ResourceType.BLOCK)) {
				elements += "{\"predicate\": {\"custom_model_data\":" + block.hashCode() + "}, \"model\": \"item/custom/block/" +block.hashCode() + "\"},";
			
				//Create block.json
				ZipEntry bj = new ZipEntry("assets/minecraft/models/item/custom/block/" + block.hashCode() + ".json");
				out.putNextEntry(bj);
				out.write(("{\"parent\": \"minecraft:block/cube\",\"textures\": {\"north\": \"" + ("block/custom/" + block.hashCode() + "_north") + "\",\"south\": \"" + ("block/custom/" + block.hashCode() + "_south") + "\",\"east\": \"" + ("block/custom/" + block.hashCode() + "_east") + "\",\"west\": \"" + ("block/custom/" + block.hashCode() + "_west") + "\",\"up\": \"" + ("block/custom/" + block.hashCode() + "_up") + "\",\"down\": \"" + ("block/custom/" + block.hashCode() + "_down") + "\"},\"display\":{\"head\": {\"rotation\": [0, 0, 0],\"translation\": [0, -56.03, 0],\"scale\": [1.601, 1.601, 1.601]}}}").getBytes(StandardCharsets.UTF_8));
				out.closeEntry();
				
				//Create block sides
				ZipEntry b = new ZipEntry("assets/minecraft/textures/block/custom/" + block.hashCode() + "_north.png");
				out.putNextEntry(b);
				out.write(ResourcePackModel.imageToByte(block.getNorth(), "png"));
				out.closeEntry();
				
				b = new ZipEntry("assets/minecraft/textures/block/custom/" + block.hashCode() + "_east.png");
				out.putNextEntry(b);
				out.write(ResourcePackModel.imageToByte(block.getEast(), "png"));
				out.closeEntry();
				
				b = new ZipEntry("assets/minecraft/textures/block/custom/" + block.hashCode() + "_south.png");
				out.putNextEntry(b);
				out.write(ResourcePackModel.imageToByte(block.getSouth(), "png"));
				out.closeEntry();
				
				b = new ZipEntry("assets/minecraft/textures/block/custom/" + block.hashCode() + "_west.png");
				out.putNextEntry(b);
				out.write(ResourcePackModel.imageToByte(block.getWest(), "png"));
				out.closeEntry();
				
				b = new ZipEntry("assets/minecraft/textures/block/custom/" + block.hashCode() + "_up.png");
				out.putNextEntry(b);
				out.write(ResourcePackModel.imageToByte(block.getUp(), "png"));
				out.closeEntry();
				
				b = new ZipEntry("assets/minecraft/textures/block/custom/" + block.hashCode() + "_down.png");
				out.putNextEntry(b);
				out.write(ResourcePackModel.imageToByte(block.getDown(), "png"));
				out.closeEntry();
			}
			
			if(elements.endsWith(",")) elements=elements.substring(0,elements.length()-1);
			e = new ZipEntry("assets/minecraft/models/item/item_frame.json");
			out.putNextEntry(e);
			out.write(("{\"parent\": \"item/handheld\",\"textures\": {\"layer0\": \"item/item_frame\"},\"overrides\": [" + elements + "]}").getBytes(StandardCharsets.UTF_8));
			out.closeEntry();
			
			
			out.close();
			return s.toByteArray();
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
