package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.BankAccount;
import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankWithdrawCommand extends PlayerSpecificCommand {
    
    public BankWithdrawCommand(SDFEconomyAPI api) {
        super("bank withdraw", api);
        
        setDescription("Withdraw money from a bank into a player account");
        setUsage(this.getName() + " §8<bank_account> <amount> [player_name] [location]");
        setArgumentRange(2, 4);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_bank");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 2, 3);
        
        String bank_name = args[0];

        // Try and parse amount, fail gracefully
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("Invalid amount specified: " + args[1]);
            return false;
        }        
        
        BankAccount bank_account = api.getBankAccount(bank_name);
           
        // If arguments are supplied then check for another players balance
        if (ploc != null) {
            // Check that we have a valid player account
            if(!api.hasAccount(ploc.playerName, ploc.locationName)) {
                sender.sendMessage("Could not find player account for " + ploc.playerName + " @ " + ploc.locationName);
                return false;
            }
            
            // Check that the bank account exists
            if(bank_account == null) {
                sender.sendMessage("Could not find bank account: " + bank_name);
                return false;
            }
            
            // Check that location of player and bank location match
            if(!ploc.locationName.equalsIgnoreCase(bank_account.getLocation())) {
                sender.sendMessage("Can not withdraw from bank located @ " + bank_account.getLocation() + " to player @ " + ploc.locationName);
                return false;
            }
            
            // Now make sure player has permissions and do withdraw
            if(handler.hasAdminPermission(sender) || 
                    sender instanceof Player && bank_account.isOwner(((Player)sender).getName()) ||
                                                bank_account.isMember(((Player)sender).getName())) {
                
                EconomyResponse bank_w_res = api.bankWithdraw(bank_name, amount);
                if(bank_w_res.type != ResponseType.SUCCESS) {
                    sender.sendMessage("Could not withdraw from bank: " + bank_w_res.errorMessage);
                    return false;
                }
                
                EconomyResponse player_d_res = api.depositPlayer(ploc.playerName, amount, ploc.locationName);
                if(player_d_res.type != ResponseType.SUCCESS) {
                    sender.sendMessage("Could not deposit into player account: " + player_d_res.errorMessage);
                    return false;
                }

                sender.sendMessage("Succesfully withdrew " + api.format(amount) + " from bank: " + bank_name + " into the account of " + ploc.playerName);
            } else {
                sender.sendMessage("You are not the owner or a member of the bank: " + bank_name);
                return false;
            }
        } else {
            // Unable to succesfully get player name and or location, helper routine will send appropriate message
            return false;
        }
        
        return true;
    }
}
