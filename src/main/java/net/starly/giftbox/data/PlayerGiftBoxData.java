package net.starly.giftbox.data;

import net.starly.core.data.Config;
import net.starly.giftbox.GiftBoxMain;
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

        this.config = new Config("data/" + owner.getUniqueId(), GiftBoxMain.getPlugin());
        config.loadDefaultConfig();

        if (config.getConfigurationSection("items") == null) config.createSection("items");
        config.getConfigurationSection("items").getKeys(false).forEach(key -> {
            items.add(Integer.parseInt(key), config.getItemStack("items." + key));
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
        config.getConfig().set("items", null);
        config.saveConfig();
        config.createSection("items");
        items.forEach(item -> config.setItemStack("items." + items.indexOf(item), item));
    }

    public void openInventory(Player p) {
        inventoryOpenMap.put(p, owner);
        p.openInventory(getInventory());
    }

    private Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 54, "§6" + owner.getName() + "§f님의 선물함");
        ItemStack emptySlot = config.getItemStack("items.empty");
        ItemStack receiptAll = config.getItemStack("items.receipt_all");

        items.forEach(item -> inventory.setItem(items.indexOf(item), item));
        Arrays.asList(45, 46, 47, 48, 50, 51, 52, 53).forEach(slot -> inventory.setItem(slot, emptySlot));
        inventory.setItem(49, receiptAll);

        return inventory;
    }
}
