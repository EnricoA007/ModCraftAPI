package net.modcraft.api.commands;

import java.security.MessageDigest;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.messages.Messages;

public class ResourcePackCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command c, String arg2, String[] args) {
	
		if(s instanceof Player) {
			try {
				((Player)s).setResourcePack("http://" + ModCraftAPI.config.getOrDefault("Resourcepack-Address", "127.0.0.1:99") + "/", MessageDigest.getInstance("SHA-1").digest(ModCraftAPI.getResourcePack()));
				s.sendMessage(Messages.RESOURCEPACK_SEND);
			}catch(Exception ex) {
				s.sendMessage(Messages.RESOURCEPACK_ERROR);
			}
		}
		
		return true;
	}

}
