package com.github.omwah.SDFEconomy;

import org.bukkit.plugin.java.JavaPlugin;

/*
 * This is the main class of the sample plug-in
 */
public class SDFEconomy extends JavaPlugin {
    EconomyAPI api;
    EconomyStorage storage;
    
    /*
     * This is called when your plug-in is enabled
     */
    @Override
    public void onEnable() {
        // save the configuration file
        saveDefaultConfig();
        
        this.storage = new EconomyYamlStorage();
        this.api = new EconomyAPI(this.getServer(), this.storage, this.getConfig());
        
        // Create the Listener to register players into economy on joining
        new SDFEconomyListener(this);
        
        // set the command executor for economy
        this.getCommand("economy").setExecutor(new SDFEconomyCommandExecutor(this));
    }
    
    /*
     * This is called when your plug-in shuts down
     */
    @Override
    public void onDisable() {
        storage.commit();
    }

    /*
     * 
     */
    EconomyAPI getAPI() {
        return this.api;
    }
}
