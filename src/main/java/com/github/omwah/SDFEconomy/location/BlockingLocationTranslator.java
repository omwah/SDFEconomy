package com.github.omwah.SDFEconomy.location;

import org.bukkit.Location;
import org.bukkit.Server;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.InterruptedException;

/**
 * Adds in the functionality for an external event to add locations for 
 * players based on additional information.
 *
 * The class queues accesses and blocks access to retrieving the 
 * a location for the same named player.
 */
public abstract class BlockingLocationTranslator extends ServerLocationTranslator {
    
    private final ConcurrentHashMap<String, Semaphore> destinationLock;
    private final ConcurrentHashMap<String, Semaphore> locationLock;
    private final ConcurrentHashMap<String, Location> destinations;
    
    public BlockingLocationTranslator(Server server) {
        super(server);

        this.destinations = new ConcurrentHashMap<String, Location>();
        this.locationLock = new ConcurrentHashMap<String, Semaphore>();
        this.destinationLock = new ConcurrentHashMap<String, Semaphore>();
    }
    
    /*
     * Adds a destination to use for a named player for subsequent
     * transactions 
     */
    public void addDestination(String playerName, Location destLocation) {
        destinationLock.putIfAbsent(playerName, new Semaphore(1, true));

        try {
            destinationLock.get(playerName).acquire();

            destinations.put(playerName, destLocation);
        } catch (InterruptedException ex) {
            // Log error and remove locks
            server.getLogger().info("Wait on destination lock was interrupted");
            if(destinationLock.containsKey(playerName)) {
                Semaphore lock = destinationLock.remove(playerName);
                lock.release();
            }
        }
    }
    
    /*
     * Removes a destination and associate locks
     */    
    public void removeDestination(String playerName, Location destLocation) {
        destinations.remove(playerName);

        locationLock.get(playerName).release();
        destinationLock.get(playerName).release();
    }
    
    public String getLocationName(String playerName) {
        if(destinationLock.containsKey(playerName)) {
            // Only acquire second lock if first was present 
            locationLock.putIfAbsent(playerName, new Semaphore(1, true));
            try {
                locationLock.get(playerName).acquire();

                return getLocationName(destinations.get(playerName));
            } catch (InterruptedException ex) {
                // Log error and remove locks
                server.getLogger().info("Wait on location lock was interrupted");
                if(locationLock.containsKey(playerName)) {
                    Semaphore lock = locationLock.remove(playerName);
                    lock.release();
                }
                return null;
            }
        } else {
            return super.getLocationName(playerName);
        }
    }
    
    public abstract String getLocationName(Location location);
}
