/*
 */
package com.github.omwah.SDFEconomy;

import org.bukkit.Location;
import org.bukkit.Server;

/**
 * Returns the location of a player exactly.
 */
public class DirectLocationTranslator extends ServerLocationTranslator {
    
    public DirectLocationTranslator(Server server) {
        super(server);
    }
    
    public String getLocationName(Location location) {
        return location.getWorld().getName();
    }
}
