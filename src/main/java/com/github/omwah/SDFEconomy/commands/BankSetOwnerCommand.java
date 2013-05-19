package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.BankAccount;
import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankSetOwnerCommand extends TranslatedCommand {

    private SDFEconomyAPI api;

    public BankSetOwnerCommand(SDFEconomyAPI api) {
        super("bank setowner");

        this.api = api;
        
        setDescription("Set a new owner for the bank account");
        setUsage(this.getName() + " §8<account_name> <new_owner>");
        setArgumentRange(2, 2);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_bank");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        String account_name = args[0];
        String new_owner = args[1];
        BankAccount account = api.getBankAccount(account_name);

        if (account != null) {
            if(handler.hasAdminPermission(sender) || 
                    sender instanceof Player && account.isOwner(((Player)sender).getName())) {
                String old_owner = account.getOwner();

                account.setOwner(new_owner);

                if (account.isOwner(new_owner)) {
                    sender.sendMessage("Succesfully set " + new_owner + " as owner of bank: " + account_name);
                } else {
                    sender.sendMessage("Failed to set " + new_owner + " as owner of bank: " + account_name);
                    return false;
                }

                // new owner does not need to a be a member of their own bank
                if (account.isMember(new_owner)) {
                    sender.sendMessage("Removing " + new_owner + " as member of bank: " + account_name);
                    account.removeMember(new_owner);
                }

                // Add previous owner as member of new bank
                if (!account.isMember(old_owner)) {
                    sender.sendMessage("Adding " + old_owner + " as member of bank: " + account_name);
                    account.addMember(old_owner);
                }
                
            } else {
                sender.sendMessage("You are not the owner of the bank: " + account_name);
                return false;
            }
        } else {
            sender.sendMessage("No bank named " + account_name + " was found");
            return false;
        }
            
        return true;
    }
   
}
