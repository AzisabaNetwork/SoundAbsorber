package net.azisaba.soundabsorber.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import net.azisaba.soundabsorber.PlayerSettingsManager;
import net.azisaba.soundabsorber.SettingsGUIManager;
import net.md_5.bungee.api.ChatColor;

public class SoundAbsorberCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "このコマンドはプレイヤーからのみ実行可能です。");
			return true;
		}
		Player p = (Player) sender;

		if (args.length <= 0) {
			Inventory inv = SettingsGUIManager.getInventory(p);
			p.openInventory(inv);
			return true;
		}

		String levelStr = args[0];
		int level = 0;
		try {
			level = Integer.parseInt(levelStr);
		} catch (Exception e) {
			p.sendMessage(ChatColor.RED + "正の数字を指定してください！");
			return true;
		}

		if (level <= 0 || level > 50) {
			p.sendMessage(ChatColor.RED + "値は 1から50 の間で指定してください。");
			return true;
		}

		PlayerSettingsManager.setAbsorbLevel(p, level);
		levelStr = level + "";
		if (level == 50) {
			levelStr = "無音(50)";
		}
		if (level == 1) {
			levelStr = "通常(1)";
		}

		p.sendMessage(ChatColor.GREEN + "吸収レベルを" + ChatColor.YELLOW + levelStr + ChatColor.GREEN + "に設定しました。");
		return true;
	}
}
