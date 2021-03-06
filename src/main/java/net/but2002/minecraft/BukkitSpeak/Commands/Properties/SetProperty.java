package net.but2002.minecraft.BukkitSpeak.Commands.Properties;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.StringManager;
import net.but2002.minecraft.BukkitSpeak.Commands.BukkitSpeakCommand;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;

public abstract class SetProperty {
	
	protected void send(CommandSender sender, Level level, String msg) {
		String m = msg;
		if (sender instanceof Player) {
			m = m.replaceAll("((&|$)([a-fk-orA-FK-OR0-9]))", "\u00A7$3");
			sender.sendMessage(BukkitSpeak.getFullName() + m);
		} else {
			m = m.replaceAll("((&|$|\u00A7)([a-fk-orA-FK-OR0-9]))", "");
			BukkitSpeak.log().log(level, m);
		}
	}
	
	protected ConfigurationSection getTsSection() {
		return BukkitSpeak.getInstance().getConfig().getConfigurationSection(StringManager.TEAMSPEAK_SECTION);
	}

	protected void reloadListener() {
		BukkitSpeak.getQuery().removeAllEvents();
		
		if (BukkitSpeak.getStringManager().getUseServer()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_SERVER, 0);
		}
		if (BukkitSpeak.getStringManager().getUseTextServer()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0);
		}
		if (BukkitSpeak.getStringManager().getUseChannel()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_CHANNEL,
					BukkitSpeak.getStringManager().getChannelID());
		}
		if (BukkitSpeak.getStringManager().getUseTextChannel()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTCHANNEL,
					BukkitSpeak.getStringManager().getChannelID());
		}
		if (BukkitSpeak.getStringManager().getUsePrivateMessages()) {
			BukkitSpeak.getQuery().addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTPRIVATE, 0);
		}
	}
	
	protected void broadcastMessage(String mcMsg, CommandSender sender) {
		if (mcMsg == null || mcMsg.isEmpty()) return;
		for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
			if (!BukkitSpeak.getMuted(pl)) {
				pl.sendMessage(BukkitSpeakCommand.convertToMinecraft(mcMsg, true,
						BukkitSpeak.getStringManager().getAllowLinks()));
			}
		}
		if (!(sender instanceof Player) || (BukkitSpeak.getStringManager().getLogInConsole())) {
			BukkitSpeak.log().info(BukkitSpeakCommand.convertToMinecraft(mcMsg, false,
					BukkitSpeak.getStringManager().getAllowLinks()));
		}
	}
	
	protected void sendChannelChangeMessage(CommandSender sender) {
		String m = BukkitSpeak.getStringManager().getMessage("ChannelChange");
		String name, displayName;
		if (sender instanceof Player) {
			name = ((Player) sender).getName();
			displayName = ((Player) sender).getDisplayName();
		} else {
			name = BukkitSpeakCommand.convertToMinecraft(BukkitSpeak.getStringManager().getConsoleName(), false, false);
			displayName = BukkitSpeak.getStringManager().getConsoleName();
		}
		HashMap<String, String> info = BukkitSpeak.getQuery().getInfo(JTS3ServerQuery.INFOMODE_CHANNELINFO,
				BukkitSpeak.getQuery().getCurrentQueryClientChannelID());
		m = m.replaceAll("%channel%", info.get("channel_name"));
		m = m.replaceAll("%description%", info.get("channel_description"));
		m = m.replaceAll("%topic%", info.get("channel_topic"));
		m = m.replaceAll("%player_name%", name);
		m = m.replaceAll("%player_displayname%", displayName);
		
		if (m.isEmpty()) return;
		broadcastMessage(m, sender);
	}
	
	protected void connectChannel(CommandSender sender) {
		int cid = BukkitSpeak.getQuery().getCurrentQueryClientChannelID();
		int clid = BukkitSpeak.getQuery().getCurrentQueryClientID();
		String pw = BukkitSpeak.getStringManager().getChannelPass();
		if (!BukkitSpeak.getQuery().moveClient(clid, cid, pw)) {
			send(sender, Level.WARNING, "&4The channel ID could not be set.");
			send(sender, Level.WARNING, "&4Ensure that the ChannelID is really assigned to a valid channel.");
			send(sender, Level.WARNING, "&4" + BukkitSpeak.getQuery().getLastError());
			return;
		}
		sendChannelChangeMessage(sender);
	}
	
	public abstract String getProperty();
	public abstract String getAllowedInput();
	public abstract String getDescription();
	public abstract boolean execute(CommandSender sender, String arg);
	public abstract List<String> onTabComplete(CommandSender sender, String[] args);
}
