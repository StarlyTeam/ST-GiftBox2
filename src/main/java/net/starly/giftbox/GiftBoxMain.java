package net.starly.giftbox;

import net.starly.core.bstats.Metrics;
import net.starly.core.data.Config;
import net.starly.giftbox.command.tabcomplete.GiftBoxTab;
import net.starly.giftbox.command.GiftBoxCmd;
import net.starly.giftbox.listener.InventoryClickListener;
import net.starly.giftbox.listener.InventoryCloseListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class GiftBoxMain extends JavaPlugin {
    private static JavaPlugin instance;
    public static Config config;

    @Override
    public void onEnable() {
        /* DEPENDENCY
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        if (!isPluginEnabled("ST-Core")) {
            getServer().getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttp://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* SETUP
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        instance = this;
        new Metrics(instance, 17440);

        /* CONFIG
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        config = new Config("config", instance);
        config.loadDefaultConfig();
        config.setPrefix("messages.prefix");

        /* COMMAND
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getServer().getPluginCommand("giftbox").setExecutor(new GiftBoxCmd());
        getServer().getPluginCommand("giftbox").setTabCompleter(new GiftBoxTab());

        /* LISTENER
         ──────────────────────────────────────────────────────────────────────────────────────────────────────────────── */
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), instance);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), instance);
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    private boolean isPluginEnabled(String name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }
}
