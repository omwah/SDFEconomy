package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.command.CommandSender;

public class BankCreateCommand extends PlayerSpecificCommand {
    
    public BankCreateCommand(SDFEconomyAPI api) {
        super("bank create", api);
        
        setDescription("Creates a new bank account");
        setUsage(this.getName() + " §8<account_name> [owner] [location]");
        setArgumentRange(1, 3);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_bank");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 1, 2);

        if(ploc != null) {
            String account_name = args[0];
            
            EconomyResponse result = api.createBank(account_name, ploc.playerName, ploc.locationName);
            if(result.type == ResponseType.SUCCESS) {
                sender.sendMessage("Succesfully created bank: " + account_name);
            } else {
                sender.sendMessage("Failed to create bank: " + result.errorMessage);
                return false;
            }
        } else {
             // Unable to succesfully get player name and or location, helper routine will send appropriate message
            return false;
        }
        
        return true;
    }
   
}
