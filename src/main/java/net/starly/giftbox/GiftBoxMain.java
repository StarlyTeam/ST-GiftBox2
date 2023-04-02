package net.starly.giftbox;

import net.starly.core.bstats.Metrics;
import net.starly.core.data.Config;
import net.starly.giftbox.command.tabcomplete.GiftBoxTab;
import net.starly.giftbox.command.GiftBoxCmd;
import net.starly.giftbox.listener.InventoryClickListener;
import net.starly.giftbox.listener.InventoryCloseListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class GiftBoxMain extends JavaPlugin {
    private static JavaPlugin plugin;
    public static Config config;

    @Override
    public void onEnable() {
        // DEPENDENCY
        if (!isPluginEnabled("net.starly.core.StarlyCore")) {
            getServer().getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : §fhttp://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }


        plugin = this;
        new Metrics(plugin, 17440);


        // CONFIG
        config = new Config("config", plugin);
        config.loadDefaultConfig();
        config.setPrefix("messages.prefix");


        // COMMANDS
        getServer().getPluginCommand("giftbox").setExecutor(new GiftBoxCmd());
        getServer().getPluginCommand("giftbox").setTabCompleter(new GiftBoxTab());


        // EVENTS
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), plugin);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), plugin);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    private boolean isPluginEnabled(String path) {
        try {
            Class.forName(path);
            return true;
        } catch (NoClassDefFoundError ignored) {
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }
}
