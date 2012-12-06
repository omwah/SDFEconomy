package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;

public class BalanceCommand extends BasicCommand
{
    private final SDFEconomyAPI api;

    public BalanceCommand(SDFEconomyAPI api)
    {
        super("Balance");
        this.api = api;
        setDescription("Check your player economy account balance");
        setUsage("balance");
        setArgumentRange(0, 0);
        setIdentifiers("balance");
        setPermission("sdfeconomy.use_account");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String identifier, String[] args)
    {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            double balance = this.api.getBalance(player);
            sender.sendMessage("Your balance is: " + balance);
        } else {
            sender.sendMessage("Not a player");
        }
        return true;
    }
}
