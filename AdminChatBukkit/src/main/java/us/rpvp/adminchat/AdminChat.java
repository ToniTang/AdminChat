package us.rpvp.adminchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import static org.bukkit.ChatColor.*;

public class AdminChat extends JavaPlugin implements PluginMessageListener, Listener, CommandExecutor {

	String serverName;
	String subChannel = "AdminChat";
	String pluginChannel = "RPvPChannel";
	String PERMISSION_USE_COMMAND = "adminchat.use";
	String PERMISSION_RECEIVE_CHAT = "adminchat.receive";

	ArrayList<UUID> list = new ArrayList<>();

	public void onEnable() {
		saveDefaultConfig();

		serverName = getConfig().getString("serverName");

		getServer().getMessenger().registerOutgoingPluginChannel(this, pluginChannel);
		getServer().getMessenger().registerIncomingPluginChannel(this, pluginChannel, this);

		getServer().getPluginManager().registerEvents(this, this);

		getCommand("achat").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("achat")) {
			if(player.hasPermission(PERMISSION_USE_COMMAND) || player.isOp()) {
				if(args.length > 0) {
					String message = buildString(args);
					sendBungeePayload(player, message);
				} else {
					if(!isInAdminChat(player.getUniqueId())) {
						player.sendMessage(ChatColor.GREEN + "You have now switched to " + ChatColor.BOLD + "ADMIN CHAT");
						addPlayer(player.getUniqueId());
					} else {
						player.sendMessage(ChatColor.RED + "You have now switched to " + ChatColor.BOLD + "SERVER CHAT");
						removePlayer(player.getUniqueId());
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission for this!");
			}
			return true;
		}
		return false;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if(!channel.equalsIgnoreCase(pluginChannel)) {
			return;
		}

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
		try {
			String sub = in.readUTF();
			if(sub.equalsIgnoreCase(subChannel)) {
				sendStaffMessage(in.readUTF(), Bukkit.getPlayer(in.readUTF()), in.readUTF());
			}
		} catch(IOException e) {
			Bukkit.getLogger().log(Level.SEVERE, "Error receiving message", e);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if(isInAdminChat(event.getPlayer().getUniqueId())) {
			sendBungeePayload(event.getPlayer(), event.getMessage());
			event.setCancelled(true);
		}
	}

	public void sendBungeePayload(Player player, String message) {
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			out.writeUTF(subChannel);
			out.writeUTF(serverName);
			out.writeUTF(player.getName());
			out.writeUTF(message);
			player.sendPluginMessage(this, pluginChannel, b.toByteArray());
			System.out.print(b.toString());
		} catch(Exception e) {
			Bukkit.getLogger().log(Level.SEVERE, "Error sending message", e);
		}
	}

	public void sendStaffMessage(String serverName, CommandSender sender, String message) {
		for(Player staff : Bukkit.getOnlinePlayers()) {
			if(staff.hasPermission(PERMISSION_RECEIVE_CHAT) || sender.isOp()) {
				staff.sendMessage(DARK_GRAY + "[" + GOLD.toString() + "Staff Chat" + DARK_GRAY + "][" + YELLOW + serverName + DARK_GRAY + "] " + DARK_RED + sender.getName() + " " + DARK_GRAY + "» " + GREEN + translateAlternateColorCodes('&', message));
			}
		}
		Bukkit.getLogger().info("[Staff Chat] " + sender.getName() + " » " + message);
	}

	public String buildString(String[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		for(String word : args) {
			stringBuilder.append(ChatColor.translateAlternateColorCodes('&', word));
			stringBuilder.append(" ");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 1);
	}

	public boolean isInAdminChat(UUID player) {
		return list.contains(player);
	}

	public void addPlayer(UUID player) {
		list.add(player);
	}

	public void removePlayer(UUID player) {
		if(!list.isEmpty())
			list.remove(player);
	}
}