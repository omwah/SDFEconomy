package com.github.omwah.SDFEconomy.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class BankListCommand extends PlayerSpecificCommand
{
    public BankListCommand(SDFEconomyAPI api)
    {
        super("bank list", api);
        
        setDescription("List bank accounts");
        setUsage(this.getName() + " §8[owner] [location]");
        setArgumentRange(0, 2);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_bank");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 0, 1);

        if(ploc != null) {
            List<String> bank_names;
            if (sender == null || handler.hasPermission(sender, "sdfeconomy.admin") || ((Player)sender).isOp()) {
                // Get all banks for a location
                bank_names = api.getBanks();
            } else {
                // Only report those owned by player unless sender is op or console
                bank_names = api.getBanks(ploc.playerName, ploc.locationName);
            }
        } else {
            // Unable to succesfully get player name and or location, helper routine will send appropriate message
            return false;
        }
            
        return true;
    }
   
}
