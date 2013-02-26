/*
 */
package com.github.omwah.SDFEconomy.location;

import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

/**
 * Abstract base class for translators that use the Bukkit server to
 * translate world names
 */
public abstract class ServerLocationTranslator implements LocationTranslator {
    protected Server server;
    
    // Helps address transaction between players
    // when one might not be in the same location
    private final HashMap<String, Location> destination_helper;
    
    public ServerLocationTranslator(Server server) {
        this.server = server;
        this.destination_helper = new HashMap<String, Location>();
    }
    
    /*
     * Adds a temporary destination to use instead for a player for all
     * subsequent transactions
     */
    public void addDestination(String playerName, Location destLocation) {
        destination_helper.put(playerName, destLocation);
    }
    
    /*
     * Removes a temporary destination
     */    
    public void removeDestination(String playerName, Location destLocation) {
        destination_helper.remove(playerName);
    }
    
    public String getLocationName(String playerName) {
        // Use specific destination if set
        if(destination_helper.containsKey(playerName)) {
            return getLocationName(destination_helper.get(playerName));
        }
        
        // If no specific destination try and determine player's location
        // whether online or off
        OfflinePlayer offlinePlayer = server.getOfflinePlayer(playerName);
        Player onlinePlayer = offlinePlayer.getPlayer();
        
        if (onlinePlayer != null) {
            return getLocationName(onlinePlayer.getLocation());
        } else if (offlinePlayer.hasPlayedBefore()) {
            // Make sure the offline player has played before or the call to
            // getBedSpawnLocation will cause a null pointer inside of bukkit
            Location bedLocation = offlinePlayer.getBedSpawnLocation();
            if (bedLocation != null) {
                return getLocationName(bedLocation);
            }
        }
        return null;
    }
    
    public String getLocationName(Player player) {
        return getLocationName(player.getName());
    }
        
    public abstract String getLocationName(Location location);
}
