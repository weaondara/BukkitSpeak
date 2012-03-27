package net.but2002.minecraft.BukkitSpeak.teamspeakEvent;

import net.but2002.minecraft.BukkitSpeak.BukkitSpeak;
import net.but2002.minecraft.BukkitSpeak.TeamspeakUser;

public class ClientlistEvent extends TeamspeakEvent {
	
	public ClientlistEvent(BukkitSpeak plugin, String msg) {
		super(plugin, msg);
		
		localKeys.add("clid");
		localKeys.add("cid");
		localKeys.add("client_database_id");
		parseLocalValues(msg);
		
		setUser(new TeamspeakUser(removeLocalKeys(msg)));
	}
	
	@Override
	protected void sendMessage() {}
}
