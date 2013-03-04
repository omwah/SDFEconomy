package com.github.omwah.SDFEconomy;

import com.github.omwah.SDFEconomy.listener.ChestShopLoadListener;
import com.github.omwah.SDFEconomy.listener.PlayerEventListener;
import com.github.omwah.SDFEconomy.location.MultiverseInvLocationTranslator;
import java.io.File;
import java.util.Iterator;
import java.util.Observer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.command.PluginCommand;

import net.milkbowl.vault.permission.Permission;

import com.github.omwah.SDFEconomy.commands.SDFEconomyCommandExecutor;
import com.github.omwah.SDFEconomy.location.GlobalLocationTranslator;
import com.github.omwah.SDFEconomy.location.LocationTranslator;
import com.github.omwah.SDFEconomy.location.PerWorldLocationTranslator;
import com.github.omwah.SDFEconomy.location.SetDestinationLocationTranslator;

/*
 * Bukkit Plugin class for SDFEconomy
 */
public class SDFEconomy extends JavaPlugin {
    private SDFEconomyAPI api;
    private EconomyStorage storage;
    private Permission permission;

    /*
     * Called when the plugin is enabled
     */
    @Override
    public void onEnable() {
        // So far only one storage method is implemented: YAML Storage
        this.getConfig().addDefault("storage.yaml.filename", "accounts.yaml");
        this.getConfig().addDefault("storage.yaml.commit_delay", 60L);
        this.getConfig().addDefault("location.translator", "multiverse");

        File storageFile = new File(this.getDataFolder(), this.getConfig().getString("storage.yaml.filename"));

        YamlStorage yaml_storage = new YamlStorage(storageFile);
        this.storage = yaml_storage;

        // Commit delay is the number of ticks to wait till commiting updates
        // A commit delay of 0 means the file is saved on every update
        // If the delay is negative then no commits are done till the plugin
        // is disabled
        long commit_delay = this.getConfig().getLong("storage.yaml.commit_delay");
        if(commit_delay == 0) {
            yaml_storage.addObserver((Observer) new StorageCommitEveryUpdate());
        } else if(commit_delay > 0) {
            yaml_storage.addObserver((Observer) new StorageCommitDelayed(this, commit_delay)); 
        }
                                                 
        // Load the location translator specified in the config file
        String translator_str = this.getConfig().getString("location.translator");
        LocationTranslator locationTrans = null;
        if(translator_str.equalsIgnoreCase("multiverse")) {
            getLogger().info("Using Multiverse-Inventories location translator");
            locationTrans = new MultiverseInvLocationTranslator(this);
        } else if (translator_str.equalsIgnoreCase("per_world")) {
            getLogger().info("Using Per World location translator");
            locationTrans = new PerWorldLocationTranslator(this.getServer());
        } else if (translator_str.equalsIgnoreCase("global")) {
            String global_name = this.getConfig().getString("location.global.name", "global");
            getLogger().info("Using Global location translator with location name: " + global_name);
            locationTrans = new GlobalLocationTranslator(global_name);
        } else {
            getLogger().info("Invalid value for config value location.translator, defaulting to multiverse translator");
            locationTrans = new MultiverseInvLocationTranslator(this);
        }

        // Create the API used both by Vault and the Plugin commands
        this.api = new SDFEconomyAPI(this.getConfig(), this.storage, locationTrans);

        // save the configuration file
        saveDefaultConfig();
        
        // Set up permissions from Vault
        setupPermissions();
        
        // Create the Listener to register players into economy on joining
        // Or teleporting to a world for the first time
        new PlayerEventListener(this);
        
        // Waits for ChestShop to be loaded and if so it optionally loads a listener
        // to use events from that plugin
        if(locationTrans instanceof SetDestinationLocationTranslator) {
            new ChestShopLoadListener(this, (SetDestinationLocationTranslator) locationTrans);
        }
        
        // Load up the list of commands in the plugin.yml and register each of these
        // This makes is simpler to update the command names that this Plugin responds
        // to just by editing plugin.yml
        for(Iterator cmd_iter = this.getDescription().getCommands().keySet().iterator(); cmd_iter.hasNext();) {
            // set the command executor for the Command
            PluginCommand curr_cmd = this.getCommand((String) cmd_iter.next());
            curr_cmd.setExecutor(new SDFEconomyCommandExecutor(curr_cmd, this.permission, this.api, this.getConfig(), this.getServer()));
        }
    }
    
    /*
     * Called when the plug-in shuts down
     * One final commit
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
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
