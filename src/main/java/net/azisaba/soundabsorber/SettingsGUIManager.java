package net.azisaba.soundabsorber;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class SettingsGUIManager {

	private static final String title = ChatColor.YELLOW + "Sound Absorb Level Setting";
	private static ItemStack increase1, decrease1, increase5, decrease5;

	protected static void init() {
		decrease1 = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 1, ChatColor.RED + "レベルを 1 下げる");
		increase1 = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 5, ChatColor.GREEN + "レベルを 1 上げる");

		decrease5 = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 14, ChatColor.RED + "レベルを 5 下げる");
		increase5 = ItemHelper.createItem(Material.STAINED_GLASS_PANE, 13, ChatColor.GREEN + "レベルを 5 上げる");
	}

	public static Inventory getInventory(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9, title);
		inv.setItem(0, decrease5);
		inv.setItem(1, decrease5);
		inv.setItem(2, decrease1);
		inv.setItem(3, decrease1);

		ItemStack sign = new ItemStack(Material.SIGN);
		int level = PlayerSettingsManager.getAbsorbLevel(p);
		if (level == 1) {
			ItemHelper.setDisplayName(sign,
					ChatColor.YELLOW + "現在の吸収レベル: " + ChatColor.DARK_GREEN + "通常 " + ChatColor.GREEN + "(1)");
		} else if (level < 50) {
			ItemHelper.setDisplayName(sign,
					ChatColor.YELLOW + "現在の吸収レベル: " + ChatColor.GREEN + level);
		} else {
			ItemHelper.setDisplayName(sign,
					ChatColor.YELLOW + "現在の吸収レベル: " + ChatColor.RED + "無音 " + ChatColor.GREEN + "(50)");
		}
		inv.setItem(4, sign);

		inv.setItem(5, increase1);
		inv.setItem(6, increase1);
		inv.setItem(7, increase5);
		inv.setItem(8, increase5);
		return inv;
	}

	public static boolean isSettingInventory(Inventory inv) {
		return inv.getTitle().startsWith(title);
	}

	public static boolean addValue(Player p, Inventory inv, int num) {
		ItemStack sign = inv.getItem(4);
		if (sign.getType() != Material.SIGN) {
			return false;
		}

		String levelStr = sign.getItemMeta().getDisplayName()
				.substring(sign.getItemMeta().getDisplayName().lastIndexOf(ChatColor.GREEN + "") + 2);
		if (levelStr.startsWith("(")) {
			levelStr = levelStr.substring(1);
		}
		if (levelStr.endsWith(")")) {
			levelStr = levelStr.substring(0, levelStr.length() - 1);
		}

		int level = 1;
		try {
			level = Integer.parseInt(levelStr);
		} catch (Exception e) {
			Bukkit.getLogger().warning("Could not parse \"" + levelStr + "\"");
			return false;
		}

		int before = level;
		level += num;
		if (level < 1) {
			level = 1;
		} else if (level > 50) {
			level = 50;
		}

		ItemMeta meta = sign.getItemMeta();
		if (level == 1) {
			meta.setDisplayName(
					ChatColor.YELLOW + "現在の吸収レベル: " + ChatColor.DARK_GREEN + "通常 " + ChatColor.GREEN + "(1)");
		} else if (level < 50) {
			meta.setDisplayName(ChatColor.YELLOW + "現在の吸収レベル: " + ChatColor.GREEN + level);
		} else {
			meta.setDisplayName(ChatColor.YELLOW + "現在の吸収レベル: " + ChatColor.RED + "無音 " + ChatColor.GREEN + "(50)");
		}
		sign.setItemMeta(meta);
		inv.setItem(4, sign);

		if (before != level) {
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_HAT, 1, 1);
		} else {
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
		}

		PlayerSettingsManager.setAbsorbLevel(p, level);
		return true;
	}

	@SuppressWarnings("deprecation")
	public static ButtonType getItemType(ItemStack item) {
		if (item.getType() != Material.STAINED_GLASS_PANE) {
			return null;
		}
		byte data = item.getData().getData();
		if (data == (byte) 1) {
			return ButtonType.DECREASE1;
		} else if (data == (byte) 5) {
			return ButtonType.INCREASE1;
		} else if (data == (byte) 13) {
			return ButtonType.INCREASE5;
		} else if (data == (byte) 14) {
			return ButtonType.DECREASE5;
		} else {
			return null;
		}
	}

	public enum ButtonType {
		INCREASE1,
		INCREASE5,
		DECREASE1,
		DECREASE5;
	}
}
