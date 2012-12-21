package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.BankAccount;
import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankDepositCommand extends PlayerSpecificCommand {
    
    public BankDepositCommand(SDFEconomyAPI api) {
        super("bank deposit", api);
        
        setDescription("Deposit money to a bank from a player account");
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
        double amount = Double.parseDouble(args[1]);
        
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
                sender.sendMessage("Can not deposit from bank located @ " + bank_account.getLocation() + " from player @ " + ploc.locationName);
                return false;
            }
            
            // Now make sure player has permissions and do withdraw
            if(handler.hasAdminPermission(sender) || 
                    sender instanceof Player && bank_account.isOwner(((Player)sender).getName()) ||
                                                bank_account.isMember(((Player)sender).getName())) {
               
                EconomyResponse player_w_res = api.withdrawPlayer(ploc.playerName, amount, ploc.locationName);
                if(player_w_res.type != ResponseType.SUCCESS) {
                    sender.sendMessage("Could not withdraw from player account: " + player_w_res.errorMessage);
                    return false;
                }
                
                EconomyResponse bank_d_res = api.bankDeposit(bank_name, amount);
                if(bank_d_res.type != ResponseType.SUCCESS) {
                    sender.sendMessage("Could not deposit into bank: " + bank_d_res.errorMessage);
                    return false;
                }
                
                sender.sendMessage("Succesfully deposited " + api.format(amount) + " from " + ploc.playerName + " into bank: " + bank_name);
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
