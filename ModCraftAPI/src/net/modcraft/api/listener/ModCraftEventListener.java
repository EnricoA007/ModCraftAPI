package net.modcraft.api.listener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;
import org.bukkit.event.world.ChunkLoadEvent;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.block.BlockElement;
import net.modcraft.api.config.Config.Type;
import net.modcraft.api.main.ModCraftMain;
import net.modcraft.api.messages.Messages;

public class ModCraftEventListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {		
		Bukkit.getScheduler().runTaskLater(ModCraftMain.plugin, new Runnable() {
			@Override
			public void run() {
				try {
					Bukkit.broadcastMessage("§d" + ModCraftAPI.config.getOrDefault("Resourcepack-Address", "127.0.0.1:99"));
					e.getPlayer().setResourcePack("http://" + ModCraftAPI.config.getOrDefault("Resourcepack-Address", "127.0.0.1:99") + "/", MessageDigest.getInstance("SHA-1").digest(ModCraftAPI.getResourcePack()));
				} catch (NoSuchAlgorithmException e) {}	
			}
		}, 60L);
	}
	
	@EventHandler
	public void onResourcePack(PlayerResourcePackStatusEvent e) {
		if(e.getStatus() == Status.DECLINED) {
			if(((boolean)ModCraftAPI.config.getOrDefault("Resourcepack-Kick-Enabled", "true", Type.BOOLEAN)))
				e.getPlayer().kickPlayer(Messages.RESOURCEPACK_KICK_MESSAGE);
		}else if(e.getStatus() == Status.SUCCESSFULLY_LOADED){
			if(((boolean)ModCraftAPI.config.getOrDefault("Greetings", "true", Type.BOOLEAN)))
				e.getPlayer().sendMessage(Messages.MODCRAFT_GREETINGS);
		}
	}
	
	@EventHandler
	public void onRubySpawn(ChunkLoadEvent e) {
		if(!(boolean)ModCraftAPI.config.getOrDefault("RubyOreSpawn", "false", Type.BOOLEAN)) return;

		Chunk c =e.getChunk();
		for(int x = c.getX(); x<c.getX()+16; x++) {
			for(int z = c.getZ(); z<c.getZ()+16; z++) {
				for(int y = 0; y<255; y++) {
					Block b =e.getWorld().getBlockAt(x,y,z);
					if(b.getType() == Material.DIAMOND_ORE) {	
						if(new Random().nextInt(25) == 1) {
							net.modcraft.api.block.Block block = ModCraftAPI.getBlockByName("RubyOre");
							block.onPlaceBlock(new BlockElement(b.getLocation(), block, null), null);
						}
					}
				}
			}	
		}
	}
	
}
