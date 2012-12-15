package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;

public class SetCommand extends BasicCommand
{
    private final SDFEconomyAPI api;
    private CommandHandler commandHandler;

    public SetCommand(SDFEconomyAPI api,  CommandHandler commandHandler)
    {
        super("Set");
        
        this.api = api;
        this.commandHandler = commandHandler;
        
        setDescription("Set the balance for a player, admin only");
        setUsage("set §8<player_name> <amount> [location]");
        setArgumentRange(2, 3);
        setIdentifiers("set");
        setPermission("sdfeconomy.admin");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String identifier, String[] args)
    {
        // Make sure we are at console or sender has sufficient privileges
        // CommandHandler should handle making sure we have the number of required arguments
        if(sender == null || 
                (this.commandHandler.hasPermission(sender, "sdfeconomy.admin") || 
                ((Player)sender).isOp())) {
            
            String destPlayer = args[0];
            double amount = Double.parseDouble(args[1]);
            
            String locationName = null;
            if(args.length > 2) {
                locationName = args[2];
            }

            // If the API can not determine the player's location, ie if they are offline
            // Then we need a location name specified as an argument
            if(api.getPlayerLocationName(destPlayer) == null && locationName == null) {
                sender.sendMessage("Could not determine player's location, supply location string as third argument");
                return true;
            }

            // Use the API's last location for player if none supplied as argument
            if(locationName == null) {
                // This will not be executed if the player did not supply a location
                locationName = api.getPlayerLocationName(destPlayer);
            }

            if(api.hasAccount(destPlayer, locationName)) {
                this.api.setBalance(destPlayer, locationName, amount);
                String balance = api.format(this.api.getBalance(destPlayer, locationName));
                sender.sendMessage(destPlayer + "'s balance @ " + locationName + " is now: " + balance);
            } else {
                sender.sendMessage("Could not find an account for " + destPlayer + " @ " + locationName);
            }
        } else {
            sender.sendMessage("Insufficient privileges to check another player's balance");
        }
           
        return true;
    }
}
