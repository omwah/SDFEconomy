package com.github.omwah.SDFEconomy.commands;

import com.github.omwah.SDFEconomy.SDFEconomyAPI;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ScaleCommand extends PlayerSpecificCommand {
    
    public ScaleCommand(SDFEconomyAPI api) {
        super("scale", api);
   
        setDescription("Scale the balance of all player accounts in a location, admin only");
        setUsage(this.getName() + " §8<amount> <location>");
        setArgumentRange(2, 2);
        setIdentifiers(this.getName());
        setPermission("sdfeconomy.admin");
    }

    @Override
    public boolean execute(CommandHandler handler, CommandSender sender, String label, String identifier, String[] args)
    {
        // Command handler, checks permission, but check again just in case
        if(handler.hasAdminPermission(sender)) {
            double scaling;
            try {
                scaling = new Double(args[0]).doubleValue();
            } catch (NumberFormatException e) {
                sender.sendMessage("Scaling amount: " + args[0] + " could not be converted to a number");
                return false;
            }
 
            String location = args[1];

            List<String> players_list = api.getPlayers(location);
            sender.sendMessage("Scaling " + players_list.size() + " players @ " + location + " by: " + scaling);
            for(String player : players_list) {
                double balance = api.getBalance(player, location);
                api.setBalance(player, location, balance * scaling);
            }
            sender.sendMessage("Done scaling"); 
        } else {
            sender.sendMessage("You are not an administrator");
        }
           
        return true;
    }
}
