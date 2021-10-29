package net.modcraft.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.item.Items;
import net.modcraft.api.messages.Messages;

public class GiveCommand implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command c, String arg2, String[] args) {
		
		if(s.hasPermission("modcraft.item.give")) {

			if(args.length == 2) {
				
			}else if(args.length == 1) {
				args[0] = args[0].toLowerCase();
				if(Items.itemMap.containsKey(args[0])) {
					if(s instanceof Player) {
						ItemStack i = Items.itemMap.get(args[0]);
						((Player)s).getInventory().addItem(i);
						s.sendMessage(ModCraftAPI.prefix + "§aDir wurde §61x §3\"§b" + args[0] + "§3\" §agegeben");
					}else {
						s.sendMessage(Messages.SENDER_IS_PLAYER);
					}
				}else {
					s.sendMessage(Messages.NOT_FOUND.str(args[0]));
				}
			}else {
				s.sendMessage(Messages.GET_COMMAND_USAGE.str(c));
			}
			
		}else {
			s.sendMessage(Messages.COMMAND_NO_PERMISSION);
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] args) {
		ArrayList<String> ls = new ArrayList<>();
		
		if(s.hasPermission("modcraft.item.give")) {
			
			if(args.length == 1) {
				String t = args[0];
				Items.itemMap.keySet().stream().forEach(a -> {
					if(a.startsWith(t)) {
						ls.add(a);
					}
				});
			}
			
		}
		
		return ls;
		
	}
	
}
