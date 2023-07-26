package net.starly.giftbox.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import static net.starly.giftbox.data.InventoryOpenMap.inventoryOpenMap;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        inventoryOpenMap.remove(event.getPlayer().getUniqueId());
    }
}
