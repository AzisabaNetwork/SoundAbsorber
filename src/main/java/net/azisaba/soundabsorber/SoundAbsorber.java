package net.azisaba.soundabsorber;

import java.io.File;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.azisaba.playersettings.PlayerSettings;
import net.azisaba.playersettings.util.SettingsData;
import net.azisaba.playersettings.util.SettingsManager;
import net.azisaba.soundabsorber.commands.SoundAbsorberCommand;
import net.md_5.bungee.api.ChatColor;

public class SoundAbsorber extends JavaPlugin {

	@Override
	public void onEnable() {

		convertOldFile();

		Bukkit.getPluginCommand("soundabsorber").setExecutor(new SoundAbsorberCommand());
		Bukkit.getPluginCommand("soundabsorber").setName(ChatColor.RED + "権限がないようです。運営に報告してください。");

		SoundAbsorbListener.register(this);

		Bukkit.getLogger().info(getName() + " enabled.");
	}

	@Override
	public void onDisable() {
		SoundAbsorbListener.unregister();
		Bukkit.getLogger().info(getName() + " disabled.");
	}

	private void convertOldFile() {
		File old = new File(getDataFolder(), "PlayerSettings.yml");

		if (!old.exists()) {
			return;
		}

		YamlConfiguration conf = YamlConfiguration.loadConfiguration(old);

		if (conf.getConfigurationSection("") == null) {
			return;
		}

		SettingsManager manager = PlayerSettings.getPlugin().getManager();

		for (String key : conf.getConfigurationSection("").getKeys(false)) {
			UUID uuid = null;

			try {
				uuid = UUID.fromString(key);
			} catch (Exception e) {
				continue;
			}

			SettingsData data = manager.getSettingsData(uuid);

			if (data.isSet("SoundAbsorber.Percentage")) {
				continue;
			}

			double newValue = (50 - conf.getInt(key)) * 2;
			data.set("SoundAbsorber.Percentage", newValue);
		}

		old.delete();
	}
}
