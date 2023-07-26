package net.starly.giftbox.listener;

import net.starly.core.util.InventoryUtil;
import net.starly.giftbox.data.PlayerGiftBoxData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

import static net.starly.giftbox.GiftBoxMain.config;
import static net.starly.giftbox.data.InventoryOpenMap.inventoryOpenMap;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!inventoryOpenMap.containsKey(player.getUniqueId())) return;

        Inventory inventory = event.getClickedInventory();
        if (inventory == player.getInventory()) {
            event.setCancelled(true);
            return;
        }

        ItemStack currentItem = event.getCurrentItem();
        if (currentItem == null) return;
        event.setCancelled(true);

        int slot = event.getSlot();
        if (slot == 49) {
            event.setCancelled(true);

            PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(inventoryOpenMap.get(player.getUniqueId()));
            player.closeInventory();

            if (playerGiftBoxData.getItems().size() == 0) {
                player.sendMessage(config.getMessage("messages.empty"));
                return;
            } else if (InventoryUtil.getSpace(player.getInventory()) - 5 < playerGiftBoxData.getItems().size()) {
                player.sendMessage(config.getMessage("messages.cannot_receive"));
                return;
            }

            Inventory inv = player.getInventory();
            playerGiftBoxData.getItems().forEach(inv::addItem);
            playerGiftBoxData.clearItems();

            player.sendMessage(config.getMessage("messages.all_received"));
        } else if (!(slot >= 45 && slot <= 53)) {
            if (event.getClickedInventory() == player.getInventory()) return;
            if (InventoryUtil.getSpace(player.getInventory()) - 5 < 1) {
                player.sendMessage(config.getMessage("messages.cannot_receive"));
                return;
            }

            PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(inventoryOpenMap.get(player.getUniqueId()));
            System.out.println(inventoryOpenMap.get(player.getUniqueId()));

            player.getInventory().addItem(currentItem);
            playerGiftBoxData.removeItem(currentItem);

            player.closeInventory();
            player.sendMessage(config.getMessage("messages.receipted"));
        }
    }
}
