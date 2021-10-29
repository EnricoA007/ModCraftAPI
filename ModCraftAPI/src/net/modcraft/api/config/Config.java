package net.modcraft.api.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.bukkit.Bukkit;

import com.google.gson.JsonObject;

import net.modcraft.api.database.Database;

public abstract class Config {
	
	private HashMap<String, String> kv = new HashMap<>();
	private Thread t = null;
	private int time = 0;
	
	protected Config() {
		
	}
	
	protected Config(String s) {
		JsonObject obj =Database.gsonPretty.fromJson(s, JsonObject.class);
		obj.entrySet().forEach(e -> {
			Bukkit.broadcastMessage("§7" + e.getKey() + " " + e.getValue().getAsString());
			set(e.getKey(), e.getValue().getAsString());
		});
	}
	
	public String getOrDefault(String key, String defaultWord) {
		return (String) getOrDefault(key, defaultWord, Type.STRING);
	}
	
	public Object getOrDefault(String key, String defaultWord, Type type) {
		if(kv.containsKey(key)) {
			switch(type) {
				case BOOLEAN:
					return getAsBoolean(key);
				case INTEGER:
					return getAsInt(key);
				case STRING:
					return getAsString(key);
			}
		}
		set(key, defaultWord);
		startTimer();
		return getOrDefault(key, defaultWord, type);
	}
	
	public String getAsString(String s) {
		if(kv.containsKey(s)) return kv.get(s);
		return "config_default";
	}
	
	public boolean getAsBoolean(String s) {
		if(kv.containsKey(s)) return Boolean.parseBoolean(kv.get(s));
		return false;
	}
	
	public int getAsInt(String s) {
		if(kv.containsKey(s)) return Integer.parseInt(kv.get(s));
		return -1;
	}
	
	public boolean contains(String s) {
		return kv.containsKey(s);
	}
	
	public void set(String k, String v) {
		if(kv.containsKey(k)) kv.remove(k);
		kv.put(k, v);
	}
	 
	public String export() {
		JsonObject obj = new JsonObject();
		
		kv.forEach((k,v) -> {
			obj.addProperty(k, v);
		});
		
		return Database.gsonPretty.toJson(obj, JsonObject.class);
	}
	
	public abstract void exportFile();
	
	@SuppressWarnings("deprecation")
	private void stopThread() {
		this.t.stop();
		this.t =null;
	}
	
	private void startTimer() {
		if(t == null) {
			t = new Thread(() -> {
				while(t != null) {
					try {
						Thread.sleep(1000);
						time++;
					}catch(Exception ex) {}
					if(time == 5) {
						exportFile();
						stopThread();
					}
				}
			});
			t.start();
		}else {
			time = 0;
		}
	}
	
	public static Config create(String s, File f) {
		return new Config(s) {
			
			@Override
			public void exportFile() {
				try {
					System.out.println("Overwrite config.json...");
					FileOutputStream fout = new FileOutputStream(f);
					fout.write(this.export().getBytes(StandardCharsets.UTF_8));
					fout.flush();
					fout.close();
					System.out.println("Overwrited.");
				}catch(IOException e) {
					System.err.println("An error is occured while creating config file \"" + e + "\"");
				}	
			}
			
		};
	}
	
	public static Config createEmpty(File f) {
		return new Config() {
			@Override
			public void exportFile() {
				try {
					System.out.println("Overwrite config.json...");
					FileOutputStream fout = new FileOutputStream(f);
					fout.write(this.export().getBytes(StandardCharsets.UTF_8));
					fout.flush();
					fout.close();
					System.out.println("Overwrited.");
				}catch(IOException e) {
					System.err.println("An error is occured while creating config file \"" + e + "\"");
				}	
			}
		};
	}
	
	public static enum Type {
		STRING,
		INTEGER,
		BOOLEAN
	}

}
