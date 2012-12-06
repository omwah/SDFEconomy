package com.github.omwah.SDFEconomy;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.permission.Permission;

import com.github.omwah.SDFEconomy.commands.CommandHandler;

/*
 * Bukkit Plugin class for SDFEconomy
 */
public class SDFEconomy extends JavaPlugin {
    private SDFEconomyAPI api;
    private EconomyStorage storage;
    private CommandHandler commandHandler;
    private Permission permission;

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
        
        // Set up permissions from Vault
        setupPermissions();
        
        // Create the Listener to register players into economy on joining
        new SDFEconomyListener(this);
        
        // set the command executor for economy
        this.commandHandler = new CommandHandler(this);
        this.getCommand("sdfeconomy").setExecutor(new SDFEconomyCommandExecutor(this, this.commandHandler));
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
    
    /*
     * Return the command handler used for, well handling commands
     */
    public CommandHandler getCommandHandler() {
        return this.commandHandler;
    }

    /*
     * Get permissions provider
     */
    public Permission getPermission() {
        return this.permission;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
