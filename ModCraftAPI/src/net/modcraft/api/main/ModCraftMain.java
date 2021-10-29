package net.modcraft.api.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.block.blocks.AdvancedWorkbench;
import net.modcraft.api.block.blocks.ExampleBlock;
import net.modcraft.api.block.blocks.LuckyBlock;
import net.modcraft.api.block.blocks.RubyOre;
import net.modcraft.api.block.blocks.TravelChest;
import net.modcraft.api.commands.ConfigCommand;
import net.modcraft.api.commands.GiveCommand;
import net.modcraft.api.commands.ResourcePackCommand;
import net.modcraft.api.config.Config;
import net.modcraft.api.database.Database;
import net.modcraft.api.item.ItemBuilder;
import net.modcraft.api.item.Items;
import net.modcraft.api.item.RubyItem;
import net.modcraft.api.listener.ModCraftBlockListener;
import net.modcraft.api.listener.ModCraftEventListener;
import net.modcraft.api.web.FileWebServer;

public class ModCraftMain extends JavaPlugin {

	public static Plugin plugin;
	public static File database = new File("ModCraft/database.db");
	public static File resources = new File("ModCraft/resources/");
	public static File uniqueIDs = new File("ModCraft/uuid/");
	public static File items = new File(uniqueIDs + "/Items.db");
	public static File config =new File("ModCraft/config.json");
	
	@Override
	public void onEnable() {

		plugin=this;
		this.getServer().getPluginManager().registerEvents(new ModCraftEventListener(), this);
		this.getServer().getPluginManager().registerEvents(new ModCraftBlockListener(), this);

		this.getCommand("mgive").setExecutor(new GiveCommand());
		this.getCommand("mgive").setTabCompleter(new GiveCommand());

		this.getCommand("resourcepack").setExecutor(new ResourcePackCommand());
		this.getCommand("config").setExecutor(new ConfigCommand());
		
		if(!resources.exists()) resources.mkdirs();
		if(!uniqueIDs.exists()) uniqueIDs.mkdirs();
		
		if(!config.exists()) {
			
			try {
				System.out.println("Creating config.json...");
				FileOutputStream fout = new FileOutputStream(ModCraftMain.config);
				fout.write(ModCraftAPI.config.export().getBytes(StandardCharsets.UTF_8));
				fout.close();
				System.out.println("Created.");
			}catch(IOException e) {
				System.err.println("An error is occured while creating config file \"" + e + "\"");
				System.err.println("Stop server...");
				Bukkit.shutdown();
			}
			
		}else {
			try {
				System.out.println("Importing config.json file...");
				FileInputStream in = new FileInputStream(config);
				byte[] a = new byte[in.available()];
				in.read(a);
				in.close();
				System.out.println("Importing config.json completed.");
				ModCraftAPI.config = Config.create(new String(a,StandardCharsets.UTF_8), ModCraftMain.config);
			} catch (IOException e) {
				System.err.println("An error is occured while reading config.json file \"" + e + "\"");
				System.err.println("Stop server...");
				Bukkit.shutdown();
			}
		}
		
		if(items.exists()) {
			try {
				System.out.println("Import item UUIDs file...");
				FileInputStream in = new FileInputStream(items);
				byte[] a = new byte[in.available()];
				in.read(a);
				in.close();
				System.out.println("Importing completed.");
				Items.importHashCodes(new String(a,StandardCharsets.UTF_8));
			} catch (IOException e) {
				System.err.println("An error is occured while reading database file \"" + e + "\"");
				System.err.println("Stop server...");
				Bukkit.shutdown();
			}
		}else {
			try {
				System.out.println("Create hash code file...");
				FileOutputStream out = new FileOutputStream(items);
				out.write(Items.exportHashCodes().getBytes(StandardCharsets.UTF_8));
				out.flush();
				out.close();
				System.out.println("Created.");
			} catch (IOException e) {
				System.err.println("An error is occured while reading database file \"" + e + "\"");
				System.err.println("Stop server...");
				Bukkit.shutdown();
			}
		}
		
		if(!database.exists()) {
			try {
				System.out.println("Creating database file...");
				FileOutputStream out = new FileOutputStream(database);
				out.write(Database.exportDatabase().getBytes(StandardCharsets.UTF_8));
				out.flush();
				out.close();
				System.out.println("Created.");
			} catch (IOException e) {
				System.err.println("An error is occured while creating database file \"" + e + "\"");
			}
		}else {
			try {
				System.out.println("Import database file...");
				FileInputStream in = new FileInputStream(database);
				byte[] a = new byte[in.available()];
				in.read(a);
				in.close();
				Database.importDatabase(new String(a,StandardCharsets.UTF_8));
				System.out.println("Importing completed.");
			} catch (IOException e) {
				System.err.println("An error is occured while reading database file \"" + e + "\"");
			}
		}

		ModCraftAPI.registerBlock(new LuckyBlock());
		ModCraftAPI.registerBlock(new TravelChest());
		ModCraftAPI.registerBlock(new ExampleBlock());
		ModCraftAPI.registerBlock(new AdvancedWorkbench());
		ModCraftAPI.registerBlock(new RubyOre());
		
		ModCraftAPI.registerItem("luckyblock", new ItemBuilder("luckyblock").build());
		ModCraftAPI.registerItem("travelchest", new ItemBuilder("travelchest").build());
		ModCraftAPI.registerItem("exampleblock", new ItemBuilder("exampleblock").build());
		ModCraftAPI.registerItem("advancedworkbench", new ItemBuilder("advancedworkbench").build());
		ModCraftAPI.registerItem("ruby_ore", new ItemBuilder("RubyOre").build());
		ModCraftAPI.registerItem(new RubyItem());
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Creating resourcepack...");
				
				ModCraftAPI.setResourcePackData(ModCraftAPI.resourcePackManager.createResourcePack());
				if(ModCraftAPI.fileWebServer == null) {
					ModCraftAPI.fileWebServer = new FileWebServer(ModCraftAPI.getResourcePack(), Integer.parseInt(ModCraftAPI.config.getOrDefault("Resourcepack-Address", "127.0.0.1:99").split(":")[1]), "pack.zip");
				}
				
				System.out.println("Created.");
				
				System.out.println("Cleanup UUIDs...");
				Items.cleanup();
				System.out.println("Cleaned up.");
			}
			
		}, 3*20L);
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		
		try {
			ModCraftAPI.fileWebServer.getServer().close();
			ModCraftAPI.fileWebServer.stop();
		} catch (Exception e) {}
		
		try {
			System.out.println("Overwrite database file...");
			FileOutputStream out = new FileOutputStream(database);
			out.write(Database.exportDatabase().getBytes(StandardCharsets.UTF_8));
			out.flush();
			out.close();
			System.out.println("Overwrited.");
		} catch (IOException e) {
			System.err.println("An error is occured while overwriting database file \"" + e + "\"");
		}
		
		try {
			System.out.println("Overwrite hash code file...");
			FileOutputStream out = new FileOutputStream(items);
			out.write(Items.exportHashCodes().getBytes(StandardCharsets.UTF_8));
			out.flush();
			out.close();
			System.out.println("Overwrited.");
		} catch (IOException e) {
			System.err.println("An error is occured while overwriting hash code uuids file \"" + e + "\"");
		}
		
	}
	
}
