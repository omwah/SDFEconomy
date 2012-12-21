package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import org.bukkit.command.CommandSender;

public class BalanceCommand extends PlayerSpecificCommand {
    
    public BalanceCommand(SDFEconomyAPI api) {
        super("balance", api);
        
        setDescription("Check player account balance");
        setUsage(this.getName() + " §8[player_name] [location]");
        setArgumentRange(0, 2);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_account");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
         PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 0, 1);
         
        // If arguments are supplied then check for another players balance
        if (ploc != null) {
            if(api.hasAccount(ploc.playerName, ploc.locationName)) {
                String balance = api.format(this.api.getBalance(ploc.playerName, ploc.locationName));
                sender.sendMessage(ploc.playerName + "'s balance @ " + ploc.locationName + " is: " + balance);
            } else {
                sender.sendMessage("Could not find an account for " + ploc.playerName + " @ " + ploc.locationName);
            }
        } else {
            // Unable to succesfully get player name and or location, helper routine will send appropriate message
            return false;
        }
        
        return true;
    }
}
