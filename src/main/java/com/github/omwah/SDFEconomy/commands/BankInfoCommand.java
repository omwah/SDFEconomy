package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.BankAccount;
import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import com.github.omwah.omcommands.BasicCommand;
import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import com.google.common.base.Joiner;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankInfoCommand extends TranslatedCommand {

    private SDFEconomyAPI api;

    public BankInfoCommand(SDFEconomyAPI api) {
        super("bank info");

        this.api = api;
        
        setDescription("Displays bank account balance and members");
        setUsage(this.getName() + " §8<account_name>");
        setArgumentRange(1, 1);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.use_bank");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        String account_name = args[0];
        BankAccount account = api.getBankAccount(account_name);

        if (account != null) {
            if(handler.hasAdminPermission(sender) || 
                    sender instanceof Player && account.isOwner(((Player)sender).getName()) ||
                                                account.isMember(((Player)sender).getName())) {
                sender.sendMessage("§c-----[ " + "§f Bank Info: " + account_name + " §c ]-----");
                sender.sendMessage("Location: " + account.getLocation());
                sender.sendMessage("Balance: " + api.format(account.getBalance()));
                sender.sendMessage("Owner: " + account.getOwner());

                Joiner joiner = Joiner.on(", ");
                String member_list = joiner.join(account.getMembers());
                sender.sendMessage("Members: " + member_list);
            } else {
                sender.sendMessage("You are not the owner or a member of the bank: " + account_name);
                return false;
            }
        } else {
            sender.sendMessage("No bank named " + account_name + " was found");
            return false;
        }
            
        return true;
    }
   
}
