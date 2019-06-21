package net.azisaba.soundabsorber.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.azisaba.soundabsorber.SettingsGUIManager;
import net.azisaba.soundabsorber.SettingsGUIManager.ButtonType;

public class SettingGUIListener implements Listener {

	@EventHandler
	public void onClickInventory(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		Inventory clicked = e.getClickedInventory();

		if (clicked == null) {
			return;
		}

		if (!SettingsGUIManager.isSettingInventory(clicked)) {
			return;
		}

		ItemStack clickedItem = e.getCurrentItem();
		if (clickedItem == null) {
			return;
		}

		e.setCancelled(true);
		ButtonType type = SettingsGUIManager.getItemType(clickedItem);

		if (type == null) {
			return;
		}

		switch (type) {
		case DECREASE1:
			SettingsGUIManager.addValue(p, clicked, -1);
			break;
		case DECREASE5:
			SettingsGUIManager.addValue(p, clicked, -5);
			break;
		case INCREASE1:
			SettingsGUIManager.addValue(p, clicked, 1);
			break;
		case INCREASE5:
			SettingsGUIManager.addValue(p, clicked, 5);
			break;
		}
	}
}
