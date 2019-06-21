package net.azisaba.soundabsorber;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.azisaba.soundabsorber.commands.SoundAbsorberCommand;
import net.azisaba.soundabsorber.listeners.SettingGUIListener;
import net.md_5.bungee.api.ChatColor;

public class SoundAbsorber extends JavaPlugin {

	@Override
	public void onEnable() {

		Bukkit.getPluginCommand("soundabsorber").setExecutor(new SoundAbsorberCommand());
		Bukkit.getPluginCommand("soundabsorber").setName(ChatColor.RED + "権限がないようです。運営に報告してください。");

		PlayerSettingsManager.init(this);
		SettingsGUIManager.init();

		Bukkit.getPluginManager().registerEvents(new SettingGUIListener(), this);

		SoundAbsorbListener.register(this);
		Bukkit.getLogger().info(getName() + " enabled.");
	}

	@Override
	public void onDisable() {
		PlayerSettingsManager.saveAllData();
		SoundAbsorbListener.unregister();
		Bukkit.getLogger().info(getName() + " disabled.");
	}
}
