package com.github.omwah.SDFEconomy;

import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import com.Acrobot.ChestShop.Events.TransactionEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


/*
 * Listens for ChestShop events and uses them to better direct
 * the location of Vault calls into the API.
 */
public class ChestShopEventListener implements Listener {
    private final SDFEconomy plugin;
    private final SDFEconomyAPI api;

    /*
     * This listener needs to know about the plugin which it came from
     */
    public ChestShopEventListener(SDFEconomy plugin) {
        // Register the listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        this.plugin = plugin;
        this.api = plugin.getAPI();
    }
    
    /*
     */
    @EventHandler
    public void onPreTransactionEvent(PreTransactionEvent event) {

    }
    
    /*
     */
    @EventHandler
    public void onTransactionEvent(TransactionEvent event) {

    }
    
}
