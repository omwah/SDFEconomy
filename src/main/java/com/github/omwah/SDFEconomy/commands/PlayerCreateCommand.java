package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import com.github.omwah.omcommands.CommandHandler;
import java.util.ResourceBundle;
import org.bukkit.command.CommandSender;

public class PlayerCreateCommand extends PlayerAndLocationSpecificCommand {
    
    public PlayerCreateCommand(SDFEconomyAPI api, ResourceBundle translation) {
        super("player create", api, translation);
        
        setDescription("Creates a new player account");
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

        boolean success = api.createPlayerAccount(ploc.playerName, ploc.locationName);
        if(success) {
            sender.sendMessage("Succesfully created player account: " + ploc.playerName + " @ " + ploc.locationName);
        } else {
            sender.sendMessage("Failed to create player account: " + ploc.playerName + " @ " + ploc.locationName);
            return false;
        }
        
        return true;
    }
   
}
