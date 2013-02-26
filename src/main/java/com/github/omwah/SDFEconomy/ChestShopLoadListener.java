package com.github.omwah.SDFEconomy;

import com.github.omwah.SDFEconomy.location.ServerLocationTranslator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

/*
 * Waits for ChestShop to get loaded and then registers the ChestShopEventListener
 */
public class ChestShopLoadListener implements Listener {
    private final SDFEconomy plugin;
    private final ServerLocationTranslator translator;

    public ChestShopLoadListener(SDFEconomy plugin, ServerLocationTranslator translator) {
        // Register the listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        this.plugin = plugin;
        this.translator = translator;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent event) {
        if (event.getPlugin().getDescription().getName().equals("ChestShop")) {
            plugin.getLogger().info("Enabling ChestShop support");
            new ChestShopEventListener(plugin, translator);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        if (event.getPlugin().getDescription().getName().equals("ChestShop")) {
            // Do nothing
        }
    }
}