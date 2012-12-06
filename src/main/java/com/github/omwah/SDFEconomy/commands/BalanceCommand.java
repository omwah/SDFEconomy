package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;

public class BalanceCommand extends BasicCommand
{
    private final SDFEconomyAPI api;
    private CommandHandler commandHandler;

    public BalanceCommand(SDFEconomyAPI api,  CommandHandler commandHandler)
    {
        super("Balance");
        
        this.api = api;
        this.commandHandler = commandHandler;
        
        setDescription("Check player account balance");
        setUsage("balance §8[player_name] [location]");
        setArgumentRange(0, 2);
        setIdentifiers("balance");
        setPermission("sdfeconomy.use_account");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String identifier, String[] args)
    {
        // If arguments are supplied then check for another players balance
        if (args.length > 0) {
            String destPlayer = args[0];
            
            String locationName = null;
            if(args.length > 1) {
                locationName = args[1];
            }
            
            // Make sure we are at console or sender has sufficient privileges
            // Also let player query themselves, in case they desire to 
            // check balances in other location
            if(sender == null || 
                    (this.commandHandler.hasPermission(sender, "sdfeconomy.admin") || 
                    ((Player)sender).isOp() ||
                    ((Player)sender).getName().equalsIgnoreCase(destPlayer) )) {
                
                // If the API can not determine the player's location, ie if they are offline
                // Then we need a location name specified as an argument
                if(api.getPlayerLocationName(destPlayer) == null && locationName == null) {
                    sender.sendMessage("Could not determine player's location, supply location string as second argument");
                    return true;
                }
                
                // Use the API's last location for player if none supplied as argument
                if(locationName == null) {
                    // This will not be executed if the player did not supply a location
                    locationName = api.getPlayerLocationName(destPlayer);
                }
                
                if(api.hasAccount(destPlayer, locationName)) {
                    double balance = this.api.getBalance(destPlayer, locationName);
                    sender.sendMessage(destPlayer + "'s balance @ " + locationName + " is: " + balance);
                } else {
                    sender.sendMessage("Could not find an account for " + destPlayer + " @ " + locationName);
                }
            } else {
                sender.sendMessage("Insufficient privileges to check another player's balance");
            }
           
        } else if (sender instanceof Player) {
            // No arguments and this is a player, check own balance
            // Player is logged in so location will be known
            Player player = (Player) sender;
            double balance = this.api.getBalance(player.getName());
            sender.sendMessage("Your balance is: " + balance);
        } else {
            // This will only be sent when command issued from console
            sender.sendMessage("Must supply player name as argument when command is run from console");
        }
        return true;
    }
}
