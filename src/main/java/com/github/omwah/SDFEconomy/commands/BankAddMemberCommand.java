package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.BankAccount;
import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import com.github.omwah.omcommands.CommandHandler;
import com.github.omwah.omcommands.TranslatedCommand;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BankAddMemberCommand extends TranslatedCommand {

    private SDFEconomyAPI api;

    public BankAddMemberCommand(SDFEconomyAPI api, ResourceBundle translation) {
        super("bank addmember", translation);

        this.api = api;
        
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
                
                if (account.isMember(member_name)) {
                    sender.sendMessage(getClassTranslation("already_member", member_name, account_name));
                    return false;
                }
                
                account.addMember(member_name);
                if (account.isMember(member_name)) {
                    sender.sendMessage(getClassTranslation("add_success", member_name, account_name));
                } else {
                    sender.sendMessage(getClassTranslation("add_failed", member_name, account_name));
                    return false;
                }
                
            } else {
                sender.sendMessage(getTranslation("BankCommon-not_owner", account_name));
                return false;
            }
        } else {
            sender.sendMessage(getTranslation("BankCommon-bank_not_found", account_name));
            return false;
        }
            
        return true;
    }
   
}
