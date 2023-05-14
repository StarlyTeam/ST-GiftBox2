package net.starly.giftbox.command;

import net.starly.core.data.Config;
import net.starly.giftbox.GiftBoxMain;
import net.starly.giftbox.data.PlayerGiftBoxData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.starly.giftbox.GiftBoxMain.config;

public class GiftBoxCmd implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(config.getMessage("messages.only_player"));
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            if (!player.hasPermission("starly.giftbox.open.self")) {
                player.sendMessage(config.getMessage("messages.no_permission"));
                return true;
            }

            PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(player);
            playerGiftBoxData.openInventory(player);

            return true;
        }

        switch (args[0].toLowerCase()) {
            case "도움말":
            case "help":
            case "?": {
                if (!player.hasPermission("starly.giftbox.help")) {
                    player.sendMessage(config.getMessage("messages.no_permission"));
                    return true;
                }

                config.getMessages("messages.help").forEach(player::sendMessage);
                return true;
            }

            case "열기":
            case "open": {
                if (!player.hasPermission("starly.giftbox.open.other")) {
                    player.sendMessage(config.getMessage("messages.no_permission"));
                    return true;
                }

                if (args.length != 2) {
                    player.sendMessage(config.getMessage("messages.wrong_command"));
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                if (target == null) {
                    player.sendMessage(config.getMessage("messages.player_not_found"));
                    return true;
                }

                PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(target);
                playerGiftBoxData.openInventory(player);

                return true;
            }

            case "보내기":
            case "send": {
                if (!player.hasPermission("starly.giftbox.send")) {
                    player.sendMessage(config.getMessage("messages.no_permission"));
                    return true;
                }

                if (args.length != 2) {
                    player.sendMessage(config.getMessage("messages.wrong_command"));
                    return true;
                }


                Plugin quickMenuPlugin = GiftBoxMain.getInstance().getServer().getPluginManager().getPlugin("ST-QuickMenu");
                if (quickMenuPlugin != null && quickMenuPlugin.isEnabled()) {
                    FileConfiguration quickMenuConfig = quickMenuPlugin.getConfig();

                    int selectedSlot = player.getInventory().getHeldItemSlot();
                    if (quickMenuConfig.getStringList("default.open-type").contains("ICON")
                            && quickMenuConfig.getInt("default.icon.slot") == selectedSlot) {
                        player.sendMessage(config.getString("messages.prefix"));
                    }
                }


                List<OfflinePlayer> targets = new ArrayList<>();
                if (args[1].equalsIgnoreCase("@a")) {
                    if (!player.hasPermission("starly.giftbox.send.all")) {
                        player.sendMessage(config.getMessage("messages.no_permission"));
                        return true;
                    }

                    targets.addAll(Bukkit.getOnlinePlayers());
                    targets.remove(player);
                } else {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);

                    if (!(target.hasPlayedBefore() || target.isOnline())) {
                        player.sendMessage(config.getMessage("messages.player_not_found"));
                        return true;
                    }

                    if (target.getPlayer() == player) {
                        player.sendMessage(config.getMessage("messages.cannot_send_self"));
                        return true;
                    }

                    targets.add(target);
                }

                ItemStack item = player.getInventory().getItemInMainHand();
                if (item.getType() == Material.AIR) {
                    player.sendMessage(config.getMessage("messages.no_item_in_hand"));
                    return true;
                }

                targets.forEach(currentTarget -> {
                    PlayerGiftBoxData playerGiftBoxData = new PlayerGiftBoxData(currentTarget);
                    if (playerGiftBoxData.getItems().size() == 45) {
                        player.sendMessage(config.getMessage("messages.full"));
                        return;
                    }

                    playerGiftBoxData.addItem(item);
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));

                    player.sendMessage(config.getMessage("messages.sent"));
                    if (currentTarget.isOnline()) {
                        currentTarget.getPlayer().sendMessage(config.getMessage("messages.received"));
                    }
                });
                return true;
            }

            case "리로드":
            case "reload": {
                if (!player.hasPermission("starly.giftbox.reload")) {
                    player.sendMessage(config.getMessage("messages.no_permission"));
                    return true;
                }

                config.reloadConfig();

                player.sendMessage(config.getMessage("messages.reloaded"));
                return true;
            }

            default: {
                player.sendMessage(config.getMessage("messages.wrong_command"));
                return true;
            }
        }
    }
}
