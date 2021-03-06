package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.StringManager;

import org.bukkit.command.CommandSender;

public class SetServerListener extends SetProperty {
	
	private static final String PROPERTY = StringManager.TEAMSPEAK_SERVER;
	private static final String ALLOWED_INPUT = "true or false";
	private static final String DESCRIPTION = "If this is set to true, BukkitSpeak will notice when somebody "
			+ "joins or leaves the TS3 server.";
	private static final String[] TAB_SUGGESTIONS = {"true", "false"};
	
	@Override
	public String getProperty() {
		return PROPERTY;
	}
	
	@Override
	public String getAllowedInput() {
		return ALLOWED_INPUT;
	}
	
	@Override
	public String getDescription() {
		return DESCRIPTION;
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg) {
		if (arg.equalsIgnoreCase("true")) {
			getTsSection().set(StringManager.TEAMSPEAK_SERVER, true);
			send(sender, Level.INFO, "&aServer joins and quits will now be broadcasted in Minecraft.");
		} else if (arg.equalsIgnoreCase("false")) {
			getTsSection().set(StringManager.TEAMSPEAK_SERVER, false);
			send(sender, Level.INFO, "&aServer joins and quits won't be broadcasted in Minecraft anymore.");
		} else {
			send(sender, Level.WARNING, "Only 'true' or 'false' are accepted.");
			return false;
		}
		BukkitSpeak.getInstance().saveConfig();
		BukkitSpeak.getInstance().reloadStringManager();
		reloadListener();
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		if (args.length != 3) return null;
		List<String> al = new ArrayList<String>();
		for (String s : TAB_SUGGESTIONS) {
			if (s.startsWith(args[2].toLowerCase())) {
				al.add(s);
			}
		}
		return al;
	}
}
