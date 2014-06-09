package us.rpvp.adminchat;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class AdminChatBungee extends Plugin implements Listener {

	String pluginChannel = "RPvPChannel";

	public void onEnable() {
		getProxy().registerChannel(pluginChannel);
		ProxyServer.getInstance().getPluginManager().registerListener(this, this);
	}

	@EventHandler
	public void onMessageReceived(PluginMessageEvent event) {
		if(!event.getTag().equalsIgnoreCase(pluginChannel)) {
			return;
		}
		System.out.print(pluginChannel + " received a message!");
		for(ServerInfo server : ProxyServer.getInstance().getServers().values()) {
			if(!server.getPlayers().isEmpty())
				server.sendData(pluginChannel, event.getData());
		}
	}
}