package net.starly.giftbox.command.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GiftBoxTab implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return Collections.emptyList();

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            if (sender.hasPermission("starly.giftbox.open.other")) completions.add("열기");
            if (sender.hasPermission("starly.giftbox.send")) completions.add("보내기");
            if (sender.hasPermission("starly.giftbox.reload")) completions.add("리로드");
            completions.add("도움말");

            return completions;
        } else if (args.length == 2) {
            if (Arrays.asList("보기", "open").contains(args[0].toLowerCase())) {
                if (sender.hasPermission("starly.giftbox.open.other")) return null;
            } else if (Arrays.asList("보내기", "send").contains(args[0].toLowerCase())) {
                if (sender.hasPermission("starly.giftbox.send")) return null;
            }
        }

        return Collections.emptyList();
    }
}
