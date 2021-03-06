package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import com.github.omwah.omcommands.CommandHandler;
import java.util.ResourceBundle;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.CommandSender;

public class WithdrawAdminCommand extends PlayerAndLocationSpecificCommand {
    
    public WithdrawAdminCommand(SDFEconomyAPI api, ResourceBundle translation) {
        super("withdraw", api, translation);

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
                sender.sendMessage(getTranslation("AccountCommon-invalid_amount", args[1]));
                return false;
            }
            
            if(api.hasAccount(ploc.playerName, ploc.locationName)) {
                EconomyResponse response = this.api.withdrawPlayer(ploc.playerName, amount, ploc.locationName);
                if (response.type == EconomyResponse.ResponseType.SUCCESS) {
                    String balance = api.format(this.api.getBalance(ploc.playerName, ploc.locationName));
                    sender.sendMessage(getClassTranslation("withdraw_success", api.format(amount), ploc.playerName, ploc.locationName)); 
                    sender.sendMessage(getTranslation("AccountCommon-new_balance", ploc.playerName, ploc.locationName, balance));
                } else {                   
                    sender.sendMessage(getClassTranslation("withdraw_failure", ploc.playerName, ploc.locationName, amount));
                    return false;                    
                }
            } else {
                sender.sendMessage(getTranslation("AccountCommon-cannot_find_account", ploc.playerName, ploc.locationName));
            }
        } else {
            sender.sendMessage(getTranslation("AccountCommon-not_admin"));
        }
           
        return true;
    }
}
