package net.but2002.minecraft.BukkitSpeak.Commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;

public class CommandAdminHelp extends BukkitSpeakCommand {
	
	private static final String[] NAMES = {"help", "adminhelp"};
	
	@Override
	public String getName() {
		return NAMES[0];
	}
	
	@Override
	public String[] getNames() {
		return NAMES;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		send(sender, Level.INFO, "&2Admin Commands Help");
		if (checkCommandPermission(sender, "ban"))
			send(sender, Level.INFO, "&e/tsa ban <target> (reason) &2- Bans a client.");
		if (checkCommandPermission(sender, "kick"))
			send(sender, Level.INFO, "&e/tsa kick <target> (reason) &2- Kicks from the TS.");
		if (checkCommandPermission(sender, "channelkick"))
			send(sender, Level.INFO, "&e/tsa channelkick <target> (reason) &2- "
					+ "Kicks from the channel and moves the client to the default channel.");
		if (checkCommandPermission(sender, "set"))
			send(sender, Level.INFO, "&e/tsa set (property) (value) &2- Change BukkitSpeak's config.");
		if (checkCommandPermission(sender, "status"))
			send(sender, Level.INFO, "&e/tsa status &2- Shows some info about BukkitSpeak.");
		if (checkCommandPermission(sender, "reload"))
			send(sender, Level.INFO, "&e/tsa reload &2- Reloads the config and the query.");
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
