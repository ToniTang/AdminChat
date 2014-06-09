package us.rpvp.adminchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminChatCmd implements CommandExecutor {

	PlayerList playerList = PlayerList.getInstance();

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("achat")) {
			if(player.hasPermission("adminchat.use") || player.isOp()) {
				if(args.length > 0) {
					String message = ChatUtil.buildString(args);
					ChatUtil.sendBungeePayload(player, message);
					ChatUtil.sendStaffMessage(player, message);
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