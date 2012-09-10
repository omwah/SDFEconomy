package com.github.omwah.SDFEconomy;

import java.text.MessageFormat;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
 * Watches for any server events that affect the economy
 */
public class SDFEconomyListener implements Listener {
    private final SDFEconomy plugin;

    /*
     * This listener needs to know about the plugin which it came from
     */
    public SDFEconomyListener(SDFEconomy plugin) {
        // Register the listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        this.plugin = plugin;
    }

    /*
     * Add new players with a default balance
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //event.getPlayer();
    }
}
