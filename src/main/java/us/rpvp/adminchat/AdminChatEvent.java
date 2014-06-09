package us.rpvp.adminchat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AdminChatEvent implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if(PlayerList.getInstance().isInAdminChat(player.getUniqueId())) {
			ChatUtil.sendStaffMessage(player, event.getMessage());
			event.setCancelled(true);
		}
	}
}