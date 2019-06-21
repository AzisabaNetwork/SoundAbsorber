package net.azisaba.soundabsorber;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PlayerSettingsManager {

	private static final String fileName = "PlayerSettings.yml";

	private static File dataFile = null;
	private static HashMap<UUID, Integer> absorbLevel = new HashMap<>();

	protected static void init(SoundAbsorber plugin) {
		dataFile = new File(plugin.getDataFolder(), fileName);
		load();
	}

	public static void setAbsorbLevel(Player player, Integer level) {
		absorbLevel.put(player.getUniqueId(), level);
	}

	public static int getAbsorbLevel(Player player) {
		if (absorbLevel.containsKey(player.getUniqueId())) {
			return absorbLevel.get(player.getUniqueId());
		}

		return 1;
	}

	public static void setAbsorbLevel(UUID uuid, Integer level) {
		absorbLevel.put(uuid, level);
	}

	public static int getAbsorbLevel(UUID uuid) {
		if (absorbLevel.containsKey(uuid)) {
			return absorbLevel.get(uuid);
		}

		return 1;
	}

	protected static void saveAllData() {
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(dataFile);

		for (UUID uuid : absorbLevel.keySet()) {
			int level = absorbLevel.get(uuid);
			if (level > 1) {
				conf.set(uuid.toString(), absorbLevel.get(uuid));
			} else {
				conf.set(uuid.toString(), null);
			}
		}

		try {
			conf.save(dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void load() {
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(dataFile);
		ConfigurationSection sec = conf.getConfigurationSection("");

		if (sec == null || sec.getKeys(false) == null) {
			return;
		}

		for (String key : sec.getKeys(false)) {
			UUID uuid = null;
			try {
				uuid = UUID.fromString(key);
			} catch (Exception e) {
				conf.set(key, 0);
				continue;
			}

			if (uuid != null) {
				absorbLevel.put(uuid, conf.getInt(key, 1));
			}
		}
	}
}
