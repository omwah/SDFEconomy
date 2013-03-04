package com.github.omwah.SDFEconomy.location;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Always returns the same string for all location queries
 */
public class GlobalLocationTranslator implements LocationTranslator {
    public final String globalName;
    
    public GlobalLocationTranslator(String globalName) {
        this.globalName = globalName;
    }

    public String getLocationName(String playerName) {
        return globalName;
    }

    public String getLocationName(Player player) {
        return globalName;
    }

    public String getLocationName(Location location) {
        return globalName;
    }

    public boolean validLocationName(String locationName) {
        if(locationName.equalsIgnoreCase(globalName)) {
            return true;
        } else {
            return false;
        }
    }
    
}
