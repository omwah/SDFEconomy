package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class PayCommand extends BasicCommand
{
    private final SDFEconomyAPI api;
    private CommandHandler commandHandler;

    public PayCommand(SDFEconomyAPI api,  CommandHandler commandHandler)
    {
        super("Pay");
        
        this.api = api;
        this.commandHandler = commandHandler;
        
        setDescription("Pay another player in your current location");
        setUsage("pay §8<player_name> <amount>");
        setArgumentRange(2, 2);
        setIdentifiers("pay");
        setPermission("sdfeconomy.pay_players");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String identifier, String[] args)
    {
        // This command only works for Players, it pays the other player in the current world
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String payer = player.getName();
            String location = api.getPlayerLocationName(payer);
            
            String payee = args[0];
            double amount = Double.parseDouble(args[1]);
            String amount_str = api.format(amount);
            
            // Be paranoid, check that sender has an account
            if (!api.hasAccount(payer, location)) {
                sender.sendMessage("You does not have an account @ " + location);
                return true;
            }
            
            // Do not proceed any further if there is no destination account in
            // existence
            if (!api.hasAccount(payee, location)) {
                sender.sendMessage(payee + " does not have an account @ " + location);
                return true;
            }
            
            // Withdraw money from player and make sure this command succeeds
            EconomyResponse resp_wd = api.withdrawPlayer(payer, amount);
           
            if (resp_wd.type != ResponseType.SUCCESS) {
                sender.sendMessage("You do not have enough money to pay " + amount_str + " to " + payee);
                return true;
            }
            
            // Despost in destination account, making sure if this fails to credit
            // back payer
            EconomyResponse resp_dep = api.depositPlayer(payee, amount);
            
            if (resp_dep.type != ResponseType.SUCCESS) {
                sender.sendMessage("Could not deposit " + amount_str + " into the account of " + payee + " crediting your account back.");
                resp_dep = api.depositPlayer(payer, amount);
                
                // Wow, something is fubar, offer our apologies
                if (resp_dep.type != ResponseType.SUCCESS) {
                    sender.sendMessage("Could not credit back " + amount_str + " to your account. Sorry :-(");
                    return false;
                }
            }
                    
            String balance = api.format(api.getBalance(payer, location));
            sender.sendMessage("Payment of " + amount_str + " to " + payee + " succeeded, your balance is now " + balance);
        } else {
            // This will only be sent when command issued from console
            sender.sendMessage("Pay command must be used by a player");
        }
        return true;
    }
}
