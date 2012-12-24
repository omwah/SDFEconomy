package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.command.CommandSender;

public class PlayerDeleteCommand extends PlayerSpecificCommand {
    
    public PlayerDeleteCommand(SDFEconomyAPI api) {
        super("player delete", api);
        
        setDescription("Deletes a player account");
        setUsage(this.getName() + " §8<player_name> <location>");
        setArgumentRange(2, 2);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.admin");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 0, 1);

        if(ploc == null) {
            // Unable to succesfully get player name and or location, helper routine will send appropriate message
            return false;
        }
        // If we pass the above check we will have a valid player and location string
        // since both were passed as arguments via CommandHandler

        boolean success = api.deletePlayerAccount(ploc.playerName, ploc.locationName);
        if(success) {
            sender.sendMessage("Succesfully deleted player account: " + ploc.playerName + " @ " + ploc.locationName);
        } else {
            sender.sendMessage("Failed to delete player account: " + ploc.playerName + " @ " + ploc.locationName);
            return false;
        }
        
        return true;
    }
   
}
