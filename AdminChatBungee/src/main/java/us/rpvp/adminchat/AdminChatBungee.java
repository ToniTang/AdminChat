package us.rpvp.adminchat;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class AdminChatBungee extends Plugin implements Listener {

	public void onEnable() {
		ProxyServer.getInstance().getPluginManager().registerListener(this, this);
	}

	@EventHandler
	public void onMessageReceived(PluginMessageEvent event) {
		if(event.getData() != null) {
			for(ServerInfo server : ProxyServer.getInstance().getServers().values()) {
				server.sendData("BungeeCord", event.getData());
			}
		}
	}
}