package us.rpvp.adminchat;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerList {

	ArrayList<UUID> list = new ArrayList<>();

	private static PlayerList instance;

	public static PlayerList getInstance() {
		if(instance == null)
			instance = new PlayerList();
		return instance;
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