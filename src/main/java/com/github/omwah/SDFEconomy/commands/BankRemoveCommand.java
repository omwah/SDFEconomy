package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.BankAccount;
import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class BankRemoveCommand extends BasicCommand {

    private SDFEconomyAPI api;

    public BankRemoveCommand(SDFEconomyAPI api) {
        super("bank remove");

        this.api = api;
        
        setDescription("Removes a bank account");
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
                    sender instanceof Player && account.isOwner(((Player)sender).getName())) {
                String owner = account.getOwner();
                String location = account.getLocation();
                double bank_balance = account.getBalance();

                EconomyResponse result = api.deleteBank(account_name);
                if (result.type == ResponseType.SUCCESS) {
                    sender.sendMessage("Succesfully removed bank: " + account_name);
                } else {
                    sender.sendMessage("Failed to remove bank: " + account_name);
                    return false;
                }

                result = api.depositPlayer(owner, bank_balance, location);
                if (result.type == ResponseType.SUCCESS) {
                    sender.sendMessage("Deposited bank balance of: " + api.format(bank_balance) + " into player account for: " + owner);
                } else {
                    sender.sendMessage("Failed to deposited bank balance of: " + api.format(bank_balance) + " into player account for: " + owner);
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
