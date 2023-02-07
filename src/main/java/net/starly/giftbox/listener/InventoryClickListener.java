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
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (!inventoryOpenMap.containsKey(p)) return;

        if (e.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD || e.getAction() == InventoryAction.HOTBAR_SWAP) {
            e.setCancelled(true);
            return;
        }

        if (e.getClickedInventory() == p.getInventory()) {
            e.setCancelled(true);
            return;
        }

        ItemStack item = e.getCurrentItem();
        if (item == null) return;

        int slotNum = e.getSlot();
        if (slotNum == 49) {
            // 모두수령 클릭

            PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(inventoryOpenMap.get(p));

            e.setCancelled(true);

            p.closeInventory();

            if (playerGiftBoxData.getItems().size() == 0) {
                p.sendMessage(config.getMessage("messages.empty"));
                return;
            } else if (InventoryUtil.getSpace(p.getInventory()) - 5 < playerGiftBoxData.getItems().size()) {
                p.sendMessage(config.getMessage("messages.cannot_receive"));
                return;
            }

            Inventory inv = p.getInventory();
            playerGiftBoxData.getItems().forEach(inv::addItem);
            playerGiftBoxData.clearItems();

            p.sendMessage(config.getMessage("messages.all_received"));
        } else if (Arrays.asList(45, 46, 47, 48, 50, 51, 52, 53).contains(slotNum)) {
            // 빈칸 아이템 클릭

            e.setCancelled(true);
        } else {
            // 일반 아이템 클릭

            if (e.getClickedInventory() == p.getInventory()) return;
            if (InventoryUtil.getSpace(p.getInventory()) - 5 < 1) {
                p.sendMessage(config.getMessage("messages.cannot_receive"));
                return;
            }

            PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(inventoryOpenMap.get(p));
            System.out.println(inventoryOpenMap.get(p));

            p.getInventory().addItem(item);
            playerGiftBoxData.removeItem(item);

            p.closeInventory();
            p.sendMessage(config.getMessage("messages.receipted"));
        }
    }
}
