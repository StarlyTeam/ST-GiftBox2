package net.starly.giftbox.data;

import net.starly.core.data.Config;
import net.starly.giftbox.GiftBoxMain;
import net.starly.giftbox.util.EncodeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.starly.giftbox.data.InventoryOpenMap.inventoryOpenMap;

public class PlayerGiftBoxData {
    private final OfflinePlayer owner;
    private final Config config;
    private final List<ItemStack> items = new ArrayList<>();

    public PlayerGiftBoxData(OfflinePlayer owner) {
        this.owner = owner;

        this.config = new Config("data/" + owner.getUniqueId(), GiftBoxMain.getInstance());
        config.loadDefaultConfig();

        if (config.getConfigurationSection("items") == null) config.createSection("items");
        config.getConfigurationSection("items").getKeys(false).forEach(key -> {
            items.add(Integer.parseInt(key), EncodeUtil.decode((byte[]) config.getObject("items." + key), ItemStack.class));
        });
    }

    public void addItem(ItemStack item) {
        items.add(item);
        saveItems();
    }

    public void removeItem(ItemStack itemStack) {
        items.remove(itemStack);
        saveItems();
    }

    public void clearItems() {
        items.clear();
        saveItems();
    }

    public List<ItemStack> getItems() {
        return items;
    }

    private void saveItems() {
        config.getConfig().createSection("items");
        for (int i = 0; i < items.size(); i++) {
            ItemStack item = items.get(i);
            config.setObject("items." + i, EncodeUtil.encode(item));
        }

        config.saveConfig();
    }

    public void openInventory(Player p) {
        inventoryOpenMap.put(p, owner);
        p.openInventory(getInventory());
    }

    private Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "§6" + owner.getName() + "§f님의 선물함");
        ItemStack emptySlot = GiftBoxMain.config.getItemStack("items.empty");
        ItemStack receiptAll = GiftBoxMain.config.getItemStack("items.receipt_all");

        for (int i = 0; i < items.size(); i++) inventory.setItem(i, items.get(i));
        Arrays.asList(45, 46, 47, 48, 50, 51, 52, 53).forEach(slot -> inventory.setItem(slot, emptySlot));
        inventory.setItem(49, receiptAll);

        return inventory;
    }
}
