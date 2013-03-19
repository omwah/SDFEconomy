package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.CommandSender;

public class WithdrawAdminCommand extends PlayerSpecificCommand {
    
    public WithdrawAdminCommand(SDFEconomyAPI api) {
        super("withdraw", api);
   
        setDescription("Withdraw money from a player account, admin only");
        setUsage(this.getName() + " §8<player_name> <amount> [location]");
        setArgumentRange(2, 3);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.admin");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        // Command handler, checks permission, but check again just in case
        if(handler.hasAdminPermission(sender)) {
            PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 0, 2);
            if(ploc == null) {
                // getPlayerAndLocation will report to sender the reason why it failed
                return false;
            }
            
            // Try and parse amount, fail gracefully
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid amount specified: " + args[1]);
                return false;
            }
            
            if(api.hasAccount(ploc.playerName, ploc.locationName)) {
                EconomyResponse response = this.api.withdrawPlayer(ploc.playerName, amount, ploc.locationName);
                if (response.type == EconomyResponse.ResponseType.SUCCESS) {
                    String balance = api.format(this.api.getBalance(ploc.playerName, ploc.locationName));
                    sender.sendMessage("Successful withdraw of " + api.format(amount) + " from " + ploc.playerName + " @ " + ploc.locationName);                    
                    sender.sendMessage(ploc.playerName + "'s balance @ " + ploc.locationName + " is now: " + balance);
                } else {                   
                    sender.sendMessage(ploc.playerName + " does not have enough money to withdraw " + amount + " @ " + ploc.locationName);
                    return false;                    
                }
            } else {
                sender.sendMessage("Could not find an account for " + ploc.playerName + " @ " + ploc.locationName);
            }
        } else {
            sender.sendMessage("Insufficient privileges to set another player's balance");
        }
           
        return true;
    }
}
