package us.rpvp.adminchat;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class AdminChat extends JavaPlugin implements PluginMessageListener {

	private static String serverName;
	private static AdminChat instance;

	public void onEnable() {
		saveDefaultConfig();

		instance = this;
		serverName = getConfig().getString("serverName");

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

		getServer().getPluginManager().registerEvents(new AdminChatEvent(), this);

		getCommand("achat").setExecutor(new AdminChatCmd());
	}

	public void onDisable() {
		instance = null;
	}

	public static AdminChat getInstance() {
		return instance;
	}

	public static String getServerName()  {
		return serverName;
	}


	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if(!channel.equals("BungeeCord"))
			return;

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		try {
			String subchannel = in.readUTF();
			if(subchannel.equals("AdminChat")) {
				System.out.print("Player Name: " + in.readUTF());
				System.out.print("Message Sent: " + in.readUTF());
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}