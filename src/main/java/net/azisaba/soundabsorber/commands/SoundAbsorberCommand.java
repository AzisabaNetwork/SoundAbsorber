package net.azisaba.soundabsorber.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.azisaba.soundabsorber.utils.Chat;

public class SoundAbsorberCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(Chat.f("&7設定は &c/settings &7に移行されました！"));
		return true;
	}
}
