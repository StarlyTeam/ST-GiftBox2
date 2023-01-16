package net.starly.giftbox.command;

import net.starly.giftbox.data.PlayerGiftBoxData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static net.starly.giftbox.GiftBoxMain.config;

public class GiftBoxCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(config.getMessage("messages.only_player"));
            return true;
        }

        if (args.length == 0) {
            if (!p.hasPermission("starly.giftbox.open.self")) {
                p.sendMessage(config.getMessage("messages.no_permission"));
                return true;
            }

            PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(p);
            playerGiftBoxData.openInventory(p);

            return true;
        }

        switch (args[0].toLowerCase()) {
            case "보기", "open" -> {
                if (!p.hasPermission("starly.giftbox.open.other")) {
                    p.sendMessage(config.getMessage("messages.no_permission"));
                    return true;
                }

                if (args.length != 2) {
                    p.sendMessage(config.getMessage("messages.wrong_command"));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (target == null) {
                    p.sendMessage(config.getMessage("messages.player_not_found"));
                    return true;
                }

                PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(target);
                playerGiftBoxData.openInventory(p);

                return true;
            }

            case "보내기", "send" -> {
                if (!p.hasPermission("starly.giftbox.send")) {
                    p.sendMessage(config.getMessage("messages.no_permission"));
                    return true;
                }

                if (args.length != 2) {
                    p.sendMessage(config.getMessage("messages.wrong_command"));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (target == null) {
                    p.sendMessage(config.getMessage("messages.player_not_found"));
                    return true;
                } else if (target.getUniqueId() == p.getUniqueId()) {
                    p.sendMessage(config.getMessage("messages.cannot_send_self"));
                    return true;
                }

                ItemStack item = p.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR) {
                    p.sendMessage(config.getMessage("messages.no_item_in_hand"));
                    return true;
                }

                PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(target);
                if (playerGiftBoxData.getItems().size() == 45) {
                    p.sendMessage(config.getMessage("messages.full"));
                    return true;
                }

                playerGiftBoxData.addItem(item);
                p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

                p.sendMessage(config.getMessage("messages.sent"));
                if (target.isOnline()) {
                    target.getPlayer().sendMessage(config.getMessage("messages.received"));
                }
                return true;
            }

            case "리로드", "reload" -> {
                if (!p.hasPermission("starly.giftbox.reload")) {
                    p.sendMessage(config.getMessage("messages.no_permission"));
                    return true;
                }

                config.reloadConfig();

                p.sendMessage(config.getMessage("messages.reloaded"));
                return true;
            }

            case "도움말", "help", "?" -> {
                config.getMessages("messages.help").forEach(p::sendMessage);
                return true;
            }

            default -> {
                p.sendMessage(config.getMessage("messages.wrong_command"));
                return true;
            }
        }
    }
}
