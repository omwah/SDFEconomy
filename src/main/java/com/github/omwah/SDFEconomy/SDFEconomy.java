package com.github.omwah.SDFEconomy;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;


/*
 * Bukkit Plugin class for SDFEconomy
 */
public class SDFEconomy extends JavaPlugin {
    SDFEconomyAPI api;
    EconomyStorage storage;
    
    /*
     * Called when the plugin is enabled
     */
    @Override
    public void onEnable() {
        this.getConfig().addDefault("storage.yaml.filename", "accounts.yaml");
        this.getConfig().addDefault("storage.yaml.save_on_update", true);

        File storageFile = new File(this.getDataFolder(), this.getConfig().getString("storage.yaml.filename"));
        boolean save_on_update = this.getConfig().getBoolean("storage.yaml.save_on_update");

        this.storage = new EconomyYamlStorage(storageFile, save_on_update);
                                              
                                                 
        DirectLocationTranslator locationTrans = new DirectLocationTranslator();
        this.api = new SDFEconomyAPI(this.getServer(), this.getConfig(), this.storage, locationTrans);

        // save the configuration file
        saveDefaultConfig();
        
        // Create the Listener to register players into economy on joining
        new SDFEconomyListener(this);
        
        // set the command executor for economy
        this.getCommand("economy").setExecutor(new SDFEconomyCommandExecutor(this));
    }
    
    /*
     * Called when the plug-in shuts down
     */
    @Override
    public void onDisable() {
        storage.commit();
    }

    /*
     * Returns the Vault interface class for the Economy
     */
    public SDFEconomyAPI getAPI() {
        return this.api;
    }
}
