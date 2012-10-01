/*
 * 
 */
package com.github.omwah.SDFEconomy;

import org.bukkit.entity.Player;

/*
 * Given a Player returns a string representation of their location.
 * This could be which multiverse or world they are currently located in
 * or some other symbolic representation of where they are located.
 */
public interface LocationTranslator {
    public String getLocationName(Player player);
}
