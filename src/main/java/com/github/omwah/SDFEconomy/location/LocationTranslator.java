package com.github.omwah.SDFEconomy.location;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
 * Given a Player returns a string representation of their location.
 * This could be which multiverse or world they are currently located in
 * or some other symbolic representation of where they are located.
 */
public interface LocationTranslator {
    /*
     * Get location name based on player name
     */
    public String getLocationName(String playerName);

    /*
     * Get location name based on a Player object
     */
    public String getLocationName(Player player);

    /*
     * Get location name based on a Location object
     */
    public String getLocationName(Location location);

    /*
     * Check if a location name is valid
     */
    public boolean validLocationName(String locationName);
}
