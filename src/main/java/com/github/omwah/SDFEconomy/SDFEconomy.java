package com.github.omwah.SDFEconomy;

import com.github.omwah.SDFEconomy.commands.SDFEconomyCommandExecutor;
import com.github.omwah.SDFEconomy.listener.ChestShopLoadListener;
import com.github.omwah.SDFEconomy.listener.PlayerEventListener;
import com.github.omwah.SDFEconomy.location.FactionsLocationSupport;
import com.github.omwah.SDFEconomy.location.GlobalLocationTranslator;
import com.github.omwah.SDFEconomy.location.LocationTranslator;
import com.github.omwah.SDFEconomy.location.MultiInvLocationTranslator;
import com.github.omwah.SDFEconomy.location.MultiverseInvLocationTranslator;
import com.github.omwah.SDFEconomy.location.MyWorldsLocationTranslator;
import com.github.omwah.SDFEconomy.location.PerWorldLocationTranslator;
import com.github.omwah.SDFEconomy.location.SetDestinationLocationTranslator;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Observer;
import java.util.logging.Level;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

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
        if (!checkForVault()) {
            getLogger().log(Level.SEVERE, "This plugin requires Vault. Please download the latest: http://dev.bukkit.org/server-mods/vault/");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        } 
        // So far only one storage method is implemented: YAML Storage
        this.getConfig().addDefault("storage.yaml.filename", "accounts.yaml");
        this.getConfig().addDefault("storage.yaml.commit_delay", 60L);
        this.getConfig().addDefault("location.translator", "multiverse");
        this.getConfig().addDefault("location.factions_support.enabled", true);
        this.getConfig().addDefault("location.factions_support.name", "factions");
        this.getConfig().addDefault("location.towny_support.enabled", true);
        this.getConfig().addDefault("location.towny_support.name", "towny");
                
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
        LocationTranslator locationTrans;
        if(translator_str.equalsIgnoreCase("multiverse")) {
            getLogger().info("Using Multiverse-Inventories location translator");
            locationTrans = new MultiverseInvLocationTranslator(this);
        } else if (translator_str.equalsIgnoreCase("multiinv")) {
            getLogger().info("Using MultInv location translator");
            locationTrans = new MultiInvLocationTranslator(this);
        } else if (translator_str.equalsIgnoreCase("worldinventories")) {
            getLogger().info("Using WorldInventories location translator");
            locationTrans = new MultiInvLocationTranslator(this);
        } else if (translator_str.equalsIgnoreCase("myworlds")) {
            getLogger().info("Using My Worlds location translator");
            locationTrans = new MyWorldsLocationTranslator(this);
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

        // Enable support for Factions accounts
        if(this.getConfig().getBoolean("location.factions_support.enabled")) {
            getLogger().info("Enabling Factions account support");
            locationTrans = new FactionsLocationSupport(locationTrans, getConfig().getString("location.factions_support.name"));
        }
        
        if(this.getConfig().getBoolean("location.towny_support.enabled")) {
            getLogger().info("Enabling Towny account support");
            locationTrans = new TownyLocationSupport(locationTrans, getConfig().getString("location.towny_support.name"));
        }
        
        // Create the API used both by Vault and the Plugin commands
        this.api = new SDFEconomyAPI(this.getConfig(), this.storage, locationTrans, getLogger());

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
        Locale locale = getLocale();
        for(Iterator cmd_iter = this.getDescription().getCommands().keySet().iterator(); cmd_iter.hasNext();) {
            // set the command executor for the Command
            PluginCommand curr_cmd = this.getCommand((String) cmd_iter.next());
            curr_cmd.setExecutor(new SDFEconomyCommandExecutor(this, curr_cmd, locale));
        }
        
        // Try and send metrics to MCStats
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not send data to MCStats!");
        }
    }
    
    private boolean checkForVault() {
        Plugin plugin = getServer().getPluginManager().getPlugin("Vault");
        if (plugin == null) {
            return false;
        } else {
            return true;
        }
    }
    
    /*
     * Called when the plug-in shuts down
     * One final commit
     */
    @Override
    public void onDisable() { 
        if (storage != null) {
            storage.commit();
        }
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
    
    /*
     * Gets locale from config or else returns the default
     */
    private Locale getLocale() {
        String language = this.getConfig().getString("locale.language");
        String country = this.getConfig().getString("locale.country");
        Locale locale;
        if(language != null && country != null) {
            locale = new Locale(language, country);
        } else if(language != null) {
            locale = new Locale(language);
        } else {
            this.getLogger().info("Locale not defined in config, using system default.");
            locale = Locale.getDefault();
        }
        this.getLogger().log(Level.INFO, "Using locale: {0}", locale);
        return locale;
    }
}
