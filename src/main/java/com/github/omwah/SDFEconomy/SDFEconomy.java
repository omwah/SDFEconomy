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
        this.getConfig().addDefault("storage.yaml.filename", "accounts.yaml");
        
        this.storage = new EconomyYamlStorage(this.getConfig().getString("storage.yaml.filename"));
        DirectLocationTranslator locationTrans = new DirectLocationTranslator();
        this.api = new EconomyAPI(this.getServer(), this.getConfig(), this.storage, locationTrans);

        // save the configuration file
        saveDefaultConfig();
       
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
