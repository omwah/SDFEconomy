/*
 */
package com.github.omwah.SDFEconomy;

import com.google.common.base.Joiner;
import org.bukkit.Location;
import org.bukkit.Server;

import com.onarandombox.multiverseinventories.MultiverseInventories;
import com.onarandombox.multiverseinventories.api.GroupManager;
import com.onarandombox.multiverseinventories.api.profile.WorldGroupProfile;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.plugin.Plugin;

/**
 * Returns the location of a player based on MultiverseInventories world
 * groupings
 */
public class MultiverseInvLocationTranslator extends ServerLocationTranslator {
    
    public MultiverseInvLocationTranslator(Server server) {
        super(server);
    }
    
    private MultiverseInventories getMultiverseInventories() {
        Plugin plugin = this.server.getPluginManager().getPlugin("Multiverse-Inventories");

        if (plugin == null || !(plugin instanceof MultiverseInventories)) {
            return null;
        }
        
        return (MultiverseInventories) plugin;
    }
    
    public String getLocationName(Location location) {
        String locationName = null;
        
        // Try and retrieve a name based on MultiverseInvetories groupings of worlds
        MultiverseInventories multiInv = getMultiverseInventories();
        if (multiInv != null) {
            GroupManager groupManager = multiInv.getGroupManager();
            if (groupManager != null) {
                ArrayList<String> worldGroupNames = new ArrayList<String>();
                List<WorldGroupProfile> worldGroupProfiles = groupManager.getGroupsForWorld(location.getWorld().getName());
                if (worldGroupProfiles != null) {
                    for (WorldGroupProfile i : worldGroupProfiles) {
                        worldGroupNames.add(i.getName());
                    }
                }
                // Create a location name that is join of all world group names
                Joiner joiner = Joiner.on("-");
                locationName = joiner.join(worldGroupNames);
            }
        }
        
        // If all else fails fall back to direct world name
        if (locationName == null) {
            locationName = location.getWorld().getName();
        }
        return locationName;
    }
}
