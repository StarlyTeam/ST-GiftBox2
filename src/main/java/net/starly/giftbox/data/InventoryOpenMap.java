package net.starly.giftbox.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InventoryOpenMap {
    /**
     * key   : Inventory Opened Player.
     * <br>
     * value : GiftBox's Owner.
     */
    public static Map<Player, OfflinePlayer> inventoryOpenMap = new HashMap<>();
}
