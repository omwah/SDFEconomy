/*
 */
package com.github.omwah.SDFEconomy;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Used for testing purposes, return predictable strings
 */
public class TestLocationTranslator implements LocationTranslator {

    public String getLocationName(String playerName) {
        if (playerName == "NullPlayer") {
            return null;
        } else {
            int name_len = playerName.length();
            return "World" + playerName.substring(name_len-1,name_len);
        }
    }

    public String getLocationName(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getLocationName(Location location) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
