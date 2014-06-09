package us.rpvp.adminchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class AdminChatCmd implements CommandExecutor {

	AdminChat plugin = AdminChat.getInstance();
	PlayerList playerList = PlayerList.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("achat")) {
			if(player.hasPermission("adminchat.use") || player.isOp()) {
				if(args.length > 0) {
					String message = ChatUtil.buildString(args);
					ChatUtil.sendStaffMessage(player, message);

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

					player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());

					/** Debug **/
					System.out.print("[Staff Chat][" + b.toByteArray()[1] + "] " + b.toByteArray()[2] + " » " + b.toByteArray()[3]);
					System.out.print(b.toString());
					/** End of Debug **/
				} else {
					if(!playerList.isInAdminChat(player.getUniqueId())) {
						player.sendMessage(ChatColor.GREEN + "You have now switched to " + ChatColor.BOLD + "ADMIN CHAT");
						playerList.addPlayer(player.getUniqueId());
					} else {
						player.sendMessage(ChatColor.RED + "You have now switched to " + ChatColor.BOLD + "SERVER CHAT");
						playerList.removePlayer(player.getUniqueId());
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permission for this!");
			}
			return true;
		}
		return false;
	}
}