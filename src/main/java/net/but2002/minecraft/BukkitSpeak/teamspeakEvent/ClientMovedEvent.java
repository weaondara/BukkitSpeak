package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.dthielke.herochat.Herochat;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;

public class ClientMovedEvent extends TeamspeakEvent {
	
	private HashMap<String, String> info;
	
	public ClientMovedEvent(HashMap<String, String> infoMap) {
		setUser(Integer.parseInt(infoMap.get("clid")));
		info = infoMap;
		BukkitSpeak.getClientList().asyncUpdateClient(Integer.parseInt(infoMap.get("clid")));
		
		if (getUser() == null) return;
		getUser().put("cid", infoMap.get("ctid"));
		performAction();
	}
	
	@Override
	protected void performAction() {
		if (getClientName().startsWith("Unknown from") || getClientType() != 0) return;
		if (!info.get("reasonid").equals("4")) {
			if (Integer.parseInt(info.get("ctid")) == BukkitSpeak.getQuery().getCurrentQueryClientChannelID()) {
				String m = BukkitSpeak.getStringManager().getMessage("ChannelEnter");
				if (m.isEmpty()) return;
				if (BukkitSpeak.useHerochat() && BukkitSpeak.getStringManager().getHerochatUsesEvents()) {
					// Send to Herochat channel
					String c = BukkitSpeak.getStringManager().getHerochatChannel();
					Herochat.getChannelManager().getChannel(c).announce(replaceValues(m, true));
				} else {
					for (Player pl : getOnlinePlayers()) {
						if (!isMuted(pl) && checkPermissions(pl, "channelenter")) {
							pl.sendMessage(replaceValues(m, true));
						}
					}
				}
				if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(replaceValues(m, false));
			} else {
				String m = BukkitSpeak.getStringManager().getMessage("ChannelLeave");
				if (m.isEmpty()) return;
				if (BukkitSpeak.useHerochat() && BukkitSpeak.getStringManager().getHerochatUsesEvents()) {
					// Send to Herochat channel
					String c = BukkitSpeak.getStringManager().getHerochatChannel();
					Herochat.getChannelManager().getChannel(c).announce(replaceValues(m, true));
				} else {
					for (Player pl : getOnlinePlayers()) {
						if (!isMuted(pl) && checkPermissions(pl, "channelleave")) {
							pl.sendMessage(replaceValues(m, true));
						}
					}
				}
				if (BukkitSpeak.getStringManager().getLogInConsole()) BukkitSpeak.log().info(replaceValues(m, false));
			}
		}
	}
}
