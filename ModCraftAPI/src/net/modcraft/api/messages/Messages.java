package net.modcraft.api.messages;

import org.bukkit.command.Command;

import net.modcraft.api.ModCraftAPI;
import net.modcraft.api.messages.StringCreator.Callback;

public class Messages {
	
	public static final String MODCRAFT_GREETINGS = ModCraftAPI.prefix + "§aDieser Server nutzt ModCraft Version §e§n" + ModCraftAPI.version + "§2.";
	public static final String RESOURCEPACK_KICK_MESSAGE = ModCraftAPI.prefix + "§cAktiviere deine §nServer-Resourcepacks§c,\num ModCraft richtig nutzen zu können§4!";
	public static final String COMMAND_NO_PERMISSION = ModCraftAPI.prefix + "§cDu besitzt keine Berechtigung§4.";
	public static final String SENDER_IS_PLAYER = ModCraftAPI.prefix + "§cDu bist kein Spieler§4.";
	public static final String RESOURCEPACK_SEND = ModCraftAPI.prefix + "§aDir wurde das Resourcepack gesendet§2.";
	public static final String RESOURCEPACK_ERROR = ModCraftAPI.prefix + "§cDas Resourcepack konnte nicht gesendet werden§4.";
	public static final String EXPORT_CONFIG_SUCCESS = ModCraftAPI.prefix + "§aDie Config wurde exportiert§2.";
	public static final String IMPORT_CONFIG_SUCCESS = ModCraftAPI.prefix + "§aDie Config wurde importiert§2.";

	public static final StringCreator<Command> GET_COMMAND_USAGE = new StringCreator<Command>(new Callback<String, Command>() {
		
		@Override
		public String callback(Command e) {
			return ModCraftAPI.prefix + "§c"+e.getUsage();
		}
		
	});
	
	public static final StringCreator<String> NOT_FOUND = new StringCreator<String>(new Callback<String, String>() {
		
		@Override
		public String callback(String e) {
			return ModCraftAPI.prefix + "§c\"" + e + "\" wurde nicht gefunden";
		}
		
	});

}
