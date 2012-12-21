package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.BankAccount;
import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankRemoveMemberCommand extends BasicCommand {

    private SDFEconomyAPI api;

    public BankRemoveMemberCommand(SDFEconomyAPI api) {
        super("bank removemember");

        this.api = api;
        
        setDescription("Remove a member from a bank");
        setUsage(this.getName() + " §8<account_name> <member_name>");
        setArgumentRange(2, 2);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_bank");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        String account_name = args[0];
        String member_name = args[1];
        BankAccount account = api.getBankAccount(account_name);

        if (account != null) {
            if(handler.hasAdminPermission(sender) || 
                    sender instanceof Player && account.isOwner(((Player)sender).getName())) {
                
                if (!account.isMember(member_name)) {
                    sender.sendMessage(member_name + " is not a member of bank: " + account_name);
                    return false;
                }
                
                account.removeMember(member_name);
                if (!account.isMember(member_name)) {
                    sender.sendMessage("Succesfully removed " + member_name + " from bank: " + account_name);
                } else {
                    sender.sendMessage("Failed to remove " + member_name + " from bank: " + account_name);
                    return false;
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
