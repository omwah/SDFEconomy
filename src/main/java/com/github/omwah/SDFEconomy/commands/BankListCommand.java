package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.BankAccount;
import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import com.github.omwah.omcommands.CommandHandler;
import java.util.List;
import java.util.ResourceBundle;
import org.bukkit.command.CommandSender;

public class BankListCommand extends PlayerAndLocationSpecificCommand
{
    public BankListCommand(SDFEconomyAPI api, ResourceBundle translation)
    {
        super("bank list", api, translation);
        
        setDescription("List bank accounts");
        setUsage(this.getName() + " §8[owner] [location]");
        setArgumentRange(0, 2);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_bank");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        List<BankAccount> bank_accounts;      
        if (handler.hasAdminPermission(sender) && args.length == 0) {
            // Get all banks for a location
            bank_accounts = api.getAllBanks();
        } else {
            PlayerAndLocation ploc = getPlayerAndLocation(handler, sender, args, 0, 1);

            if(ploc == null) {
                // Unable to succesfully get player name and or location, helper routine will send appropriate message
                return false;
            }
            
            // Only report those owned by player unless sender is op or console
            bank_accounts = api.getPlayerBanks(ploc.playerName, ploc.locationName);
        }
            
        sender.sendMessage("§c-----[ " + "§f Bank Accounts §c ]-----");
        for(BankAccount account : bank_accounts) {
            String balance_str = api.format(account.getBalance());
            if (handler.hasAdminPermission(sender)) {
                sender.sendMessage(account.getName() + " @ " + account.getLocation() + " : " + balance_str + ", Owner: " + account.getOwner());
            } else {
                sender.sendMessage(account.getName() + " : " + balance_str);
            }
        }
            
        return true;
    }
   
}
