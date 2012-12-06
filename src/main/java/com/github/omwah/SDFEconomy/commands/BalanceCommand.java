package com.github.omwah.SDFEconomy.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.omwah.SDFEconomy.SDFEconomy;
import com.github.omwah.SDFEconomy.SDFEconomyAPI;

public class BalanceCommand extends BasicCommand
{
    private final SDFEconomy plugin;

    public BalanceCommand(SDFEconomy plugin)
    {
        super("Balance");
        this.plugin = plugin;
        setDescription("Check your player economy account balance");
        setUsage("/sdfeconomy balance");
        setArgumentRange(0, 0);
        setIdentifiers("balance");
        setPermission("sdfeconomy.use_account");
    }

    @Override
    public boolean execute(CommandSender sender, String identifier, String[] args)
    {
        SDFEconomyAPI api = plugin.getAPI();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            double balance = api.getBalance(player);
            sender.sendMessage("Your balance is: " + balance);
        } else {
            sender.sendMessage("Not a player");
        }
        return true;
    }
}
