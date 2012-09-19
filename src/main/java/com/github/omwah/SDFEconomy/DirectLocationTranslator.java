/*
 */
package com.github.omwah.SDFEconomy;

import org.bukkit.entity.Player;

/**
 *
 */
public class DirectLocationTranslator implements LocationTranslator { 
    public DirectLocationTranslator() {
    }


    public String getLocationName(Player player) {
        return player.getLocation().toString();
    }
}
