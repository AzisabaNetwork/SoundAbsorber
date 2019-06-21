package net.azisaba.soundabsorber;

import java.util.UUID;

import org.bukkit.entity.Player;

import net.azisaba.playersettings.PlayerSettings;
import net.azisaba.playersettings.util.SettingsData;

public class SoundPercentageContainer {

	private static final String key = "SoundAbsorber.Percentage";

	public static void setPercentage(Player player, double percentage) {
		setPercentage(player.getUniqueId(), percentage);
	}

	public static double getAbsorbLevel(Player player) {
		return getPercentage(player.getUniqueId());
	}

	public static void setPercentage(UUID uuid, double percentage) {
		PlayerSettings.getPlugin().getManager().getSettingsData(uuid).set(key, percentage);
	}

	public static double getPercentage(UUID uuid) {
		SettingsData data = PlayerSettings.getPlugin().getManager().getSettingsData(uuid);
		if (!data.isSet(key)) {
			return 100;
		}

		return data.getDouble(key);
	}
}
