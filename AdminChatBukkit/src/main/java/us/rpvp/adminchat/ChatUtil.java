package us.rpvp.adminchat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import static org.bukkit.ChatColor.*;

public class ChatUtil {

	public static void sendStaffMessage(CommandSender sender, String message) {
		for(Player staff : Bukkit.getOnlinePlayers()) {
			if(staff.hasPermission("adminchat.receive")) {
				staff.sendMessage(DARK_GRAY + "[" + GOLD.toString() + "Staff Chat" + DARK_GRAY + "][" + YELLOW + AdminChat.getServerName() + DARK_GRAY + "] " + DARK_RED + sender.getName() + " " + DARK_GRAY + "» " + GREEN + translateAlternateColorCodes('&', message));
			}
		}
		Bukkit.getLogger().info("[Staff Chat] " + sender.getName() + " » " + message);
	}

	public static void sendBungeePayload(Player player, String message) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(b);
		try {
			out.writeUTF("AdminChat");
			out.writeUTF(AdminChat.getServerName());
			out.writeUTF(player.getName());
			out.writeUTF(message);
		} catch(Exception e) {
			e.printStackTrace();
		}
		player.sendPluginMessage(AdminChat.getInstance(), "BungeeCord", b.toByteArray());
	}

	public static String buildString(String[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		for(String word : args) {
			stringBuilder.append(ChatColor.translateAlternateColorCodes('&', word));
			stringBuilder.append(" ");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 1);
	}
}