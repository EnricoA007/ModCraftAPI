package net.modcraft.api.commands;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.config.Config;
import net.modcraft.api.main.ModCraftMain;
import net.modcraft.api.messages.Messages;

public class ConfigCommand implements CommandExecutor {

	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {
		if(s.hasPermission("modcraft.config")) {
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("export")) {
					ModCraftAPI.config.exportFile();
					s.sendMessage(Messages.EXPORT_CONFIG_SUCCESS);
				}else if(args[0].equalsIgnoreCase("import")) {
					try {
						System.out.println("Importing config.json file...");
						FileInputStream in = new FileInputStream(ModCraftMain.config);
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
					s.sendMessage(Messages.IMPORT_CONFIG_SUCCESS);
				}else {
					s.sendMessage(Messages.GET_COMMAND_USAGE.str(c));
				}
			}else {
				s.sendMessage(Messages.GET_COMMAND_USAGE.str(c));
			}
		}else {
			s.sendMessage(Messages.COMMAND_NO_PERMISSION);
		}
		return true;
	};
	
}
