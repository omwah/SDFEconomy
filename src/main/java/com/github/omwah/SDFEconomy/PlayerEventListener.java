package com.github.omwah.SDFEconomy;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;


/*
 * Watches for any server events that affect the economy
 */
public class PlayerEventListener implements Listener {
    private final SDFEconomy plugin;
    private final SDFEconomyAPI api;

    /*
     * This listener needs to know about the plugin which it came from
     */
    public PlayerEventListener(SDFEconomy plugin) {
        // Register the listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        this.plugin = plugin;
        this.api = plugin.getAPI();
    }

    /*
     * Create a new account for a player if it does not have
     * one in its current location
     */
    private void createPlayerAccount(Player player) {
        if(!api.hasAccount(player.getName())) {
            api.createPlayerAccount(player.getName());
        }
    }
   
    /*
     * Create a new account at a specific location
     */
    private void createPlayerAccount(Player player, Location location) {
        String locationName = api.getLocationTranslated(location);
        if(!api.hasAccount(player.getName(), locationName)) {
            api.createPlayerAccount(player.getName(), locationName);
        }
    }
    
    /*
     * Add new account on join if necessary
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        createPlayerAccount(event.getPlayer());
    }
    
    /*
     * Add new account on teleporting if necessary
     */
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        // Player from event will be the "from" location
        // not the "to" location
        createPlayerAccount(event.getPlayer(), event.getTo());
    }
    
}
