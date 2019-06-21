package net.azisaba.soundabsorber;

import java.util.UUID;

import org.bukkit.entity.Player;

import net.azisaba.playersettings.PlayerSettings;
import net.azisaba.playersettings.util.SettingsData;

public class PlayerSettingsManager {

	private static final String key = "SoundAbsorber.Level";

	public static void setAbsorbLevel(Player player, Integer level) {
		setAbsorbLevel(player.getUniqueId(), level);
	}

	public static int getAbsorbLevel(Player player) {
		return getAbsorbLevel(player.getUniqueId());
	}

	public static void setAbsorbLevel(UUID uuid, Integer level) {
		PlayerSettings.getPlugin().getManager().getSettingsData(uuid).set(key, level);
	}

	public static int getAbsorbLevel(UUID uuid) {
		SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(uuid);
		if (!data.isSet(key)) {
			return 1;
		}

		return data.getInt(key);
	}
}
