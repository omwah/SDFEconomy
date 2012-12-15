/*
 */
package com.github.omwah.SDFEconomy;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

/**
 * Abstract base class for translators that use the Bukkit server to
 * translate world names
 */
public abstract class ServerLocationTranslator implements LocationTranslator {
    protected Server server;
    
    public ServerLocationTranslator(Server server) {
        this.server = server;
    }
    
    public String getLocationName(String playerName) {
        OfflinePlayer offlinePlayer = server.getOfflinePlayer(playerName);
        Player onlinePlayer = offlinePlayer.getPlayer();
        
        if (onlinePlayer != null) {
            return getLocationName(onlinePlayer);
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
        return getLocationName(player.getLocation());
    }
        
    public abstract String getLocationName(Location location);
}
