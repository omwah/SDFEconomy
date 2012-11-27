package com.github.omwah.SDFEconomy;

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
        
        this.storage = new EconomyYamlStorage(this.getDataFolder() + "/" + this.getConfig().getString("storage.yaml.filename"), 
                                              this.getConfig().getBoolean("storage.yaml.save_on_update"));
                                                 
        DirectLocationTranslator locationTrans = new DirectLocationTranslator();
        this.api = new SDFEconomyAPI(this.getServer(), this.getConfig(), this.storage, locationTrans);

        // save the configuration file
        saveDefaultConfig();
        this.storage.commit();
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
    SDFEconomyAPI getAPI() {
        return this.api;
    }
}
