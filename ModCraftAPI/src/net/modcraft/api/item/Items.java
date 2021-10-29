package net.modcraft.api.item;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.database.Database;
import net.modcraft.api.resourcepack.model.ResourcePackModel;

public class Items {

	private static HashMap<Integer, String> uuids = new HashMap<>();
	public static HashMap<String, ItemStack> itemMap = new HashMap<>();
	public static int hashCodeEnd = 1;
	
	public static String exportHashCodes() {
		JsonArray a = new JsonArray();
		
		uuids.forEach((n,o) ->{
			JsonObject obj = new JsonObject();
			obj.addProperty("ID", n);
			obj.addProperty("Name", o);
			a.add(obj);
		});
		
		return Database.gsonPretty.toJson(a);
	}
	
	public static void importHashCodes(String s) {
		JsonArray a = Database.gsonPretty.fromJson(s, JsonArray.class);
		
		System.out.println("//-- ANALYSE HASH CODES [ITEMS] --\\");
		
		if(a.size() == 0) System.out.println("-- NO HASH CODE FOUND --");
		
		a.forEach(e ->{
			JsonObject o =e.getAsJsonObject();
			int id = o.get("ID").getAsInt();
			if(id > hashCodeEnd) hashCodeEnd=id+1;
			String name = o.get("Name").getAsString();
			System.out.println("Import the id " + id + " with name " + name);
			uuids.put(id, name);
		});
		
		System.out.println("\\-- ANALYSE HASH CODES [ITEMS] --//");
	}
	
	public static void cleanup() {
		HashMap<Integer, String> nu = new HashMap<>();
		for(int id : uuids.keySet()) {
			String name= uuids.get(id);
			boolean checked = false;
			
			for(ResourcePackModel m : ModCraftAPI.resourcePackManager.getResourcePackModels()) {
				if(m.getName().equals(name)) {
					checked =true;
				}
			}

			if(!checked) {
				System.out.println(name + " has failed the cleanup test! It will not be loaded.");
			}else {
				nu.put(id, name);
			}
		}
		uuids.clear();
		nu.forEach(uuids::put);
	}
	
	public static int getHashCode(ResourcePackModel model) {
		String n = model.getName();
		AtomicInteger f = new AtomicInteger(-1);
		
		uuids.forEach((i,z) -> {
			if(z.equals(n)) {
				f.set(i);
			}
		});
		
		if(f.get() == -1) {
			System.out.println("Registering new hash code for \"" + n + "\" with id " + hashCodeEnd);
			uuids.put(hashCodeEnd, n);
			hashCodeEnd++;
			return hashCodeEnd;
		}
		return f.get();
	}
	

}
