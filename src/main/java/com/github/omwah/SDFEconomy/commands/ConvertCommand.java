package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.PatternSyntaxException;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

public class ConvertCommand extends BasicCommand {
        private SDFEconomyAPI api;
        private Server server;
    
    public ConvertCommand(SDFEconomyAPI api, Server server) {
        super("convert");
   
        this.api = api;
        this.server = server;
        
        setDescription("Convert from another Vault economy");
        setUsage(this.getName() + " §8<economy_name> <location1>=<scaling> [ <location2>=scaling ...]");
        setArgumentRange(2, 999); // Can specify many key val pairs
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.admin");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        // Command handler, checks permission, but check again just in case
        if(!handler.hasAdminPermission(sender)) {
            sender.sendMessage("Insufficient privileges to set another player's balance");
        }

        // Which economy to load from
        String economy_name = args[0];

        // Parse out location scaling key pairs
        HashMap<String, Double> location_scales = new HashMap<String, Double>();
        for(int arg_idx = 1; arg_idx < args.length; arg_idx++) {
            try {
                String[] loc_scal_pair = args[arg_idx].split("=", 2);
                String location_name = loc_scal_pair[0].trim();
                if(!api.validLocationName(location_name)) {
                    sender.sendMessage("Invalid location name: " + location_name);
                }
                
                location_scales.put(location_name, new Double(loc_scal_pair[1]));
            } catch(PatternSyntaxException e) {
                sender.sendMessage("Could not parse argument #" + arg_idx + " : " + args[arg_idx]);
                sender.sendMessage("Specify location=scaling pairs where location is a name and scaling a double");
                return false;
            }
        }
            
        Collection<RegisteredServiceProvider<Economy>> econs = this.server.getServicesManager().getRegistrations(Economy.class);
        if (econs == null || econs.size() < 2) {
            sender.sendMessage("You must have at least 1 other economy loaded to convert.");
            return false;
        }
        
        Economy src_econ = null;
        for (RegisteredServiceProvider<Economy> econ : econs) {
            String econName = econ.getProvider().getName().replace(" ", "");
            if (econName.equalsIgnoreCase(economy_name)) {
                src_econ = econ.getProvider();
            }
        }

        if (src_econ == null) {
            sender.sendMessage("Could not find " + economy_name + " loaded on the server, check your spelling");
            return false;
        }

        sender.sendMessage("This may take some time to convert, expect server lag.");
        for (OfflinePlayer op : server.getOfflinePlayers()) {
            String pName = op.getName();
            if (src_econ.hasAccount(pName)) {
                // Scale incoming balance to each destination locations
                double src_balance = src_econ.getBalance(pName);
                for(String location_name : location_scales.keySet()) {
                    if (api.hasAccount(pName, location_name)) {
                        sender.sendMessage(pName + " already has an account @ " + location_name + ", skipping.");
                        continue;
                    }
                    api.createPlayerAccount(pName, location_name);
                
                    double new_balance = src_balance * location_scales.get(location_name).doubleValue();
                    sender.sendMessage(pName + " @ " + location_name + " -> " + new_balance);
                    api.setBalance(pName, location_name, new_balance);
                }
            }
        }
        api.forceCommit();
        sender.sendMessage("Converson complete, please verify the data before using it.");
        
        return true;
    }
}
