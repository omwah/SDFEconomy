package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class BankCreateCommand extends BasicCommand
{
    private final SDFEconomyAPI api;

    public BankCreateCommand(SDFEconomyAPI api)
    {
        super("bank create");
        
        this.api = api;
        
        setDescription("Creates a new bank account");
        setUsage(this.getName() + " §8<account_name> [owner] [location]");
        setArgumentRange(1, 3);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_bank");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        // CommandHandler should make sure the Player both has bank privileges and 
        // we get either 1 or 2 args
        String account_name = args[0];
        
        // Make sure we are at console or sender has sufficient privileges
        // if a owner name is specified
        String player_name;
        if (args.length > 1 && (sender == null || 
                (handler.hasPermission(sender, "sdfeconomy.admin") || 
                ((Player)sender).isOp()))) {
            
            // Op or admin creating a bank account for another player
            player_name = args[1];
            
        } else if (sender instanceof Player) {
            Player player_obj = (Player) sender;
            if(args.length > 1 && !args[1].equals(player_obj.getName())) {
                // Player tried to specify owner's name without sufficient privileges
                sender.sendMessage("Insufficient privileges to create a bank account for another player");
                return false;
            } else {
                player_name = ((Player) sender).getName();
            }
        } else {
           sender.sendMessage("Must specify player name and possibly location when using command from console");
           return false;
        }
   
        String location_name = null;
        if(args.length > 2) {
            location_name = args[2];
        }
            
        // Use the API's last location for player if none supplied as argument
        if(location_name == null) {
            // This will not be executed if the player did not supply a location
            location_name = api.getPlayerLocationName(player_name);
        }
            
        EconomyResponse result = api.createBank(account_name, player_name, location_name);
        if(result.type == ResponseType.SUCCESS) {
            sender.sendMessage("Succesfully created bank: " + account_name);
        } else {
            sender.sendMessage("Failed to create bank: " + result.errorMessage);
            return false;
        } 
        
        return true;
    }
   
}
