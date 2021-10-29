package net.modcraft.api.database;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Database {
	
	public static Gson gson = new GsonBuilder().create();
	public static Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
	
	public static HashMap<String, HashMap<String, String>> map = new HashMap<>();
	
	public static HashMap<String, String> getDatabase(String a) {
		if(!map.containsKey(a)) {
			registerDatabase(a);
		}
		return map.get(a);
	}
	
	public static void registerDatabase(String s) {
		if(!map.containsKey(s)) {
			map.put(s, new HashMap<>());
		}
	}
	
	public static void unregisterDatabase(String s) {
		if(map.containsKey(s)) {
			map.remove(s, new HashMap<>());
		}
	}
	
	public static boolean hasRegisteredDatabase(String s) {
		return map.containsKey(s);
	}
	
	public static void importDatabase(String s) {
		JsonArray base = gsonPretty.fromJson(s, JsonArray.class);
		
		if(base.size() == 0) return;
		
		base.forEach(e -> {
			JsonObject o = e.getAsJsonObject();
			String title = o.get("title").getAsString();
			JsonArray elements = o.get("elements").getAsJsonArray();
			
			HashMap<String, String> m = new HashMap<>();
			
			elements.forEach(l -> {
				JsonObject b =l.getAsJsonObject();
				m.put(b.get("K").getAsString(), b.get("V").getAsString());
			});
			
			map.put(title,m);
		});
		
	}
	
	public static String exportDatabase() {
		JsonArray base = new JsonArray();
		
		for(String name : map.keySet()) {
			JsonObject bs = new JsonObject();
			
			HashMap<String, String> kv =map.get(name);
			
			JsonArray a = new JsonArray();
			
			kv.forEach((k,v) -> {
				JsonObject kve = new JsonObject();
				
				kve.addProperty("K", k);
				kve.addProperty("V", v);

				a.add(kve);
			});
			
			bs.addProperty("title", name);
			bs.add("elements", a);
			
			base.add(bs);
		}
		
		return gsonPretty.toJson(base);
	}
	
	public static String fromLocation(Location loc) {
		return loc.getWorld().getName() + ":" + loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getPitch() + ":" + loc.getYaw();
	}
	
	public static Location toLocation(String s) {
		String[] p = s.split(":");
		return new Location(Bukkit.getWorld(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]), Double.parseDouble(p[3]), Float.parseFloat(p[4]), Float.parseFloat(p[5]));
	}

}
